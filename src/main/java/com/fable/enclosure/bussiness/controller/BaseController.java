package com.fable.enclosure.bussiness.controller;

import com.fable.enclosure.bussiness.entity.*;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by bll on 2017/9/2.
 */
@Controller
@RequestMapping("baseController")
public class BaseController {

    @RequestMapping("/oldService")
    @ResponseBody
    public ServiceResponse service(HttpServletRequest request) throws UnsupportedEncodingException{
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        String serviceId = request.getParameter("serviceId");
        IBaseService baseService = SpringContextUtil.getBean(serviceId,IBaseService.class);
        ServiceRequest<Map<String,Object>> serviceRequest = new ServiceRequest<>();
        serviceRequest.setMethod(method);
        serviceRequest.setRequest(request);
        return baseService.service(serviceRequest);
    }

    @RequestMapping("/newService")
    @ResponseBody
    public ServiceResponse service(HttpServletRequest request,@RequestBody Map<String,Object> param) throws UnsupportedEncodingException {
        IBaseService baseService = SpringContextUtil.getBean(param.get("serviceId").toString(),IBaseService.class);
        ServiceRequest<Map<String,Object>> serviceRequest = new ServiceRequest<>();
        serviceRequest.setParam(param);
        serviceRequest.setRequest(request);
        return baseService.OrdinaryService(serviceRequest);
    }

    @RequestMapping("/bootstrapPageService")
    @ResponseBody
    public PageResponse pageService(HttpServletRequest request,@RequestBody PageRequest<Map<String,Object>> param) throws UnsupportedEncodingException{
        request.setCharacterEncoding("UTF-8");
        IBaseService baseService = SpringContextUtil.getBean(param.getParam().get("serviceId").toString(),IBaseService.class);
        ServiceRequest<PageRequest<Map<String,Object>>> serviceRequest = new ServiceRequest<>();
        serviceRequest.setParam(param);
        serviceRequest.setRequest(request);
        return baseService.bootstrapPage(serviceRequest);
    }

    @RequestMapping("/easyPageService")
    @ResponseBody
    public EasyUiPageResponse easyPageService(HttpServletRequest request) throws UnsupportedEncodingException{
        request.setCharacterEncoding("UTF-8");
        String serviceId = request.getParameter("serviceId");
        IBaseService baseService = SpringContextUtil.getBean(serviceId,IBaseService.class);
        return baseService.easyPageService(request);
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
}
