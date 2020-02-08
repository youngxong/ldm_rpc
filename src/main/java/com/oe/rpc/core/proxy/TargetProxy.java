package com.oe.rpc.core.proxy;

import javassist.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TargetProxy {

    private ClassPool pool = ClassPool.getDefault();
    private Map<String, CtClass> ctClassMap = new ConcurrentHashMap();
    public static final String SUFFIX = "Impl";

    public TargetProxy() {
    }

    public CtClass markTargetClass(String clazzName, CtClass ctClass) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        CtClass cc = this.markClassBody(this.pool.get(ctClass.getName()), clazzName);
        cc.addInterface(this.pool.get(ctClass.getName()));
        cc.defrost();
        return cc;
    }

    private CtClass markClassBody(CtClass clazz, String className) throws NotFoundException, CannotCompileException {
        CtClass targetCtClass = null;
        if (null != this.ctClassMap.get(className + "Impl")) {
            targetCtClass = (CtClass)this.ctClassMap.get(className + "Impl");
        } else {
            targetCtClass = this.pool.makeClass(className + "Impl");
        }

        targetCtClass = this.createClassBody(targetCtClass, clazz.getDeclaredMethods(), clazz.getName());
        this.ctClassMap.put(className + "Impl", targetCtClass);
        return targetCtClass;
    }

    private CtClass createClassBody(CtClass ctClass, CtMethod[] methods, String service) throws NotFoundException, CannotCompileException {
        CtClass targetCtClass = ctClass;
        CtMethod[] arr$ = methods;
        int len$ = methods.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            CtMethod method = arr$[i$];
            String[] var10000 = new String[]{"name"};
            int parLen = 0;
            CtClass[] ctClasses = method.getParameterTypes();
            if (ctClasses != null) {
                parLen = ctClasses.length;
            }

            CtClass[] classes = method.getParameterTypes();
            CtClass returnClass = method.getReturnType();
            CtClass[] exClasses = method.getExceptionTypes();
            String methodName = method.getName();
            CtMethod ctMethod = CtNewMethod.make(returnClass, methodName, classes, exClasses, this.markMethodBody(parLen, service + "." + methodName, returnClass), targetCtClass);
            ctMethod.setModifiers(1);
            targetCtClass.addMethod(ctMethod);
        }

        return targetCtClass;
    }

    private String markMethodBody(int attr, String methodName, CtClass returnClass) throws NotFoundException, CannotCompileException {
        StringBuffer sb = new StringBuffer();

        sb.append("Object[] arr=new Object["+attr+"];");

        for(int index = 0; index < attr; ++index) {
            sb.append("arr["+index+"]="+ "$"+(index + 1)+";");
        }
        sb.append("com.oe.rpc.core.model.Param param=new com.oe.rpc.core.model.Param();");
        sb.append("param.setMethod(\""+methodName+"\");");
        sb.append("param.setParams(arr);");
        sb.append("com.oe.rpc.context.LdmRpcEventProcessor processor = com.oe.rpc.context.LdmRpcEventProcessor.newInstance();");
        if (returnClass != null) {
            sb.append("return (" + returnClass.getName() + ")processor.publish(new com.oe.rpc.core.events.RpcInvokeEvent(param));");
        }

        return "{" + sb.toString() + "}";
    }
}
