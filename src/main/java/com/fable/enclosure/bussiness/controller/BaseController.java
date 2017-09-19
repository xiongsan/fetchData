package com.fable.enclosure.bussiness.controller;

import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.service.IBaseService;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
}
