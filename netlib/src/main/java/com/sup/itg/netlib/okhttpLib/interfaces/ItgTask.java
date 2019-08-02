package com.sup.itg.netlib.okhttpLib.interfaces;

public interface ItgTask {
    boolean append();

    int getProgress();

    long getDownloadSize();

    long getContentLength();

    void cancel(String cancel);

    String url();

    boolean broadcast();

    String broadcastComponentName();

    String path();

    String md5();

    String customBroadcast();

    String extra();
}
