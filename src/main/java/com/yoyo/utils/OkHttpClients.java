package com.yoyo.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class OkHttpClients {
    private OkHttpClients(){
    }

    public static String doGet(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
