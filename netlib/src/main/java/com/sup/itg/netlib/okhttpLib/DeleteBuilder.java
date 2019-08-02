package com.sup.itg.netlib.okhttpLib;

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
        builder.tag(mUrl);
        builder.url(mUrl);
        builder.delete(getRequestBody());
        Call call = mOkHttpClient.newCall(builder.build());
        return call;
    }
}
