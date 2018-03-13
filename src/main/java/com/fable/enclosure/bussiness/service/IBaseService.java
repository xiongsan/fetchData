package com.fable.enclosure.bussiness.service;


import com.fable.enclosure.bussiness.entity.FileRelation;
import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.exception.BussinessException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;


/**
 * Created by bll on 2017/9/2.
 */
public interface IBaseService {

    ServiceResponse service(HttpServletRequest request) throws BussinessException;

    void showPic(FileRelation fileRelation) throws Exception;

    ServiceResponse upload(FileRelation fileRelation) throws UnsupportedEncodingException;

    void download(FileRelation fileRelation);

}
