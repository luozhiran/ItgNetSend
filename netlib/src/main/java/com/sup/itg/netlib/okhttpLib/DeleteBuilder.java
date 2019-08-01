package com.sup.itg.netlib.okhttpLib;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class DeleteBuilder  extends AdapterBuilder {

    public DeleteBuilder(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    public Call createCall() {
        return null;
    }
}
