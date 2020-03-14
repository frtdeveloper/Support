package com.dfh.support.utils;


import androidx.annotation.Nullable;

public class TextUtils {
    public static boolean isEmpty(@Nullable String str) {
        return (android.text.TextUtils.isEmpty(str) || "null".equalsIgnoreCase(str));
    }
}
