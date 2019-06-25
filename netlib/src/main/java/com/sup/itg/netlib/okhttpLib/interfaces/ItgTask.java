package com.sup.itg.netlib.okhttpLib.interfaces;

public interface ItgTask {
    boolean append();

    int getProgress();

    float getDownloadSize();

    long getContentLength();

    void cancel(String cancel);

    String url();

}
