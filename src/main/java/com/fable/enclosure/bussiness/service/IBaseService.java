package com.fable.enclosure.bussiness.service;


import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fable.enclosure.bussiness.entity.ServiceResponse;
import com.fable.enclosure.bussiness.exception.BussinessException;


/**
 * Created by bll on 2017/9/2.
 */
public interface IBaseService {
    ServiceResponse service(ServiceRequest serviceRequest) throws BussinessException;
}
