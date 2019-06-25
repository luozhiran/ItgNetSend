package com.sup.itg.netlib.okhttpLib;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostBuilder extends AdapterBuilder {

    public PostBuilder(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    public Call createCall() {
        Request.Builder builder = new Request.Builder();
        Headers headers = getHeader();
        if (headers != null) {
            builder.headers(headers);
        }
        builder.tag(mUrl);
        builder.url(mUrl);
        builder.post(getFromBody());
        Call call = mOkHttpClient.newCall(builder.build());
        return call;
    }

}
