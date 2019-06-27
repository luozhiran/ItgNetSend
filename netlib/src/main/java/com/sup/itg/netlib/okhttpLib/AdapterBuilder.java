package com.sup.itg.netlib.okhttpLib;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.sup.itg.netlib.ItgNetSend;
import com.sup.itg.netlib.okhttpLib.interfaces.Builder;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class AdapterBuilder implements Builder {
    protected OkHttpClient mOkHttpClient;
    protected String mUrl = ItgNetSend.itg().itgSet().getItgUrl();
    protected StringBuilder mHeaderSb = new StringBuilder();
    protected StringBuilder mParamSb = new StringBuilder();
    protected List<File> mFiles;
    protected List<String> mContents;
    protected List<String> mContentMediaTypes;
    protected List<String> mContentNames;

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


    private void initList() {
        if (mContentNames == null) {
            mContentNames = new ArrayList<>();
        }

        if (mContents == null) {
            mContents = new ArrayList<>();
        }
        if (mContentMediaTypes == null) {
            mContentMediaTypes = new ArrayList<>();
        }
    }

    @Override
    public Builder addFile(File file) {
        if (mFiles == null) {
            mFiles = new ArrayList<>();
        }
        if (file != null) {
            mFiles.add(file);
        }
        return this;
    }

    @Override
    public Builder addContent(String content, String mediaType) {
        initList();
        mContents.add(content);
        mContentNames.add("");
        mContentMediaTypes.add(mediaType);
        return this;
    }


    @Override
    public Builder addContent(String content, String contentFlag, String mediaType) {
        initList();
        mContents.add(content);
        mContentNames.add(contentFlag);
        mContentMediaTypes.add(mediaType);
        return this;
    }


    protected Headers getHeader() {
        if (!TextUtils.isEmpty(mHeaderSb) && mHeaderSb.length() > 0) {
            Headers.Builder builder = new Headers.Builder();
            String[] key_value = mHeaderSb.toString().split("[$]");
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
            String[] key_value = mParamSb.toString().split("[$]");
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


    protected RequestBody getRequestBody() {
        if (mContents != null && mContents.size() == 1 && mFiles == null) {
            return getUpdateStringRequestBody(mContentMediaTypes.get(0), mContents.get(0));
        } else if (mFiles != null && mFiles.size() == 0) {
            return getUpdateFileRequestBody(mFiles.get(0));
        } else if (!TextUtils.isEmpty(mParamSb)&&mContents == null&&mFiles == null) {
            return getFormBody();
        } else {
            return getMultipartBody();
        }
    }


    public MediaType getFileType(String fileName) {
        if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
            return MediaType.parse("image/jpg");
        } else {
            return null;
        }
    }


    private FormBody getFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        if (!TextUtils.isEmpty(mParamSb) && mParamSb.length() > 0) {
            String[] key_value = mParamSb.toString().split("[$]");
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

    private RequestBody getUpdateStringRequestBody(String mediaType, String content) {
        MediaType mt = MediaType.parse(mediaType);
        RequestBody body = RequestBody.create(mt, content);
        return body;
    }


    private RequestBody getUpdateFileRequestBody(File file) {
        MediaType mt = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mt, file);
        return body;
    }

    private MultipartBody getMultipartBody() {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (!TextUtils.isEmpty(mParamSb) && mParamSb.length() > 0) {
            String[] key_value = mParamSb.toString().split("[$]");
            if (key_value == null || key_value.length == 0) return builder.build();
            for (String value : key_value) {
                String[] s = value.split("#");
                if (s != null && s.length == 2) {
                    builder.addFormDataPart(s[0], s[1]);
                }
            }
        }
        if (mContents != null && mContents.size() > 0) {
            int count = 0;
            for (String s : mContents) {
                MediaType mediaType = MediaType.parse(mContentMediaTypes.get(count));
                RequestBody requestBody = RequestBody.create(mediaType, s);
                builder.addFormDataPart(mContentNames.get(count), null, requestBody);
                count++;
            }
        }
        if (mFiles != null) {
            for (File file : mFiles) {
                RequestBody fileBody = RequestBody.create(getFileType(file.getName()), file);
                builder.addFormDataPart("file", file.getName(), fileBody);
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
