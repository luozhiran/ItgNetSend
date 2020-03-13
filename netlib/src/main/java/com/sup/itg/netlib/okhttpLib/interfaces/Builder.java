package com.sup.itg.netlib.okhttpLib.interfaces;

import android.os.Handler;

import java.io.File;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Cookie;

public interface Builder {
    Builder addParam(String key, String value);

    Builder addHeader(String key, String value);

    Builder url(String url);

    void send(ItgCallback callback);

    void send(Handler handler, int what, int errorWhat);

    void send(Callback response);

    Builder addFile(File file);

    Builder addFile(String fileName, File file);

    Builder addFile(String fileName, String mediaType, File file);

    Builder addContent(String content, String mediaType);

    Builder addContent(String content, String contentFlag, String mediaType);

    Builder addInterva(File file, long offset);

    Builder addCookie(Cookie cookie);

    Builder addCookie(List<Cookie> cookie);
    Builder addTag(String tag);
}
