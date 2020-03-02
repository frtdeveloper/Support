package com.dfh.support.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;


public class PermissionUtil {
    private static PermissionUtil permissionUtil=null;
    private static List<String> mListPermissions;
    private PermissionUtil(){
        mListPermissions=new ArrayList<String>();
        addAllPermissions(mListPermissions);
    }
    private void addAllPermissions(List<String> mListPermissions) {
//        mListPermissions.add(android.Manifest.permission.GET_ACCOUNTS);
//        mListPermissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        mListPermissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
//        mListPermissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//        mListPermissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
//        mListPermissions.add(android.Manifest.permission.READ_PHONE_STATE);
//        mListPermissions.add(android.Manifest.permission.CAMERA);
    }
    public static PermissionUtil getInstance() {
        if (permissionUtil == null) {
            permissionUtil = new PermissionUtil();
        }
        return permissionUtil;
    }
    public boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public List<String> findDeniedPermissions(Activity activity, List<String> permissions){
        List<String> denyPermissions = new ArrayList<>();
        for(String value : permissions){
            if(activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED){
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }
    @TargetApi(value = Build.VERSION_CODES.M)
    public boolean requestPermissions(Object object, int requestCode){
        if(!isOverMarshmallow()) {
            return false;
        }
        List<String> deniedPermissions =null;
        if(object instanceof Activity) {
            deniedPermissions = findDeniedPermissions((Activity)object, mListPermissions);
        }else if(object instanceof Fragment){
            deniedPermissions = findDeniedPermissions(((Fragment)object).getActivity(), mListPermissions);
        }
        if(deniedPermissions!=null) {
            if (deniedPermissions.size() > 0) {
                if (object instanceof Activity) {
                    ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
                } else if (object instanceof Fragment) {
                    ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
                } else {
                    throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
                }
                return true;
            }
        }
        return false;
    }
    public void requestResult(Object obj, int requestCode, String[] permissions, int[] grantResults, PermissionCallBack permissionCallBack){
        List<String> deniedPermissions = new ArrayList<>();
        for(int i=0; i<grantResults.length; i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                deniedPermissions.add(permissions[i]);
            }
        }
        if(deniedPermissions.size() > 0){
            permissionCallBack.onPermissionGetFail();
        } else {
            permissionCallBack.onPermissionGetSuccess();
        }
    }
    public interface PermissionCallBack{
        void onPermissionGetSuccess();
        void onPermissionGetFail();
    }
}

