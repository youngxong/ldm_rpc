/**
 * @Project: SanxiapayCommon
 * @Title: ReadPropertyUtil.java
 * @Package com.hundsun.epay.util.file
 * @Description: TODO(用一句话描述该文件做什么)
 * @author tongjunpeng yedm@hundsun.com
 * @date 2014-4-21 下午5:32:34
 * @Copyright: 2014 www.hundsun.com All Rights Reserved.
 * @version V1.0.0.0 
 */
package com.oe.rpc.common;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


public class ReadPropertyUtil {

	
	private static Map<String,Map<String,String>> proMap=new HashMap<String,Map<String,String>>();


	private static Map<String,String> readProperty(String path) throws IOException{
		Properties pro = new Properties();
		Map<String,String> map=new HashMap<String,String>();
		Resource cr = new ClassPathResource(path);
		InputStream in = cr.getInputStream();
		pro.load(in);
		for( Entry<Object, Object> s:pro.entrySet()){
			map.put(s.getKey().toString(), s.getValue()!=null?s.getValue().toString():null);
		}
		return map;
	}

	public static Map<String,String> readProperty(String path,boolean isReLoad) throws IOException{
		Map<String,String> map=new HashMap<String,String>();
		if(isReLoad){
			Properties pro = new Properties();
			Resource cr = new ClassPathResource(path);
			InputStream in = cr.getInputStream();
			pro.load(in);
			for( Entry<Object, Object> s:pro.entrySet()){
				map.put(s.getKey().toString(), s.getValue()!=null?s.getValue().toString():null);
			}
			proMap.put(path, map);
		}else{
			map=proMap.get(path);
			if(map==null||map.isEmpty()){
				map=readProperty(path);
				proMap.put(path, map);
			}
		}
		return map;
	}
}
