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
 * Time :13:52
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public class DataTableResponse<T> implements BaseResponse {
    /*----dataTable请求返回值start-----*/

    private List<T> data;

    private int recordsTotal;

    private int recordsFiltered;

    /*----dataTable请求返回值end-----*/

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    @Override
    public String toString() {
        return "DataTableResponse{" +
                "data=" + data +
                ", recordsTotal=" + recordsTotal +
                ", recordsFiltered=" + recordsFiltered +
                '}';
    }
}
