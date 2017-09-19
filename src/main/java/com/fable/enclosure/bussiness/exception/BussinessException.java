package com.fable.enclosure.bussiness.exception;

/**
 * Created by bll on 2017/9/2.
 */
public class BussinessException extends  RuntimeException {
    public BussinessException(String cause, Throwable throwable){
        super(cause,throwable);
    }
    public BussinessException(String message){
        super(message);
    }
}
