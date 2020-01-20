package com.sup.itg.netlib.okhttpLib;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.sup.itg.netlib.IntervalBody;
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
import okhttp3.Cookie;
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
    protected StringBuilder mParamSb = new StringBuilder();//以表单的心事上传
    protected List<File> mFiles;
    protected List<String> mFileNames;
    protected List<String> mFileMediaTypes;

    protected List<String> mContents;
    protected List<String> mContentMediaTypes;
    protected List<String> mContentNames;

    protected long mIntervalOffset;
    protected File mIntervalFile;
    protected String mCookies;

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
        initFile();
        if (file != null) {
            mFiles.add(file);
            mFileNames.add("file");
            mFileMediaTypes.add("");
        }
        return this;
    }


    @Override
    public Builder addFile(String name, File file) {
        initFile();
        if (file != null) {
            mFiles.add(file);
            mFileMediaTypes.add("");
            mFileNames.add(name);
        }
        return this;
    }

    @Override
    public Builder addFile(String name, String mediaType, File file) {
        initFile();
        if (file != null) {
            mFiles.add(file);
            mFileMediaTypes.add(mediaType);
            mFileNames.add(name);
        }
        return this;
    }

    private void initFile() {
        if (mFiles == null) {
            mFiles = new ArrayList<>();
        }
        if (mFileNames == null) {
            mFileNames = new ArrayList<>();
        }
        if (mFileMediaTypes == null) {
            mFileMediaTypes = new ArrayList<>();
        }
    }


    @Override
    public Builder addInterva(File file, long offset) {
        mIntervalFile = file;
        mIntervalOffset = offset;
        return null;
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

    @Override
    public Builder addCookie(Cookie cookie) {
        if (cookie != null) {
            StringBuilder cookieHeader = new StringBuilder();
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
            mCookies = cookieHeader.toString();
        }
        return this;
    }

    @Override
    public Builder addCookie(List<Cookie> cookies) {
        if (cookies == null || cookies.size() == 0) return this;
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        mCookies = cookieHeader.toString();
        return this;
    }

    protected Headers getHeader() {
        boolean head = !TextUtils.isEmpty(mHeaderSb) && mHeaderSb.length() > 0;
        boolean cookie = !TextUtils.isEmpty(mCookies) && mCookies.length() > 0;
        Headers.Builder builder = null;
        if (head || cookie) {
            builder = new Headers.Builder();
        }
        if (builder == null) {
            return null;
        } else {
            if (head) {
                String[] key_value = mHeaderSb.toString().split("[$]");
                if (key_value == null || key_value.length == 0) {

                } else {
                    for (String value : key_value) {
                        String[] s = value.split("#");
                        if (s != null && s.length == 2) {
                            builder.add(s[0], s[1]);
                        }
                    }
                }
            }
            if (cookie) {
                builder.add("Cookie", mCookies);
            }
            return builder.build();
        }

    }


    /**
     * 过滤参数，把可用参数和全局参数合并，并剔除重复参数
     *
     * @return
     */
    private StringBuilder mergeParam(StringBuilder requestParams) {
        HashMap<String, String> hashMap = ItgNetSend.itg().itgSet().getLocalParam();
        if (hashMap.size() > 0) {
            StringBuilder localBuild = new StringBuilder();
            String params = requestParams.toString();
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                String str = entry.getKey() + "#" + entry.getValue();
                if (!params.contains(str)) {
                    localBuild.append(str).append("$");
                }
            }
            return localBuild.append(requestParams);
        } else {
            return requestParams;
        }

    }

    protected String getParam() {
        StringBuilder urlParam = mergeParam(mParamSb);
        if (!TextUtils.isEmpty(urlParam) && urlParam.length() > 0) {
            Uri.Builder urlBuild = Uri.parse(mUrl).buildUpon();
            String[] key_value = urlParam.toString().split("[$]");
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
        StringBuilder formParams = mergeParam(mParamSb);
        if (mIntervalFile != null) {
            return getIntervalBody();
        } else {
            if (mContents != null && mContents.size() == 1 && mFiles == null && TextUtils.isEmpty(formParams)) {
                return getUpdateStringRequestBody(mContentMediaTypes.get(0), mContents.get(0));
            } else if (mContents == null && mFiles != null && mFiles.size() == 1 && TextUtils.isEmpty(formParams)) {
                return getUpdateFileRequestBody(mFiles.get(0));
            } else if (!TextUtils.isEmpty(formParams) && mContents == null && mFiles == null) {
                return getFormBody(formParams);
            } else {
                return getMultipartBody(formParams);
            }
        }
    }


    public MediaType getFileType(String fileName) {
        if (fileName.endsWith(".png")) {
            return MediaType.parse("image/png");
        } else if (fileName.endsWith(".jpg")) {
            return MediaType.parse("image/jpeg");
        } else {
            return null;
        }
    }

    private IntervalBody getIntervalBody() {
        IntervalBody.Builder builder = new IntervalBody.Builder();
        builder.addFile(mIntervalFile);
        builder.addFileOffset(mIntervalOffset);
        return builder.build();
    }


    private FormBody getFormBody(StringBuilder formParams) {
        FormBody.Builder builder = new FormBody.Builder();
        if (!TextUtils.isEmpty(formParams) && formParams.length() > 0) {
            String[] key_value = formParams.toString().split("[$]");
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

    private MultipartBody getMultipartBody(StringBuilder formParams) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        boolean hasVaule = false;
        if (!TextUtils.isEmpty(formParams) && formParams.length() > 0) {
            String[] key_value = formParams.toString().split("[$]");
            if (key_value == null || key_value.length == 0) return builder.build();
            for (String value : key_value) {
                String[] s = value.split("#");
                if (s != null && s.length == 2) {
                    builder.addFormDataPart(s[0], s[1]);
                }
                hasVaule = true;
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
            hasVaule = true;
        }

        if (mFiles != null) {
            int count = 0;
            for (File file : mFiles) {
                RequestBody fileBody = RequestBody.create(TextUtils.isEmpty(mFileMediaTypes.get(count)) ? getFileType(file.getName()) : MediaType.parse(mFileMediaTypes.get(count)), file);
                builder.addFormDataPart(mFileNames.get(count), file.getName(), fileBody);
                count++;
            }
            hasVaule = true;
        }

        if (!hasVaule) {
            builder.addFormDataPart("body", "not appropriate body");
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
