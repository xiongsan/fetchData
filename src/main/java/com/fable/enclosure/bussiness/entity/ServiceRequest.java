package com.fable.enclosure.bussiness.entity;

import com.fable.enclosure.bussiness.interfaces.BaseRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wanghairui on 2017/9/4.
 */
public class ServiceRequest<T> implements BaseRequest<T>{

    private T param;

    private HttpServletRequest request;

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public int getPageNo() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return 0;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "param=" + param +
                ", request=" + request +
                '}';
    }
}
