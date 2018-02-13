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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
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
    public void showPic(ServiceRequest request) throws Exception {
        request.getRequest().setCharacterEncoding("utf-8");
        String path = getPath(request);
        if (path == null) {
            throw new BussinessException("路径不能为空");
        }
        FileInputStream inputStream = new FileInputStream(path + File.separator + request.getFileUrl());
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
        if (path == null) {
            throw new BussinessException("路径不能为空");
        }
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        fileName = request.getFile().getOriginalFilename();
        fileUrl = Tool.newGuid();

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

    @Override
    public void download(ServiceRequest serviceRequest) {
        String fileName = serviceRequest.getFileName();
        String fileUrl = serviceRequest.getFileUrl();
        HttpServletResponse response = serviceRequest.getResponse();
        HttpServletRequest request = serviceRequest.getRequest();
        if (StringUtils.isEmpty(fileUrl)) {
            throw new BussinessException("下载文件名不能为空");
        }

        File file = new File(getPath(serviceRequest), fileUrl);

        if (!file.exists()) {
            throw new BussinessException("上传文件丢失");
        }
        response.setCharacterEncoding("UTF-8");
        //IE 浏览器解决乱码
        String agent = request.getHeader("User-Agent").toUpperCase();
        if (agent.indexOf("MSIE") > 0
                || (agent.contains("RV") && !agent.contains("FIREFOX"))) {
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\"");

        OutputStream os = null;
        FileInputStream fis = null;
        try {
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            byte[] b = new byte[100];
            int c;
            while ((c = fis.read(b)) > 0) {
                os.write(b, 0, c);
            }
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ServiceResponse deleteFile(ServiceRequest serviceRequest) {
        File file = new File(getPath(serviceRequest), serviceRequest.getFileUrl());
        if (file.exists()) {
            if (file.delete())
                return ResultKit.success();
            return ResultKit.fail("删除文件失败");
        }
        return ResultKit.fail("文件不存在");
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
                try {
                    if (arguments.length != 0) {
                        serviceResponse = (ServiceResponse) method.invoke(instance, arguments);
                    } else {
                        serviceResponse = (ServiceResponse) method.invoke(instance);
                    }
                } catch (Exception e) {
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

    private String getPath(ServiceRequest request) {
        String methodName = request.getMethod();
        Method[] methods = this.getClass().getDeclaredMethods();
        String path = null;
        String beanName = (this.getClass().getAnnotation(Service.class)).value();
        //注意，classType.newInstance,和spring中的不是一个实例，不方便向其注入属性。
        Object instance = SpringContextUtil.getBean(beanName);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                try {
                    path = (String) method.invoke(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }
}
