package com.linson.xtools.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/16.
 */
public class PermissionUtils {
    public static void verifyStoragePermissions(Activity activity) {
        List permissionList = new ArrayList();
        int permission = 0;
        permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.INTERNET);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.RECEIVE_SMS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECEIVE_SMS);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    (String[]) permissionList.toArray(new String[permissionList.size()]),
                    100);
        }
    }
}
