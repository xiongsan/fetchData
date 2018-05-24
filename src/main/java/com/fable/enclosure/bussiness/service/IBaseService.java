package com.fable.enclosure.bussiness.service;


import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fable.enclosure.bussiness.entity.FileRelation;
import com.fable.enclosure.bussiness.exception.BussinessException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;


/**
 * Created by bll on 2017/9/2.
 */
public interface IBaseService {

    BaseResponse service(HttpServletRequest request) throws BussinessException;

    void showPic(FileRelation fileRelation) throws Exception;

    BaseResponse upload(FileRelation fileRelation) throws UnsupportedEncodingException;

    void download(FileRelation fileRelation);

}
