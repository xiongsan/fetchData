package com.fable.enclosure.bussiness.service;


import com.fable.enclosure.bussiness.entity.FileRelation;
import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.exception.BussinessException;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by bll on 2017/9/2.
 */
public interface IBaseService {

    ServiceResponse service(HttpServletRequest request,ServiceRequest serviceRequest) throws BussinessException;

    void showPic(FileRelation fileRelation) throws Exception;

    ServiceResponse upload(FileRelation fileRelation);

    void download(FileRelation fileRelation);

}
