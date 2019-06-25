package com.sup.itg.netlib.okhttpLib.interfaces;

import com.sup.itg.netlib.okhttpLib.Task;

public interface Dispatch {
    void download(Task task);
    void appendDownload(Task task);

}
