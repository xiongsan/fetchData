package com.fable.enclosure.bussiness.service;


import com.fable.enclosure.bussiness.entity.*;
import com.fable.enclosure.bussiness.exception.BussinessException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * Created by bll on 2017/9/2.
 */
public interface IBaseService {

    ServiceResponse service(ServiceRequest<Map<String,Object>> serviceRequest) throws BussinessException;

    PageResponse bootstrapPage(ServiceRequest<PageRequest<Map<String,Object>>> serviceRequest) throws BussinessException;

    EasyUiPageResponse easyPageService(HttpServletRequest request) throws BussinessException;

    void showPic(ServiceRequest serviceRequest) throws Exception;

    ServiceResponse upload(ServiceRequest serviceRequest);

    void download(ServiceRequest serviceRequest);

    ServiceResponse OrdinaryService(ServiceRequest<Map<String,Object>> serviceRequest) throws BussinessException;

}
