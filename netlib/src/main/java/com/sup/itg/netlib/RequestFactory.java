package com.sup.itg.netlib;

import com.sup.itg.netlib.okhttpLib.AdapterBuilder;
import com.sup.itg.netlib.okhttpLib.DeleteBuilder;
import com.sup.itg.netlib.okhttpLib.GetBuilder;
import com.sup.itg.netlib.okhttpLib.PostBuilder;
import com.sup.itg.netlib.okhttpLib.PutBuilder;

import okhttp3.OkHttpClient;

import static com.sup.itg.netlib.ItgNetSend.DELETE;
import static com.sup.itg.netlib.ItgNetSend.GET;
import static com.sup.itg.netlib.ItgNetSend.POST;
import static com.sup.itg.netlib.ItgNetSend.PUT;

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
            case DELETE:
                adapterBuilder = new DeleteBuilder(okHttpClient);
                break;
            case PUT:
                adapterBuilder = new PutBuilder(okHttpClient);
                break;
        }

        return adapterBuilder;
    }
}
