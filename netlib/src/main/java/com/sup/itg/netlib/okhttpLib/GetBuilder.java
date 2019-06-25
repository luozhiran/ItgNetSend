package com.sup.itg.netlib.okhttpLib;

import android.os.Handler;
import android.text.TextUtils;

import com.sup.itg.netlib.ItgNetSend;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetBuilder extends AdapterBuilder {

    public GetBuilder(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    public Call createCall() {
        Request.Builder builder = new Request.Builder();
        Headers headers = getHeader();
        if (headers != null) {
            builder.headers(headers);
        }
        String url = getParam();
        builder.url(url);
        builder.tag(url);
        builder.get();
        Call call = mOkHttpClient.newCall(builder.build());
        return call;
    }


}
