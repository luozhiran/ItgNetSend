package com.sup.itg.netlib.okhttpLib;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.sup.itg.netlib.okhttpLib.interfaces.ItgProgressback;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgTask;

import java.util.HashMap;
import java.util.Objects;

public class Task implements ItgTask {

    private HashMap<String, String> mParam;
    private String tUrl;
    private String tMd5;
    private String tPath;
    private ItgProgressback mItgProgressback;
    private long rContentLength;
    private long rDownloadSize;
    private String rCancelUrl;
    private boolean tAppend;


    public HashMap<String, String> params() {
        return mParam;
    }

    public void param(HashMap<String, String> mParam) {
        this.mParam = mParam;
    }


    public Task url(String url) {
        tUrl = url;
        return this;
    }

    public Task md5(String md5) {
        tMd5 = md5;
        return this;
    }

    public Task path(String path) {
        tPath = path;
        return this;
    }

    public Task itgProgressBack(ItgProgressback itgProgressback) {
        mItgProgressback = itgProgressback;
        return this;
    }

    @Override
    public String url() {
        return tUrl;
    }

    public String md5() {
        return tMd5;
    }

    public String path() {
        return tPath;
    }

    public ItgProgressback itgProgressback() {
        return mItgProgressback;
    }

    void contentLength(long contentLength) {
        rContentLength = contentLength;
    }

    void downloadSize(long downloadSize) {
        rDownloadSize = downloadSize;
    }


    public void append(boolean append) {
        tAppend = append;
    }

    @Override
    public boolean append() {
        return tAppend;
    }

    @Override
    public int getProgress() {
        if (rContentLength == 0) {
            return -1;
        } else {
            return (int) (100l * rDownloadSize / rContentLength);
        }
    }

    @Override
    public float getDownloadSize() {
        return rDownloadSize;
    }

    @Override
    public long getContentLength() {
        return rContentLength;
    }

    @Override
    public void cancel(String cancel) {
        rCancelUrl = cancel;
    }

    public String cancel() {
        return rCancelUrl;
    }

}
