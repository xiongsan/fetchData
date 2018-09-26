package com.fable.enclosure.bussiness.service.impl;

import com.fable.enclosure.bussiness.entity.FileRelation;
import com.fable.enclosure.bussiness.entity.MethodPropertiesCachedEntity;
import com.fable.enclosure.bussiness.exception.BussinessException;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fable.enclosure.bussiness.interfaces.Constants;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.ResultKit;
import com.fable.enclosure.bussiness.util.Tool;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bll on 2017/9/2.
 */
public class BaseServiceImpl implements IBaseService {

    private Map<String,MethodPropertiesCachedEntity> cachedMethod= Constants.cachedMethod;

    private static ObjectMapper mapper=Constants.getObjectMapper();

    public BaseResponse service(JsonNode node){
        try {
            return this.invokeMethodByMethodName(this.getClass(),node);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultKit.fail("调用后台服务出现异常"+e.getMessage());
        }
    }

    @Override
    public void showPic(FileRelation fileRelation) throws Exception {
        fileRelation.getRequest().setCharacterEncoding("utf-8");
        String path = getPath(fileRelation.getRequest());
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
    public BaseResponse upload(FileRelation fileRelation) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        String fileName = "";
        String fileUrl = "";

        if (fileRelation.getFile().isEmpty()) {
            map.put("fileName", fileName);
            map.put("fileUrl", fileUrl);
            return ResultKit.serviceResponse(map);
        }

        String path = getPath(fileRelation.getRequest());
        if (path == null) {
            throw new BussinessException("路径不能为空");
        }
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        fileName =fileRelation.getFile().getOriginalFilename();//antdesign提供的上传组件不需要转码
//        fileName = new String(fileRelation.getFile().getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
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

        File file = new File(getPath(request), fileUrl);

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
                //获取系统默认字符集，windows为GBK，linux为utf-8
                String charsetName=System.getProperty("sun.jnu.encoding");
                if("GBK".equals(charsetName)){
                    fileName=new String(fileName.getBytes("utf-8"), "ISO8859-1");
                }
                else{
                    fileName=URLDecoder.decode(fileName,"UTF-8");
                }
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

    private BaseResponse invokeMethodByMethodName(Class<?> classType,JsonNode node) throws BussinessException {
        String methodName = node.path("method").asText();
        MethodPropertiesCachedEntity methodPropertiesCachedEntity = this.getObjects(methodName, classType);
        return (BaseResponse) invokeWrap(methodPropertiesCachedEntity, node);
    }

    private MethodPropertiesCachedEntity getObjects(String methodName,Class<?> classType){
        MethodPropertiesCachedEntity methodPropertiesCachedEntity=this.cachedMethod.get(methodName);
        if(methodPropertiesCachedEntity==null){
            for(Method method:classType.getDeclaredMethods()){
                if (method.getName().equals(methodName)) {
                    Type[] type = method.getGenericParameterTypes();
                    method.setAccessible(true);
                    if(type.length==0){
                        methodPropertiesCachedEntity = new MethodPropertiesCachedEntity();
                        methodPropertiesCachedEntity.setMethod(method);
                        Transactional transactional= method.getAnnotation(Transactional.class);
                        if(transactional!=null){
                        methodPropertiesCachedEntity.setNeedTrans(true);
                        methodPropertiesCachedEntity.setIsolation(transactional.isolation());
                        methodPropertiesCachedEntity.setPropagation(transactional.propagation());
                        }
                        this.cachedMethod.put(methodName, methodPropertiesCachedEntity);
                        return methodPropertiesCachedEntity;
                    }
                    if(type.length==1){
                        JavaType javaType = mapper.getTypeFactory().constructType(type[0]);
                        methodPropertiesCachedEntity = new MethodPropertiesCachedEntity();
                        methodPropertiesCachedEntity.setMethod(method);
                        methodPropertiesCachedEntity.setJavaType(javaType);
                        Transactional transactional= method.getAnnotation(Transactional.class);
                        if(transactional!=null){
                            methodPropertiesCachedEntity.setNeedTrans(true);
                            methodPropertiesCachedEntity.setIsolation(transactional.isolation());
                            methodPropertiesCachedEntity.setPropagation(transactional.propagation());
                        }
                        this.cachedMethod.put(methodName, methodPropertiesCachedEntity);
                        return methodPropertiesCachedEntity;
                    }
                        throw new BussinessException(String.format("method :%s,in %s,parameter size >1 is not support current",methodName,classType.getName()));
                }
            }
            throw new BussinessException(String.format("method :%s,not exist in %s",methodName,classType.getName()));
        }
        return methodPropertiesCachedEntity;

    }
    private Object getJavaParam(JsonNode node, JavaType javaType){
        try{
            node = node.path("param");
            return mapper.readValue(node.toString(), javaType);
        }
        catch(Exception e){
            throw new BussinessException("处理json字符串到参数对象失败:" + node, e);
        }
    }

    private String getPath(HttpServletRequest request) {
        String methodName = FileRelation.method;
        //第一次为空的话，取0不会报空指针异常
        MethodPropertiesCachedEntity methodPropertiesCachedEntity = this.cachedMethod.get(methodName);
        try{
            if(methodPropertiesCachedEntity==null){
                Method[] methods = this.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        methodPropertiesCachedEntity = new MethodPropertiesCachedEntity();
                        methodPropertiesCachedEntity.setMethod(method);
                        method.setAccessible(true);
                        this.cachedMethod.put(methodName, methodPropertiesCachedEntity);
                        return  (String) method.invoke(this, request);
                    }
                }
            }
            Method m =methodPropertiesCachedEntity.getMethod();
            return  (String) m.invoke(this, request);
        }catch (Exception e){
            throw new BussinessException("look for file store path failed",e);
        }
    }

    private synchronized Object invokeWrap(MethodPropertiesCachedEntity entity,JsonNode node){
        Method method = entity.getMethod();
        if(!entity.isNeedTrans()){
            try{
                if(entity.getJavaType()==null)
                {
                    return method.invoke(this);
                }
                Object param = this.getJavaParam(node, entity.getJavaType());
                return  method.invoke(this,param);
            }catch (Exception e){
                e.printStackTrace();
                throw new BussinessException("normal invoke Exception", e);
            }
        }
        Object result;
        try{
            Tool.startTransaction(entity);
            if(entity.getJavaType()==null)
            {
                result= method.invoke(this);
            }
            else{
                Object param = this.getJavaParam(node, entity.getJavaType());
                result=  method.invoke(this,param);
            }
            Tool.endTransaction();
        }
        catch (Exception e){
            Tool.rollBack();
            throw new BussinessException("proxy invoke Exception",e);
        }

        return result;
    }

}
