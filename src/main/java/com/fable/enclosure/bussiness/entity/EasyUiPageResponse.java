package com.fable.enclosure.bussiness.entity;

import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyUiPageResponse<T> {



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

    public static <T> EasyUiPageResponse<T> wrap(Page<T> page){
        EasyUiPageResponse<T> response = new EasyUiPageResponse<T>();
        response.setTotal((int)page.getTotal());
        response.setRows(page);
        return response;
    }
}
