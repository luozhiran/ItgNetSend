package com.sup.itg.netlib.okhttpLib;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.sup.itg.netlib.ItgLog;
import com.sup.itg.netlib.ItgNetSend;
import com.sup.itg.netlib.okhttpLib.interfaces.Builder;
import com.sup.itg.netlib.okhttpLib.interfaces.Dispatch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DispatchTool implements Dispatch {

    private List<Task> mParamTasks = new ArrayList<>();
    private List<String> mParamTasksUrl = new ArrayList<>();

    private List<Task> mRunningTasks = new ArrayList<>();
    private List<String> mRunningTasksUrl = new ArrayList<>();

    private Object mLock = new Object();
    private final int MAX_DOWNLOAD_NUM = 3;
    private volatile Looper mItgLooper;
    private volatile ItgHandler mItgHandler;

    private final class ItgHandler extends Handler {
        public ItgHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Task task = null;
            synchronized (mLock) {
                if (mParamTasks.size() > 0 && mRunningTasks.size() <= MAX_DOWNLOAD_NUM) {
                    task = mParamTasks.remove(mParamTasks.size() - 1);
                    mParamTasksUrl.remove(mParamTasks.size() - 1);
                    if (task != null) {
                        if (task.append()) {
                            appendDownload(task);
                        } else {
                            download(task);
                        }
                    }
                }
            }
        }
    }


    public DispatchTool() {
        HandlerThread thread = new HandlerThread("itg");
        thread.start();
        mItgLooper = thread.getLooper();
        mItgHandler = new ItgHandler(mItgLooper);
    }

    private boolean isDownload(Task task) {
        synchronized (mLock) {
            if (task.url().equals(task.cancel())) {
                mItgHandler.sendEmptyMessage(1);
                return false;
            }
            if (!mParamTasksUrl.contains(task.url())) {
                if (!mRunningTasksUrl.contains(task.url())) {
                    if (mRunningTasks.size() <= MAX_DOWNLOAD_NUM) {
                        mRunningTasks.add(task);
                        mRunningTasksUrl.add(task.url());
                        return true;
                    } else {
                        mParamTasksUrl.add(task.url());
                        mParamTasks.add(task);
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if (!mRunningTasks.contains(task)) {
                    if (mRunningTasks.size() <= MAX_DOWNLOAD_NUM) {
                        mParamTasksUrl.remove(task.url());
                        mParamTasks.remove(task);
                        mRunningTasksUrl.add(task.url());
                        mRunningTasks.add(task);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    mParamTasksUrl.remove(task.url());
                    mParamTasks.remove(task);
                    return false;
                }
            }
        }
    }


    @Override
    public void download(Task task) {
        if (isDownload(task)) {
            sendDownloadRequest(task, null);
        } else {
            if (mParamTasks.size() > 0) {
                mItgHandler.sendEmptyMessage(1);
            }
        }
    }


    private void sendDownloadRequest(final Task task, String header) {
        getBuilder(task, header).send(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                synchronized (mLock) {
                    mRunningTasks.remove(task);
                    mRunningTasksUrl.remove(task.url());
                }
                mItgHandler.sendEmptyMessage(1);
                ItgLog.wtf(e.getMessage() + " " + task.url());
                task.itgProgressback().fail(e.getMessage(),task.url());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (task.append()) {
                    if (response.code() == 206) {
                        response(response, task);
                    } else {
                        synchronized (mLock) {
                            mRunningTasksUrl.remove(task.url());
                            mRunningTasks.remove(task);
                        }
                        mItgHandler.sendEmptyMessage(1);
                        task.itgProgressback().fail("append can not is true",task.url());
                        ItgLog.wtf("append can not is true" + "  " + task.url());
                    }
                } else {
                    if (response.code() == 200) {
                        response(response, task);
                    } else {
                        synchronized (mLock) {
                            mRunningTasksUrl.remove(task.url());
                            mRunningTasks.remove(task);
                        }
                        mItgHandler.sendEmptyMessage(1);
                        task.itgProgressback().fail("response.code() = " + response.code(),task.url());
                        ItgLog.wtf("response.code() = " + response.code() + "  " + task.url());
                    }
                }
            }
        });


    }

    private void response(Response response, Task task) throws IOException {
        File file = new File(task.path() + ".tmp");
        if (task.append()) {
            task.contentLength(response.body().contentLength() + file.length());
        } else {
            task.contentLength(response.body().contentLength());
        }
        boolean mkSuccess = true;
        if (!file.getParentFile().exists()) {
            mkSuccess = file.getParentFile().mkdirs();
        } else {
            mkSuccess = true;
        }
        if (!mkSuccess) {
            response.body().close();
            ItgLog.wtf("check path " + task.url());
        } else {
            /*  将网络流中的文件写入本地*/
            streamHander(response.body().byteStream(), file, task);
            response.body().close();
        }

    }

    @Override
    public void appendDownload(final Task task) {
        if (isDownload(task)) {
            getBuilder(task, null).send(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    synchronized (mLock) {
                        mRunningTasks.remove(task);
                        mRunningTasksUrl.remove(task.url());
                    }
                    mItgHandler.sendEmptyMessage(1);
                    ItgLog.wtf(e.getMessage() + " " + task.url());
                    task.itgProgressback().fail(e.getMessage(),task.url());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        File file = new File(task.path() + ".tmp");
                        if (file.exists()) {
                            sendDownloadRequest(task, file.length() + "-" + (response.body().contentLength() - 1));
                        } else {
                            sendDownloadRequest(task, 0 + "-" + (response.body().contentLength() - 1));
                        }
                    } else {
                        synchronized (mLock) {
                            mRunningTasksUrl.remove(task.url());
                            mRunningTasks.remove(task);
                        }
                        task.itgProgressback().fail("response.code() = " + response.code(),task.url());
                        ItgLog.wtf("response.code() = " + response.code() + "  " + task.url());
                        mItgHandler.sendEmptyMessage(1);
                    }
                }
            });
        } else {
            if (mParamTasks.size() > 0) {
                mItgHandler.sendEmptyMessage(1);
            }
        }

    }


    private Builder getBuilder(Task task, String header) {
        Builder builder = ItgNetSend
                .itg()
                .builder(ItgNetSend.GET)
                .url(task.url());
        if (!TextUtils.isEmpty(header)) {
            builder.addHeader("RANGE", "bytes=" + header);
        }
        if (task.params() != null) {
            Set<Map.Entry<String, String>> entries = task.params().entrySet();
            for (Map.Entry<String, String> entry : entries) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }


    private void streamHander(InputStream in, File file, Task task) {
        byte[] buffer = new byte[1024 << 2];
        int length = -1;
        OutputStream out = null;
        try {
            out = new FileOutputStream(file, task.append());
            int pre = 0, cur = 0;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
                task.downloadSize(file.length());
                cur = task.getProgress();
                if (!TextUtils.isEmpty(task.cancel()) && task.url().equals(task.cancel())) {
                    ItgLog.wtf("cancel " + task.url());
                    synchronized (mLock) {
                        mRunningTasksUrl.remove(task.url());
                        mRunningTasks.remove(task);
                        task.itgProgressback().fail("cancel download url",task.url());
                    }
                    return;
                } else {
                    if (cur != pre && task.itgProgressback() != null) {
                        if (cur == 100) {
                            ItgLog.e(cur + "  -->  " + pre + " " + task.getContentLength() + "  " + file.length());
                            file.renameTo(new File(file.getAbsolutePath().replace(".tmp", "")));
                            task.itgProgressback().itgProgress(task);
                            synchronized (mLock) {
                                mRunningTasks.remove(task);
                            }
                            mItgHandler.sendEmptyMessage(0);
                        } else {
                            ItgLog.e(cur + "  " + pre + " " + task.getContentLength() + "  " + file.length());
                            task.itgProgressback().itgProgress(task);
                        }
                    }
                }
                pre = cur;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            synchronized (mLock) {
                mRunningTasksUrl.remove(task.url());
                mRunningTasks.remove(task);
            }
            task.itgProgressback().fail("not found file exception",task.url());
        } catch (IOException e) {
            e.printStackTrace();
            synchronized (mLock) {
                mRunningTasksUrl.remove(task.url());
                mRunningTasks.remove(task);
            }
            task.itgProgressback().fail("io exception",task.url());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void cancel(String url) {
        synchronized (mLock) {
            for (Task task : mRunningTasks) {
                if (task.url().equals(url)) {
                    task.cancel(url);
                    break;
                }
            }
        }
    }

}
