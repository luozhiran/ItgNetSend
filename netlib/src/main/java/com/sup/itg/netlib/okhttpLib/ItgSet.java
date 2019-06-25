package com.sup.itg.netlib.okhttpLib;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.sup.itg.netlib.ItgNetSend;

import java.io.File;
import java.io.IOException;

public class ItgSet {
    protected Context mContext;
    protected String mItgUrl;
    protected String mLogPath;
    private Handler mHandler;


    public String getItgUrl() {
        return mItgUrl;
    }

    public String getLog() {
        if (TextUtils.isEmpty(mLogPath)) {
            String path = mContext.getExternalFilesDir("log").getAbsolutePath();
            path = path + "/log.txt";
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (file.length() > 1024 * 1024 * 5) {
                    file.delete();
                }
            }
            return file.getAbsolutePath();
        } else {
            File file = new File(mLogPath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (file.length() > 1024 * 1024 * 5) {
                    file.delete();
                }
            }
            return file.getAbsolutePath();
        }
    }

    public ItgSet app(Context context) {
        mContext = context;
        return this;
    }

    public ItgSet url(String url) {
        mItgUrl = url;
        return this;
    }

    public ItgSet log(String path) {
        mLogPath = path;
        return this;
    }

    protected Handler itgHandler() {
        if (mHandler == null) {
            synchronized (ItgSet.class) {
                if (mHandler == null) {
                    mHandler = new Handler(mContext.getMainLooper());
                }
            }
        }
        return mHandler;
    }
}
