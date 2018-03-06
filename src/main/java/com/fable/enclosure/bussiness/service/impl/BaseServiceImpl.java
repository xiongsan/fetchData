package com.fable.enclosure.bussiness.service.impl;

import com.alibaba.fastjson.JSON;
import com.fable.enclosure.bussiness.entity.FileRelation;
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
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bll on 2017/9/2.
 */
public class BaseServiceImpl implements IBaseService {

    public ServiceResponse service(HttpServletRequest request,ServiceRequest serviceRequest) throws BussinessException {
        try {
            return this.invokeMethodByMethodName(this.getClass(),request,serviceRequest);
        } catch (Exception e) {
            throw new BussinessException("调用方法出现异常", e);
        }
    }

    @Override
    public void showPic(FileRelation fileRelation) throws Exception {
        fileRelation.getRequest().setCharacterEncoding("utf-8");
        String path = getPath();
        if (path == null) {
            throw new BussinessException("路径不能为空");
        }
        FileInputStream inputStream = new FileInputStream(path + File.separator + fileRelation.getFileUrl());
        int i = inputStream.available();
        byte[] buff = new byte[i];
        inputStream.read(buff);
        inputStream.close();
        fileRelation.getResponse().setContentType("image/*");
        OutputStream out = fileRelation.getResponse().getOutputStream();
        out.write(buff);
        out.close();
    }

    @Override
    public ServiceResponse upload(FileRelation fileRelation) {
        Map<String, Object> map = new HashMap<>();
        String fileName = "";
        String fileUrl = "";

        if (fileRelation.getFile().isEmpty()) {
            map.put("fileName", fileName);
            map.put("fileUrl", fileUrl);
            return ResultKit.serviceResponse(map);
        }

        String path = getPath();
        if (path == null) {
            throw new BussinessException("路径不能为空");
        }
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        fileName = fileRelation.getFile().getOriginalFilename();
        fileUrl = Tool.newGuid();

        File tempFile = new File(path, fileUrl);

        try {
            fileRelation.getFile().transferTo(tempFile);
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
    public void download(FileRelation fileRelation) {
        String fileName = fileRelation.getFileName();
        String fileUrl = fileRelation.getFileUrl();
        HttpServletResponse response = fileRelation.getResponse();
        HttpServletRequest request = fileRelation.getRequest();
        if (StringUtils.isEmpty(fileUrl)) {
            throw new BussinessException("下载文件名不能为空");
        }

        File file = new File(getPath(), fileUrl);

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

    private ServiceResponse invokeMethodByMethodName(Class<?> classType,HttpServletRequest request,ServiceRequest sr) throws IllegalAccessException, InvocationTargetException {
        String methodName=sr.getMethod();
        Method[] methods = classType.getDeclaredMethods();
        ServiceResponse serviceResponse = null;
        String beanName = (classType.getAnnotation(Service.class)).value();
        //注意，classType.newInstance,和spring中的不是一个实例，不方便向其注入属性。
        Object instance = SpringContextUtil.getBean(beanName);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                Object[] arguments = getObjectArray(request,method, sr);
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

    private Object[] getObjectArray(HttpServletRequest servletRequest,Method method, ServiceRequest sr) {
        List<Object> list = new ArrayList<>();
        Object param;
        if(!(sr.getPageNo()==0)){
            param = sr;
        }
        else{
            param = sr.getParam();
        }

        Type[] t = method.getGenericParameterTypes();//获取参数泛型
        Type paramType;
        if(t.length>1){
            paramType = t[1];
            param = JSON.parseObject(JSON.toJSONString(param), paramType);
        }
        else if(t.length==1){
            paramType = t[0];
            param = JSON.parseObject(JSON.toJSONString(param), paramType);
        }

        Class[] parameterTypes = method.getParameterTypes();
        switch (parameterTypes.length) {
            case 0:
                break;
            case 1:
                if (parameterTypes[0].isAssignableFrom(HttpServletRequest.class)) {
                    list.add(servletRequest);
                } else {
                    list.add(param);
                }
                break;
            case 2:
                list.add(servletRequest);
                list.add(param);
        }
        return list.toArray();
    }

    private String getPath() {
        String methodName = FileRelation.method;
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
