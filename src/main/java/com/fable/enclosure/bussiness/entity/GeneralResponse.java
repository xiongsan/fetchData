package com.fable.enclosure.bussiness.entity;

import com.fable.enclosure.bussiness.interfaces.BaseResponse;

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
 * Time :13:47
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public class GeneralResponse<T> implements BaseResponse {
    /*----正常请求返回值start-----*/

    private String status;

    private T data;

    private String tips;

    /*----正常请求返回值end-----*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", tips='" + tips + '\'' +
                '}';
    }
}
