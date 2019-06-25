package com.sup.itg.netlib.okhttpLib.interfaces;

import okhttp3.Response;

public interface ItgCallback{
    void onFailure(String er);

    void onResponse(String result, int code);

}
