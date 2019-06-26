package com.sup.itg.netlib.okhttpLib;

import com.sup.itg.netlib.ItgNetSend;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgProgressback;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgTask;

public class ItgDownload {
    private Task mTask;
    private DispatchTool mDispatchTool;

    public ItgDownload() {
        mDispatchTool = new DispatchTool();
    }

    public ItgDownload initTask() {
        mTask = new Task();
        return this;
    }

    public ItgDownload url(String url) {
        if (mTask == null) {
            new NullPointerException("invoke initTask");
            return null;
        }
        mTask.url(url);
        return this;
    }

    public ItgDownload path(String path) {
        if (mTask == null) {
            new NullPointerException("invoke initTask");
            return null;
        }
        mTask.path(path);
        return this;
    }

    public ItgDownload md5(String md5) {
        if (mTask == null) {
            new NullPointerException("invoke initTask");
            return null;
        }
        mTask.md5(md5);
        return this;
    }

    public ItgDownload append(boolean append) {
        if (mTask == null) {
            new NullPointerException("invoke initTask");
            return null;
        }
        mTask.append(append);
        return this;
    }

    public ItgDownload callback(ItgProgressback progressback) {
        if (mTask == null) {
            new NullPointerException("invoke initTask");
            return null;
        }
        mTask.itgProgressBack(progressback);
        return this;
    }

    public ItgDownload registerCallback() {
        this.task().itgProgressBack(new ItgProgressback() {
            @Override
            public void itgProgress(ItgTask task) {
                ItgNetSend.itg().callbackMgr().loop(task);
            }

            @Override
            public void fail(String error, String url) {
                ItgNetSend.itg().callbackMgr().loopFail(error, url);
            }
        });
        return this;
    }


    public Task task() {
        return mTask;
    }

    public void start(Task task) {
        if (task.append()) {
            mDispatchTool.appendDownload(task);
        } else {
            mDispatchTool.download(task);
        }
    }

    public void cancel(String url) {
        mDispatchTool.cancel(url);
    }
}
