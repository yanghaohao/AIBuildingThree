package com.zgw.company.base.core.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionRequest {

    //相机权限
    private String camera = Manifest.permission.CAMERA;
    //写入权限
    private String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //本地位置权限
    private String fine_location= Manifest.permission.ACCESS_FINE_LOCATION;

    //权限允许
    private int access = PackageManager.PERMISSION_GRANTED;
    //权限拒绝
    private int denied = PackageManager.PERMISSION_DENIED;

    public int requestPermission(Context context, Activity activity,String...permissions){
        int i = 0;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != access){
                i++;
            }
        }

        if (i == 0){
            return 1;
        }else {
            ActivityCompat.requestPermissions(activity, permissions, 1);
        }
        return -1;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getWrite_permission() {
        return write_permission;
    }

    public void setWrite_permission(String write_permission) {
        this.write_permission = write_permission;
    }

    public String getFine_location() {
        return fine_location;
    }

    public void setFine_location(String fine_location) {
        this.fine_location = fine_location;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getDenied() {
        return denied;
    }

    public void setDenied(int denied) {
        this.denied = denied;
    }
}
