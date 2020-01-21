package com.example.newtest.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static final String ROOT_URL = "http://115.68.223.21/";
    private static final String EV_URL = "https://www.ev.or.kr/";

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

    private static Retrofit getEvRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(EV_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getEvApiService() {
        return getEvRetrofitInstance().create(ApiService.class);
    }

    //    private static final String KAKAO_URL = "https://kapi.kakao.com/";
//    private static Retrofit getKakaoRetrofitInstance() {
//        return new Retrofit.Builder()
//                .baseUrl(KAKAO_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
//
//    public static ApiService getKakaoApiService() {
//        return getKakaoRetrofitInstance().create(ApiService.class);
//    }
}