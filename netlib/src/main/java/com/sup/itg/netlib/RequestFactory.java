package com.sup.itg.netlib;

import com.sup.itg.netlib.okhttpLib.AdapterBuilder;
import com.sup.itg.netlib.okhttpLib.GetBuilder;
import com.sup.itg.netlib.okhttpLib.PostBuilder;

import okhttp3.OkHttpClient;

import static com.sup.itg.netlib.ItgNetSend.GET;
import static com.sup.itg.netlib.ItgNetSend.POST;

public class RequestFactory {

    public static AdapterBuilder create(int type, OkHttpClient okHttpClient) {
        AdapterBuilder adapterBuilder = null;
        switch (type) {
            case GET:
                adapterBuilder = new GetBuilder(okHttpClient);
                break;
            case POST:
                adapterBuilder = new PostBuilder(okHttpClient);
                break;
        }

        return adapterBuilder;
    }
}
