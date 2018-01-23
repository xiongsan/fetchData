package com.fable.enclosure.bussiness.service;


import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.exception.BussinessException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by bll on 2017/9/2.
 */
public interface IBaseService {

    ServiceResponse service(ServiceRequest serviceRequest) throws BussinessException;

    void showPic(ServiceRequest serviceRequest) throws Exception;

    ServiceResponse upload(ServiceRequest serviceRequest);

    void download(ServiceRequest serviceRequest);
}
