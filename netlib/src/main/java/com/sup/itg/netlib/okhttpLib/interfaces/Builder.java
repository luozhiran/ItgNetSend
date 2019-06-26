package com.sup.itg.netlib.okhttpLib.interfaces;

import android.os.Handler;

import java.io.File;

import okhttp3.Callback;
import okhttp3.Response;

public interface Builder {
    Builder addParam(String key, String value);

    Builder addHeader(String key, String value);

    Builder url(String url);

    void send(ItgCallback callback);

    void send(Handler handler, int what, int errorWhat);

    void send(Callback response);

    Builder addFile(File file);

    Builder addContent(String content,String mediaType);
}
