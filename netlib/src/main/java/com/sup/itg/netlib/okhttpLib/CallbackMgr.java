package com.sup.itg.netlib.okhttpLib;

import com.sup.itg.netlib.okhttpLib.interfaces.ItgProgressback;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackMgr {

    private Object mLock = new Object();
    private Map<String, ItgProgressback> itgProgressbackMap;

    private List<ItgProgressback> mItgProgressbacks;

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


    public void switchItgProgressback(String oldUrl, String url) {
        if (oldUrl.equals(url)) return;
        if (itgProgressbackMap != null && itgProgressbackMap.containsKey(oldUrl)) {
            ItgProgressback itgProgressback = itgProgressbackMap.get(oldUrl);
            itgProgressbackMap.remove(oldUrl);
            itgProgressbackMap.put(url, itgProgressback);
        }
    }


    public void addItgProgress1(ItgProgressback progressback) {
        if (mItgProgressbacks == null) {
            mItgProgressbacks = new ArrayList<>();
        }
        synchronized (mLock) {
            mItgProgressbacks.add(progressback);
        }
    }

    public void removeItgProgress1(ItgProgressback progressback) {
        synchronized (mLock) {
            if (mItgProgressbacks == null) {
                mItgProgressbacks.remove(progressback);
            }
        }

    }


    public void loop(ItgTask task) {
        if (itgProgressbackMap != null) {
            ItgProgressback itgProgressback = itgProgressbackMap.get(task.url());
            if (itgProgressback != null) {
                itgProgressback.itgProgress(task);
            }
        }

        synchronized (mLock) {
            if (mItgProgressbacks != null) {
                for (ItgProgressback itgProgressback : mItgProgressbacks) {
                    itgProgressback.itgProgress(task);
                }
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

        synchronized (mLock) {
            if (mItgProgressbacks != null) {
                for (ItgProgressback itgProgressback : mItgProgressbacks) {
                    itgProgressback.fail(msg, url);
                }
            }
        }
    }

}
