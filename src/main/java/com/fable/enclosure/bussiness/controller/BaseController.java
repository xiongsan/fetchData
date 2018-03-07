package com.fable.enclosure.bussiness.controller;

import com.alibaba.fastjson.JSON;
import com.fable.enclosure.bussiness.entity.FileRelation;
import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by bll on 2017/9/2.
 */
@Controller
@RequestMapping("baseController")
public class BaseController {

    @RequestMapping("/service")
    @ResponseBody
    public ServiceResponse service(HttpServletRequest request,@RequestBody ServiceRequest sr) throws UnsupportedEncodingException {
        IBaseService baseService = SpringContextUtil.getBean(sr.getServiceId(),IBaseService.class);
        return baseService.service(request,sr);
    }

    @RequestMapping("/easyPageService")
    @ResponseBody
    public ServiceResponse easyPageService(HttpServletRequest request) throws UnsupportedEncodingException{
        request.setCharacterEncoding("UTF-8");
        String serviceId = request.getParameter("serviceId");
        String method=request.getParameter("Method");
        ServiceRequest sr = JSON.parseObject(request.getParameter("param"),ServiceRequest.class);
        sr.setMethod(method);
        IBaseService baseService = SpringContextUtil.getBean(serviceId,IBaseService.class);
        return baseService.service(request,sr);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public ServiceResponse upload(@RequestParam("file") CommonsMultipartFile file){
        IBaseService baseService = SpringContextUtil.getBean(FileRelation.serviceId,IBaseService.class);
        FileRelation fileRelation = FileRelation.getFileRelation();
        fileRelation.setFile(file);
        return baseService.upload(fileRelation);
    }

    @RequestMapping("/showPic/{fileUrl}")
    public void showPic(HttpServletRequest request, HttpServletResponse response,@PathVariable String fileUrl) throws Exception {
        IBaseService baseService = SpringContextUtil.getBean(FileRelation.serviceId,IBaseService.class);
        FileRelation fileRelation = FileRelation.getFileRelation();
        fileRelation.setRequest(request);
        fileRelation.setResponse(response);
        fileRelation.setFileUrl(fileUrl);
        baseService.showPic(fileRelation);
    }

    @RequestMapping("/download/{fileName}/{fileUrl}")
    public void download(HttpServletRequest request,HttpServletResponse response,@PathVariable String fileName,@PathVariable String fileUrl){
        IBaseService baseService = SpringContextUtil.getBean(FileRelation.serviceId,IBaseService.class);
        FileRelation fileRelation = FileRelation.getFileRelation();
        fileRelation.setRequest(request);
        fileRelation.setResponse(response);
        fileRelation.setFileUrl(fileUrl);
        fileRelation.setFileName(fileName);
        baseService.download(fileRelation);
    }
}
