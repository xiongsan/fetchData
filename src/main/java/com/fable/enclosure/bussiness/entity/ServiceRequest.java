package com.fable.enclosure.bussiness.entity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wanghairui on 2017/9/4.
 */
public class ServiceRequest<T> {

    private int pageNo=0;

    private int pageSize;

    private T param;

    private HttpServletRequest request;

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
