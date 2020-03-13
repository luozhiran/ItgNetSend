package com.sup.itg.netlib.okhttpLib;

import android.text.TextUtils;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PutBuilder extends AdapterBuilder {
    public PutBuilder(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    public Call createCall() {
        Request.Builder builder = new Request.Builder();
        Headers headers = getHeader();
        if (headers != null) {
            builder.headers(headers);
        }

        builder.tag(TextUtils.isEmpty(mTag)?mUrl:mTag);
        builder.url(mUrl);
        builder.put(getRequestBody());
        Call call = mOkHttpClient.newCall(builder.build());
        return call;
    }
}
