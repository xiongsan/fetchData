package com.fable.enclosure.bussiness.controller;

import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by bll on 2017/9/2.
 */
@Controller
public class BaseController {

    @RequestMapping("/fableService")
    @ResponseBody
    public ServiceResponse service(HttpServletRequest request) throws UnsupportedEncodingException{
        request.setCharacterEncoding("UTF-8");
        return doService(request);
    }

    public ServiceResponse doService(HttpServletRequest request) {
        String method = request.getParameter("method");
        String serviceId = request.getParameter("serviceId");
        IBaseService baseService = SpringContextUtil.getBean(serviceId,IBaseService.class);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setMethod(method);
        serviceRequest.setRequest(request);
        return baseService.service(serviceRequest);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public ServiceResponse upload(@RequestParam("file") CommonsMultipartFile file){
        IBaseService baseService = SpringContextUtil.getBean("fileService",IBaseService.class);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setMethod("getFileFolder");
        serviceRequest.setFile(file);
        return baseService.upload(serviceRequest);
    }

    @RequestMapping("/showPic/{fileUrl}")
    public void showPic(HttpServletRequest request, HttpServletResponse response,@PathVariable String fileUrl) throws Exception {
        IBaseService baseService = SpringContextUtil.getBean("fileService",IBaseService.class);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setMethod("getFileFolder");
        serviceRequest.setRequest(request);
        serviceRequest.setResponse(response);
        serviceRequest.setFileUrl(fileUrl);
        baseService.showPic(serviceRequest);
    }

    @RequestMapping("/download/{fileName}/{fileUrl}")
    public void download(HttpServletRequest request,HttpServletResponse response,@PathVariable String fileName,@PathVariable String fileUrl){
        IBaseService baseService = SpringContextUtil.getBean("fileService",IBaseService.class);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setRequest(request);
        serviceRequest.setMethod("getFileFolder");
        serviceRequest.setResponse(response);
        serviceRequest.setFileUrl(fileUrl);
        serviceRequest.setFileName(fileName);
        baseService.download(serviceRequest);
    }

    @RequestMapping("/delete/{fileUrl}")
    public ServiceResponse download(HttpServletRequest request,HttpServletResponse response,@PathVariable String fileUrl){
        IBaseService baseService = SpringContextUtil.getBean("fileService",IBaseService.class);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setRequest(request);
        serviceRequest.setMethod("getFileFolder");
        serviceRequest.setResponse(response);
        serviceRequest.setFileUrl(fileUrl);
        return baseService.deleteFile(serviceRequest);
    }
}
