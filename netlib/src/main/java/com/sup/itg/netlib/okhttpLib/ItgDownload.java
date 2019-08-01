package com.sup.itg.netlib.okhttpLib;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

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

    @SuppressLint("WrongConstant")
    public ItgDownload registerCallback() {
        this.task().itgProgressBack(new ItgProgressback() {
            @Override
            public void connecting(ItgTask task) {
                ItgNetSend.itg().callbackMgr().loopConnecting(task);
            }

            @Override
            public void itgProgress(ItgTask task) {
                ItgNetSend.itg().callbackMgr().loop(task);
                if (task.getProgress() == 100 && task.broadcast()) {
                    Intent intent = new Intent(ItgNetSend.BROAD_ACTION);
                    if (Build.VERSION.SDK_INT >= 26 && !TextUtils.isEmpty(task.broadcastComponentName())) {
                        intent.addFlags(0x01000000);//加上这句话，可以解决在android8.0系统以上2个module之间发送广播接收不到的问题
                        intent.setComponent(new ComponentName(ItgNetSend.itg().itgSet().mContext.getPackageName(), task.broadcastComponentName()));
                    }

                    intent.putExtra("url", task.url());
                    intent.putExtra("file", task.path());
                    ItgNetSend.itg().itgSet().mContext.sendBroadcast(intent);
                }
            }

            @Override
            public void fail(String error, String url) {
                ItgNetSend.itg().callbackMgr().loopFail(error, url);
            }
        });
        return this;
    }

    public ItgDownload broadcast(boolean broad) {
        if (mTask == null) {
            new NullPointerException("invoke initTask");
            return null;
        }
        mTask.broadcast(broad);
        return this;
    }

    public ItgDownload broadcastComponentName(String componentName) {
        if (mTask == null) {
            new NullPointerException("invoke initTask");
            return null;
        }
        mTask.broadcastComponentName(componentName);
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

    public boolean isQueue(String url) {
        return mDispatchTool.isQueue(url);
    }

    public void cancel(String url) {
        mDispatchTool.cancel(url);
    }
}
