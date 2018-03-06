package com.fable.enclosure.bussiness.entity;


import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by Wanghairui on 2017/6/9.
 */
public class ServiceResponse<T> {

    /*----正常请求返回值start-----*/

    private String status;

    private Object data;

    private String tips;

    /*----正常请求返回值end-----*/




    /*----easyUi请求返回值start-----*/

    private int total; //总记录数

    private List<? extends T> Rows; //分页结果集

     /*----easyUi请求返回值end-----*/





      /*----bootstrap请求返回值start-----*/

    List<T> list;

    private int recordsTotal;

    private int recordsFiltered;

    /*----bootstrap请求返回值end-----*/



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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public static <T> ServiceResponse<T> wrapEasyUiResponse(Page<T> page){
        ServiceResponse<T> response = new ServiceResponse<T>();
        response.setTotal((int)page.getTotal());
        response.setRows(page);
        return response;
    }

    public static <T> ServiceResponse<T> wrap(List<T> page,int no,int size) {
        ServiceResponse<T> response = new ServiceResponse<T>();
        response.setRecordsTotal(page.size());
        response.setRecordsFiltered(page.size());
        if(no*size>page.size()){
            page=page.subList((no-1)*size,page.size());
        }
        else{
            page = page.subList((no-1)*size,(no-1)*size+size);
        }
        response.setList(page);
        return response;
    }

    public static <T> ServiceResponse<T> wrap(Page<T> page){
        ServiceResponse<T> response = new ServiceResponse<T>();
        response.setRecordsTotal((int)page.getTotal());
        response.setRecordsFiltered((int)page.getTotal());
        response.setList(page);
        return response;
    }
}
