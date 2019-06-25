package com.sup.itg.netlib;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ItgLog {
    private static String className = "";
    private static String methodName = "";
    private static int lineNumber;
    private static boolean PRINT_LOG = true;

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

    public static void wtf(String message) {
        if (PRINT_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.wtf(className, createLog(message));
            write(lineNumber + " ---> " + message + "\r\n");
        }
    }


    private static void write(String log) {
        String path = ItgNetSend.itg().itgSet().getLog();
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(path, true), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(log);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
