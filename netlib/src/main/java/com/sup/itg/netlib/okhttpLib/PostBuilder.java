package com.sup.itg.netlib.okhttpLib;

import com.sup.itg.netlib.okhttpLib.interfaces.Builder;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PostBuilder extends AdapterBuilder {

    public PostBuilder(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    final public PostBuilder appendUrl(String key, String value) {
        mParamSb.append(key).append("#").append(value).append("$");
        return this;
    }

    @Override
    public Call createCall() {
        Request.Builder builder = new Request.Builder();
        Headers headers = getHeader();
        if (headers != null) {
            builder.headers(headers);
        }
        String url = getParam();
        builder.tag(url);
        builder.url(url);
        builder.post(getRequestBody());
        Call call = mOkHttpClient.newCall(builder.build());
        return call;
    }

}
