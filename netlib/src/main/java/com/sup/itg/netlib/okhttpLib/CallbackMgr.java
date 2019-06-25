package com.sup.itg.netlib.okhttpLib;

import com.sup.itg.netlib.okhttpLib.interfaces.ItgProgressback;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgTask;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackMgr {

    private Object mLock = new Object();
    private Map<String, ItgProgressback> itgProgressbackMap;

    public CallbackMgr() {

    }

    public void addItgProgress(String url, ItgProgressback progressback) {
        if (itgProgressbackMap == null) {
            itgProgressbackMap = new ConcurrentHashMap<>();
        }
        itgProgressbackMap.put(url, progressback);
    }


    public void removeItgProgress(String url) {
        if (itgProgressbackMap != null) {
            itgProgressbackMap.remove(url);
        }
    }

    public void loop(ItgTask task) {
        if (itgProgressbackMap != null) {
            ItgProgressback itgProgressback = itgProgressbackMap.get(task.url());
            if (itgProgressback != null) {
                itgProgressback.itgProgress(task);
            }
        }
    }

    public void loopFail(String msg, String url) {
        if (itgProgressbackMap != null) {
            ItgProgressback itgProgressback = itgProgressbackMap.get(url);
            if (itgProgressback != null) {
                itgProgressback.fail(msg, url);
            }
        }
    }

}
