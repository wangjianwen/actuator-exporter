package com.yoyo.utils;

import java.util.Map;

public final class Maps {
    private Maps(){
    }

    public static double safedGetValue(Map<String,Number> map, String key){
        return map.get(key) == null ? 0.0: map.get(key).doubleValue();
    }
}
