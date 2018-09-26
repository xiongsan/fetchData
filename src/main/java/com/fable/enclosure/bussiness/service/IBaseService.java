package com.fable.enclosure.bussiness.service;


import com.fable.enclosure.bussiness.entity.FileRelation;
import com.fable.enclosure.bussiness.exception.BussinessException;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.UnsupportedEncodingException;


/**
 * Created by bll on 2017/9/2.
 */
public interface IBaseService {

    BaseResponse service(JsonNode jsonNode);

    void showPic(FileRelation fileRelation) throws Exception;

    BaseResponse upload(FileRelation fileRelation) throws UnsupportedEncodingException;

    void download(FileRelation fileRelation);

}
