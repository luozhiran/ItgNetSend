package com.sup.itg.netlib.okhttpLib;

import android.util.Log;

import com.sup.itg.netlib.ItgNetSend;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ItgLog {
    private static String className = "";
    private static String methodName = "";
    private static int lineNumber;
    private static boolean PRINT_LOG = true;

    public static void openLog() {
        PRINT_LOG = true;
    }

    public static void closeLog() {
        PRINT_LOG = false;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (PRINT_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(className, createLog(message));
        }
    }


    public static void i(String message) {
        if (PRINT_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(className, createLog(message));
        }
    }

    public static void d(String message) {
        if (PRINT_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(message));
        }
    }

    public static void v(String message) {
        if (PRINT_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.v(className, createLog(message));
        }
    }

    public static void w(String message) {
        if (PRINT_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(className, createLog(message));
        }
    }

    /**
     * 写http日志
     *
     * @param message
     */
    protected static void httpWtf(String message) {
        if (PRINT_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.wtf(className, createLog(message));
            StringBuilder sb = new StringBuilder();
            sb.append(ItgNetSend.itg().itgSet().today()).append("：").append(lineNumber).append("：").append(message).append("\r\n");
            write(sb.toString(), ItgNetSend.itg().itgSet().getHttpLog());
        }
    }

    public static void wtf(String message) {
        if (PRINT_LOG) {
            StringBuilder sb = new StringBuilder();
            sb.append(ItgNetSend.itg().itgSet().today()).append("：").append(message).append("\r\n");
            write(sb.toString(), ItgNetSend.itg().itgSet().getDebugLog());
        }
    }


    private static void write(String log, String path) {
        OutputStreamWriter write = null;
        try {
            write = new OutputStreamWriter(new FileOutputStream(path, true), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(log);
            writer.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            if (write != null) {
                try {
                    write.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }

    }

}
