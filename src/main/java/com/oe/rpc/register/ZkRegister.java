package com.oe.rpc.register;

import com.oe.rpc.exception.LdmRpcException;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ZkRegister extends AbstractRegisterCenter<ServiceInfo> {

    private static final Logger LOG = LoggerFactory.getLogger(ZkRegister.class);

    private final String ROOT_PATH="/ldm-prc";

    public final static String SLASH = "/";

    public final static String SPLIT = "&";

    final CountDownLatch connectedSignal = new CountDownLatch(1);

    ZkRegisterConfig zkConfig;

    ZooKeeper zooKeeper;


    @Override
    public void init(AbstractRegisterConfig config) {
        this.zkConfig=(ZkRegisterConfig) config;
        connect();
        createRoot();

    }

    @Override
    public void connect() {
        try {
            zooKeeper=new ZooKeeper(zkConfig.getUrl(), zkConfig.getTimeOut(), new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                        LOG.info("zookeeper 连接成功!");
                    }
                }
            });
            connectedSignal.await();
        } catch (IOException e) {
            LOG.error("zooKeeper connect fail!",e);
            throw new LdmRpcException("zooKeeper connect fail");
        } catch (InterruptedException e) {
            LOG.error("zooKeeper connect fail!",e);
            throw new LdmRpcException("zooKeeper connect fail");
        }
    }

    @Override
    public void createRoot() {
        try {
            Stat exists = zooKeeper.exists(ROOT_PATH, true);
            if(null==exists){
                zooKeeper.create(ROOT_PATH,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            LOG.error("create root fail!",e);
            throw new LdmRpcException("create root fail");
        } catch (InterruptedException e) {
            LOG.error("create root fail!",e);
            throw new LdmRpcException("create root fail");
        }
    }

    @Override
    public void register(ServiceInfo serviceInfo) {
        String serviceName = serviceInfo.getServiceName();
        String serviceNode=ROOT_PATH+SLASH+serviceName;
        try {
            Stat exists = zooKeeper.exists(serviceNode, true);
            if(exists==null){
                zooKeeper.create(serviceNode,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            LOG.info("拼接的zk信息:{}",appendChildNode(serviceInfo));
            zooKeeper.create(appendChildNode(serviceInfo),"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }catch (Exception e){
            LOG.error("register service fail!",e);
            throw new LdmRpcException("register service fail!");
        }

    }

    private String appendChildNode(ServiceInfo serviceInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(ROOT_PATH+SLASH+serviceInfo.getServiceName());
        sb.append(SLASH);
        sb.append(serviceInfo.getUrl());
        sb.append("?");
        sb.append("version=").append(serviceInfo.getVersion());
        sb.append(SPLIT);
        sb.append("interface=").append(serviceInfo.getServiceName());
        sb.append(SPLIT);
        sb.append("methods=").append(serviceInfo.getMethods());
        sb.append(SPLIT);
        sb.append("weights=").append(serviceInfo.getWeights());
        return sb.toString();
    }

    @Override
    public ServiceInfo get(String serviceName) {
        return null;
    }

    private  List<ServiceInfo> convert(List<String> children) {
        List<ServiceInfo> infos = new ArrayList<>();
        for (String child : children) {
            ServiceInfo serviceInfo = new ServiceInfo();
            String[] info = child.split("\\?");
            serviceInfo.setUrl(info[0]);
            Map<String, String> map = turnMap(info[1]);
            serviceInfo.setVersion(map.get("version"));
            serviceInfo.setServiceName(map.get("interface"));
            serviceInfo.setMethods(map.get("methods"));
            serviceInfo.setWeights(Integer.parseInt(map.get("weights")));
            infos.add(serviceInfo);
        }
        return infos;
    }

    private Map<String,String> turnMap(String s) {
        String[] split = s.split("&");
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            String ab = split[i];
            int index = ab.indexOf("=");
            map.put(ab.substring(0,index),ab.substring(index+1,ab.length()));
        }
        return map;
    }

    @Override
    public List<ServiceInfo> list(String serviceName) {
        String path=ROOT_PATH+SLASH+serviceName;
        try {
            List<String> children = zooKeeper.getChildren(path, true);
            LOG.info("获取的zk信息：{}",children.toString());
            return  convert(children);
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("get services fail");
        }
    }

    @Override
    public void close() {
        try {
            if(zooKeeper!=null){
                zooKeeper.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw  new RuntimeException("zooKeeper close fail");
        }
    }
}
