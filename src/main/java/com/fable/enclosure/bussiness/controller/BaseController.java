package com.fable.enclosure.bussiness.controller;

import com.fable.enclosure.bussiness.entity.ResultKit;
import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import org.springframework.stereotype.Controller;
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

    @RequestMapping("upload")
    @ResponseBody
    public ServiceResponse upload(@RequestParam("file") CommonsMultipartFile file){
        return ResultKit.serviceResponse(upload(file));
    }

    @RequestMapping("/showPic")
    public void showPic(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String name = request.getParameter("name");
        String url = System.getProperty("user.dir");
        String path = url.substring(0, url.lastIndexOf(File.separator)) + File.separator + "user" + File.separator + "uploadFile";
        FileInputStream inputStream = new FileInputStream(path+File.separator+name);
        int i = inputStream.available();
        byte[] buff = new byte[i];
        inputStream.read(buff);
        inputStream.close();
        response.setContentType("image/*");
        OutputStream out = response.getOutputStream();
        out.write(buff);
        out.close();
    }

    private Map<String, Object> upload(MultipartFile file) {

        Map<String, Object> map = new HashMap<>();
        String fileName = "";
        String fileUrl = "";

        if (file.isEmpty()) {
            map.put("fileName", fileName);
            map.put("fileUrl", fileUrl);
            return map;
        }


        //String path = request.getSession().getServletContext().getRealPath("upload");
        String url = System.getProperty("user.dir");
        String path = url.substring(0, url.lastIndexOf(File.separator)) + File.separator + "user" + File.separator + "uploadFile";
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String uuid = UUID.randomUUID().toString();
        fileName = file.getOriginalFilename();
        fileUrl = uuid ;

        File tempFile = new File(path, fileUrl);

        try {
            file.transferTo(tempFile);
            map.put("fileName", fileName);
            map.put("fileUrl", fileUrl);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("fileName", fileName);
            map.put("fileUrl", fileUrl);
            return map;
        }
    }
}
