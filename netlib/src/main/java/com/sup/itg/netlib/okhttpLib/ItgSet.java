package com.sup.itg.netlib.okhttpLib;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.sup.itg.netlib.ItgNetSend;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ItgSet {
    protected Context mContext;
    protected String mItgUrl;
    protected String mLogPath;
    protected int MAX_DOWNLOAD_NUM = 3;
    protected SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String mPkgName;
    private HashMap<String, String> mLocalParams = new HashMap<>();

    private Handler mHandler;


    public String getItgUrl() {
        return mItgUrl;
    }

    public String getHttpLog() {
        if (TextUtils.isEmpty(mLogPath)) {
            File file = Environment.getExternalStorageDirectory();
            file = new File(file, "/itg/" + mPkgName + "/httpLog.txt");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
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

    public String getDebugLog() {
        File file = Environment.getExternalStorageDirectory();
        file = new File(file, "/itg/" + mPkgName + "/debug.txt");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
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

    public String today() {
        String today = mDateFormat.format(new Date());
        return today;
    }

    public ItgSet app(Context context) {
        mContext = context;
        mPkgName = context.getPackageName();
        return this;
    }

    public ItgSet url(String url) {
        mItgUrl = url;
        return this;
    }

    @Deprecated
    public ItgSet log(String path) {
        mLogPath = path;
        return this;
    }

    public ItgSet downloadQueueMaxNum(int num) {
        MAX_DOWNLOAD_NUM = num;
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

    public ItgSet addLocalParam(String key, String value) {
        if (mLocalParams.containsKey(key)) {
            mLocalParams.remove(key);
        }
        mLocalParams.put(key, value);
        return this;
    }

    public HashMap<String, String> getLocalParam() {
        return mLocalParams;
    }
}
