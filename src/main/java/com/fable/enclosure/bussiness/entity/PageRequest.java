package com.fable.enclosure.bussiness.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/5/23
 * Time :15:55
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageRequest<T>{

    private int pageNo=0;

    private int pageSize;

    private T param;

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

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ",param="+getParam()+
                '}';
    }
}
