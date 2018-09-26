package com.fable.enclosure.bussiness.entity;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.lang.reflect.Method;

/**
 * Created by hairui on 2018/9/26.
 */
public class MethodPropertiesCachedEntity {

    private Method method;

    private JavaType javaType;

    private boolean needTrans;

    private Propagation propagation;

    private Isolation isolation;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public JavaType getJavaType() {
        return javaType;
    }

    public void setJavaType(JavaType javaType) {
        this.javaType = javaType;
    }

    public boolean isNeedTrans() {
        return needTrans;
    }

    public void setNeedTrans(boolean needTrans) {
        this.needTrans = needTrans;
    }

    public Propagation getPropagation() {
        return propagation;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }

    public Isolation getIsolation() {
        return isolation;
    }

    public void setIsolation(Isolation isolation) {
        this.isolation = isolation;
    }
}
