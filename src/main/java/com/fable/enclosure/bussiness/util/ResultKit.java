package com.fable.enclosure.bussiness.util;

import com.fable.enclosure.bussiness.entity.DataTableResponse;
import com.fable.enclosure.bussiness.entity.EasyUiResponse;
import com.fable.enclosure.bussiness.entity.GeneralResponse;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by Wanghairui on 2017/6/9.
 */
public class ResultKit {

    public static <T> BaseResponse serviceResponse(T param){
        GeneralResponse<T> serviceResponse = new GeneralResponse<>();
        serviceResponse.setData(param);
        serviceResponse.setStatus("1");
        return serviceResponse;
    }

    public static BaseResponse success(){
        GeneralResponse serviceResponse = new GeneralResponse();
        serviceResponse.setStatus("1");
        return serviceResponse;
    }

    public static BaseResponse fail(){
        GeneralResponse serviceResponse = new GeneralResponse();
        serviceResponse.setStatus("-1");
        return  serviceResponse;
    }

    public static BaseResponse fail(String message){
        GeneralResponse serviceResponse = new GeneralResponse();
        serviceResponse.setStatus("-1");
        serviceResponse.setTips(message);
        return serviceResponse;
    }

    public static <T> BaseResponse wrapEasyUiResponse(Page<T> page){
        EasyUiResponse<T> response = new EasyUiResponse<>();
        response.setTotal((int)page.getTotal());
        response.setRows(page);
        return response;
    }

    public static <T> BaseResponse wrap(List<T> page, int no, int size) {
        DataTableResponse<T> response = new DataTableResponse<>();
        response.setRecordsTotal(page.size());
        response.setRecordsFiltered(page.size());
        if(no*size>page.size()){
            page=page.subList((no-1)*size,page.size());
        }
        else{
            page = page.subList((no-1)*size,(no-1)*size+size);
        }
        response.setData(page);
        return response;
    }

    public static <T> BaseResponse wrap(Page<T> page){
        DataTableResponse<T> response = new DataTableResponse<>();
        response.setRecordsTotal((int)page.getTotal());
        response.setRecordsFiltered((int)page.getTotal());
        response.setData(page);
        return response;
    }
}
