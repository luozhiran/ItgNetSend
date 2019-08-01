package com.sup.itg.netlib.okhttpLib.interfaces;

public interface ItgProgressback {
    /**
     * 与服务器建立连接过程中
     *
     * @param task
     */
    void connecting(ItgTask task);

    void itgProgress(ItgTask task);

    void fail(String error, String url);
}
