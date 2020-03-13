package com.sup.itg.netlib.okhttpLib;

import android.text.TextUtils;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DeleteBuilder  extends AdapterBuilder {

    public DeleteBuilder(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    public Call createCall() {
        Request.Builder builder = new Request.Builder();
        Headers headers = getHeader();
        if (headers != null) {
            builder.headers(headers);
        }
        builder.url(mUrl);
        builder.tag(TextUtils.isEmpty(mTag)?mUrl:mTag);
        builder.delete(getRequestBody());
        Call call = mOkHttpClient.newCall(builder.build());
        return call;
    }
}
