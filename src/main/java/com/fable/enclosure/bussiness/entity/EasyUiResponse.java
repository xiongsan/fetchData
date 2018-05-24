package com.fable.enclosure.bussiness.entity;

import com.fable.enclosure.bussiness.interfaces.BaseResponse;

import java.util.List;

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
 * Time :14:01
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public class EasyUiResponse<T> implements BaseResponse {

    private int total; //总记录数

    private List<? extends T> Rows; //分页结果集

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<? extends T> getRows() {
        return Rows;
    }

    public void setRows(List<? extends T> rows) {
        Rows = rows;
    }

    @Override
    public String toString() {
        return "EasyUiResponse{" +
                "total=" + total +
                ", Rows=" + Rows +
                '}';
    }
}
