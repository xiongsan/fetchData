package com.fable.enclosure.bussiness.controller;

import com.fable.enclosure.bussiness.entity.FileRelation;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fable.enclosure.bussiness.interfaces.Constants;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by bll on 2017/9/2.
 */
@Controller
@RequestMapping("baseController")
public class BaseController {

    @RequestMapping("/service")
    @ResponseBody
    public BaseResponse service(@RequestBody String string) throws IOException {
        return getBaseResponse(string);
    }

    @RequestMapping("/noAuth")
    @ResponseBody
    public BaseResponse noAuth(@RequestBody String string) throws IOException {
        return getBaseResponse(string);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public BaseResponse upload(HttpServletRequest request,@RequestParam("file") CommonsMultipartFile file) throws UnsupportedEncodingException {
        IBaseService baseService = SpringContextUtil.getBean(FileRelation.serviceId,IBaseService.class);
        FileRelation fileRelation = FileRelation.getFileRelation();
        fileRelation.setFile(file);
        fileRelation.setRequest(request);
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

    private BaseResponse getBaseResponse(String string) throws IOException {
        JsonNode node= Constants.mapper.readTree(string);
        String serviceId=node.path("serviceId").asText();
        IBaseService baseService = SpringContextUtil.getBean(serviceId,IBaseService.class);
        return baseService.service(node);
    }
}
