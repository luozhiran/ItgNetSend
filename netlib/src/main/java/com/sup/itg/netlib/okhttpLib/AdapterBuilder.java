package com.sup.itg.netlib.okhttpLib;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.sup.itg.netlib.ItgNetSend;
import com.sup.itg.netlib.okhttpLib.interfaces.Builder;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public abstract class AdapterBuilder implements Builder {
    protected OkHttpClient mOkHttpClient;
    protected String mUrl = ItgNetSend.itg().itgSet().getItgUrl();
    protected StringBuilder mHeaderSb = new StringBuilder();
    protected StringBuilder mParamSb = new StringBuilder();

    public AdapterBuilder(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    @Override
    final public Builder addParam(String key, String value) {
        mParamSb.append(key).append("#").append(value).append("$");
        return this;
    }

    @Override
    final public Builder addHeader(String key, String value) {
        mHeaderSb.append(key).append("#").append(value).append("$");
        return this;
    }


    @Override
    final public Builder url(String url) {
        mUrl = url;
        return this;
    }


    protected Headers getHeader() {
        if (!TextUtils.isEmpty(mHeaderSb) && mHeaderSb.length() > 0) {
            Headers.Builder builder = new Headers.Builder();
            String[] key_value = mHeaderSb.toString().split("\\$");
            if (key_value == null || key_value.length == 0) return null;
            for (String value : key_value) {
                String[] s = value.split("#");
                if (s != null && s.length == 2) {
                    builder.add(s[0], s[1]);
                }
            }
            return builder.build();
        }
        return null;
    }

    protected String getParam() {
        if (!TextUtils.isEmpty(mParamSb) && mParamSb.length() > 0) {
            Uri.Builder urlBuild = Uri.parse(mUrl).buildUpon();
            String[] key_value = mParamSb.toString().split("//$");
            if (key_value == null || key_value.length == 0) return mUrl;
            for (String value : key_value) {
                String[] s = value.split("#");
                if (s != null && s.length == 2) {
                    urlBuild.appendQueryParameter(s[0], s[1]);
                }
            }
            mUrl = urlBuild.build().toString();
            return mUrl;
        } else {
            return mUrl;
        }
    }

    protected FormBody getFromBody() {
        FormBody.Builder builder = new FormBody.Builder();
        if (!TextUtils.isEmpty(mParamSb) && mParamSb.length() > 0) {
            String[] key_value = mParamSb.toString().split("//$");
            if (key_value == null || key_value.length == 0) return builder.build();
            for (String value : key_value) {
                String[] s = value.split("#");
                if (s != null && s.length == 2) {
                    builder.add(s[0], s[1]);
                }
            }
        }
        return builder.build();
    }


    public abstract Call createCall();

    @Override
    final public void send(final ItgCallback callback) {
        Call call = createCall();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()) {
                } else {
                    callback.onFailure(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()) {

                } else {
                    callback.onResponse(response.body().string(), response.code());
                }
            }
        });
    }

    @Override
    public void send(final Handler handler, final int what, final int errorWhat) {
        Call call = createCall();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = errorWhat;
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()) {

                } else {
                    Message msg = Message.obtain();
                    msg.what = what;
                    msg.obj = response;
                    if (response.isSuccessful()) {
                        msg.obj = response.body().string();
                    } else {
                        msg.obj = response.body().string();
                    }
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public void send(Callback callback) {
        Call call = createCall();
        call.enqueue(callback);
    }

}
