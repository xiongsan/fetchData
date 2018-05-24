package com.fable.enclosure.bussiness.interfaces;

import com.fable.enclosure.bussiness.entity.PageRequest;
import com.fable.enclosure.bussiness.entity.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.servlet.http.HttpServletRequest;

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
 * Time :15:52
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS,property="@class")
public interface BaseRequest<T> {

    T getParam();

    HttpServletRequest getRequest();

    int getPageNo();

    int getPageSize();
}
