package com.dfh.support.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {
    public static void shortToast(Context context, String text){
        Toast.makeText(context,text, Toast.LENGTH_LONG).show();
    }
    public static void shortToast(Context context, int textID){
        Toast.makeText(context,textID, Toast.LENGTH_LONG).show();
    }
    public static void longToast(Context context, String text){
        Toast.makeText(context,text, Toast.LENGTH_LONG).show();
    }
}
