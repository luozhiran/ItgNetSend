package com.sup.itg.downloadapk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sup.itg.netlib.ItgLog;
import com.sup.itg.netlib.ItgNetSend;
import com.sup.itg.netlib.okhttpLib.Task;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgCallback;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgTask;
import com.sup.itg.netlib.okhttpLib.interfaces.ItgProgressback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        checkPermission();
    }

    public void sendRequest(View view) {
        ItgNetSend.itg()
                .builder(ItgNetSend.GET)
                .send(new ItgCallback() {
                    @Override
                    public void onFailure(String er) {
                        Log.e("dd", er);
                    }

                    @Override
                    public void onResponse(String result, int code) {
                        Log.e("dd", result);
                    }

                });

    }

    public void download(View view) {
        Task task = ItgNetSend.itg()
                .itgDownlad()
                .url("http://robot.yuanqutech.com:8030/ver/download?id=205")
                .path(Environment.getExternalStorageDirectory() + "/cd/download.apk")
                .append(true)
                .callback(new ItgProgressback() {
                    @Override
                    public void itgProgress(ItgTask task) {


                    }

                    @Override
                    public void fail(String error, String url) {
                        ItgLog.e(error);
                    }
                }).task();
        ItgNetSend.itg().itgDownlad().start(task);

    }

    public void testOnClock(View view) {


    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionList.clear();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);//添加还未授予的权限
                }
            }
            if (mPermissionList.size() > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                    ActivityCompat.requestPermissions(this, permissions, 12);
                } else {
                    ActivityCompat.requestPermissions(this, permissions, 12);
                }
            }
        }
    }

}
