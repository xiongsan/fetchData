package com.fable.enclosure.bussiness.service.impl;

import com.alibaba.fastjson.JSON;
import com.fable.enclosure.bussiness.entity.ResultKit;
import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.exception.BussinessException;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import com.fable.enclosure.bussiness.util.Tool;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bll on 2017/9/2.
 */
public class BaseServiceImpl implements IBaseService {
    public ServiceResponse service(ServiceRequest serviceRequest) throws BussinessException {
        new ServiceResponse();
        try {
            return this.invokeMethodByMethodName(this.getClass(), serviceRequest);
        } catch (Exception e) {
            throw new BussinessException("调用方法出现异常", e);
        }
    }

    @Override
    public void showPic(ServiceRequest request) throws Exception{
        request.getRequest().setCharacterEncoding("utf-8");
        String path = getPath(request);
        if(path==null){
            throw new BussinessException("路径不能为空");
        }
        FileInputStream inputStream = new FileInputStream(path+File.separator+request.getTemp());
        int i = inputStream.available();
        byte[] buff = new byte[i];
        inputStream.read(buff);
        inputStream.close();
        request.getResponse().setContentType("image/*");
        OutputStream out = request.getResponse().getOutputStream();
        out.write(buff);
        out.close();
    }

    @Override
    public ServiceResponse upload(ServiceRequest request) {
        Map<String, Object> map = new HashMap<>();
        String fileName = "";
        String fileUrl = "";

        if (request.getFile().isEmpty()) {
            map.put("fileName", fileName);
            map.put("fileUrl", fileUrl);
            return ResultKit.serviceResponse(map);
        }
        //String url = System.getProperty("user.dir");
       // String path = url.substring(0, url.lastIndexOf(File.separator)) + File.separator + "user" + File.separator + "uploadFile";
        String path = getPath(request);
        if(path==null){
            throw new BussinessException("路径不能为空");
        }
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        fileName = request.getFile().getOriginalFilename();
        fileUrl = Tool.newGuid() ;

        File tempFile = new File(path, fileUrl);

        try {
            request.getFile().transferTo(tempFile);
            map.put("fileName", fileName);
            map.put("fileUrl", fileUrl);
            return ResultKit.serviceResponse(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("fileName", fileName);
            map.put("fileUrl", fileUrl);
            return ResultKit.serviceResponse(map);
        }
    }

    private ServiceResponse invokeMethodByMethodName(Class<?> classType, ServiceRequest request) throws IllegalAccessException, InvocationTargetException {
        String methodName = request.getMethod();
        Method[] methods = classType.getDeclaredMethods();
        ServiceResponse serviceResponse = null;
		String beanName = (classType.getAnnotation(Service.class)).value();
        //注意，classType.newInstance,和spring中的不是一个实例，不方便向其注入属性。
        Object instance = SpringContextUtil.getBean(beanName);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                Class[] parameterTypes = method.getParameterTypes();
                Object[] arguments = getObjectArray(parameterTypes, request);
                try{
                    if (arguments.length != 0) {
                        serviceResponse = (ServiceResponse) method.invoke(instance, arguments);
                    } else {
                        serviceResponse = (ServiceResponse) method.invoke(instance);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return serviceResponse;
    }

    private Object[] getObjectArray(Class[] parameterTypes, ServiceRequest request) {
        List<Object> list = new ArrayList<>();
        HttpServletRequest servletRequest = request.getRequest();
        Map<String, Object> params = (Map) JSON.parse(servletRequest.getParameter("data"));
        switch (parameterTypes.length) {
            case 0:
                break;
            case 1:
                if (parameterTypes[0].isAssignableFrom(HttpServletRequest.class)) {
                    list.add(servletRequest);
                } else {
                    list.add(params);
                }
                break;
            case 2:
                list.add(servletRequest);
                list.add(params);
        }
        return list.toArray();
    }

    private String getPath(ServiceRequest request){
        String methodName = request.getMethod();
        Method[] methods = this.getClass().getDeclaredMethods();
        String path = null;
        String beanName = (this.getClass().getAnnotation(Service.class)).value();
        //注意，classType.newInstance,和spring中的不是一个实例，不方便向其注入属性。
        Object instance = SpringContextUtil.getBean(beanName);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                try{
                    path = (String) method.invoke(instance);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return path;
    }
}
