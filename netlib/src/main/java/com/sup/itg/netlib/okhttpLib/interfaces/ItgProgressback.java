package com.sup.itg.netlib.okhttpLib.interfaces;

public interface ItgProgressback {
    void itgProgress(ItgTask task);

    void fail(String error,String url);
}
