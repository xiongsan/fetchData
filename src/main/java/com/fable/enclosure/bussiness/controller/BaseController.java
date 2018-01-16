package com.fable.enclosure.bussiness.controller;

import com.fable.enclosure.bussiness.entity.ResultKit;
import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import com.fable.enclosure.bussiness.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @RequestMapping("/upload/{serviceId}/{method}")
    @ResponseBody
    public ServiceResponse upload(@RequestParam("file") CommonsMultipartFile file,@PathVariable String serviceId,@PathVariable String method){
        IBaseService baseService = SpringContextUtil.getBean(serviceId,IBaseService.class);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setMethod(method);
        serviceRequest.setFile(file);
        return baseService.upload(serviceRequest);
    }

    @RequestMapping("/showPic/{serviceId}/{method}/{name}")
    public void showPic(HttpServletRequest request, HttpServletResponse response, @PathVariable String serviceId,@PathVariable String method,@PathVariable String name) throws Exception {
        IBaseService baseService = SpringContextUtil.getBean(serviceId,IBaseService.class);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setMethod(method);
        serviceRequest.setRequest(request);
        serviceRequest.setResponse(response);
        serviceRequest.setTemp(name);
        baseService.showPic(serviceRequest);
    }
}
