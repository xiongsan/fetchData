package com.fable.enclosure.bussiness.interfaces;

import com.fable.enclosure.bussiness.entity.MethodPropertiesCachedEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hairui on 2018/9/17.
 */
public class Constants {
    public static final Map<String,MethodPropertiesCachedEntity> cachedMethod=new ConcurrentHashMap<>();

    private static ObjectMapper mapper;

    public static ObjectMapper getObjectMapper(){
        if(mapper==null){
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper;
        }
        return mapper;
    }
}
