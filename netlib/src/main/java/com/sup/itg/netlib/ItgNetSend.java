package com.sup.itg.netlib;

import com.sup.itg.netlib.okhttpLib.CallbackMgr;
import com.sup.itg.netlib.okhttpLib.interfaces.Builder;
import com.sup.itg.netlib.okhttpLib.ItgDownload;
import com.sup.itg.netlib.okhttpLib.ItgSet;
import com.sup.itg.netlib.okhttpLib.OkhttpMgr;

public class ItgNetSend {
    public static final int GET = 1;
    public static final int POST = 2;

    private static volatile ItgNetSend itgNetSend;
    private OkhttpMgr mOkhttpMgr;
    private ItgSet mItgSet;
    private ItgDownload mItgDownload;
    private CallbackMgr mCallbackMgr;


    public static ItgNetSend itg() {
        if (itgNetSend == null) {
            synchronized (ItgNetSend.class) {
                if (itgNetSend == null) {
                    itgNetSend = new ItgNetSend();
                }
            }
        }
        return itgNetSend;
    }


    private ItgNetSend() {
        mOkhttpMgr = new OkhttpMgr();
        mItgSet = new ItgSet();
        mItgDownload = new ItgDownload();
        mCallbackMgr = new CallbackMgr();
    }


    public ItgSet itgSet() {
        return mItgSet;
    }

    public CallbackMgr callbackMgr() {
        return mCallbackMgr;
    }

    public ItgDownload itgDownlad() {
        mItgDownload.initTask();
        return mItgDownload;
    }


    public Builder builder(int type) {
        return RequestFactory.create(type, mOkhttpMgr.mOkHttpClient);
    }

}
