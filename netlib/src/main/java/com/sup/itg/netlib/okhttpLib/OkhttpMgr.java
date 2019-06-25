package com.sup.itg.netlib.okhttpLib;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkhttpMgr {
    public OkHttpClient mOkHttpClient;

    public OkhttpMgr() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(35, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }


}
