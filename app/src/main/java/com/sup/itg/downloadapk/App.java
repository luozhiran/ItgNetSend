package com.sup.itg.downloadapk;

import android.app.Application;

import com.sup.itg.netlib.ItgNetSend;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ItgNetSend.itg().itgSet().app(this).url("http://www.baidu.com");
    }
}
