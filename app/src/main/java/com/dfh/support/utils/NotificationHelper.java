package com.dfh.support.utils;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import com.dfh.support.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID="8848_service_id";   //通道渠道id
    public static final String  CHANEL_NAME="8848_service_name"; //通道渠道名称


    @TargetApi(Build.VERSION_CODES.O)
    public static  void  show(Context context){
        LogUtil.printPushLog("SupportApplication::NotificationHelper show");
        NotificationChannel channel = null;
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            LogUtil.printPushLog("SupportApplication::NotificationHelper version>O");
//            //创建 通知通道  channelid和channelname是必须的（自己命名就好）
//            channel = new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
//            channel.enableLights(true);//是否在桌面icon右上角展示小红点
//            channel.setLightColor(Color.GREEN);//小红点颜色
//            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
//        }
        Notification notification;
        //获取Notification实例   获取Notification实例有很多方法处理    在此我只展示通用的方法（虽然这种方式是属于api16以上，但是已经可以了，毕竟16以下的Android机很少了，如果非要全面兼容可以用）
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            LogUtil.printPushLog("SupportApplication::NotificationHelper version>O");
            //向上兼容 用Notification.Builder构造notification对象
            notification = new Notification.Builder(context,CHANNEL_ID)
                    .setContentTitle("通知栏标题")
                    .setContentText("这是消息通过通知栏的内容")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.img_logo)
                    .setColor(Color.parseColor("#FEDA26"))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.img_logo))
                    .setTicker("巴士门")
                    .build();
        }else {
            LogUtil.printPushLog("SupportApplication::NotificationHelper version<O");
            //向下兼容 用NotificationCompat.Builder构造notification对象
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle("通知栏标题")
                    .setContentText("这是消息通过通知栏的内容")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.img_logo)
                    .setColor(Color.parseColor("#FEDA26"))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.img_logo))
                    .setTicker("巴士门")
                    .build();
        }


        //发送通知
        int  notifiId=1;
        //创建一个通知管理器
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            notificationManager.createNotificationChannel(channel);
//        }
        notificationManager.notify(notifiId,notification);
        LogUtil.printPushLog("SupportApplication::NotificationHelper notify");

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        LogUtil.printPushLog("SupportApplication::isNotificationEnabled init");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);//是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN);//小红点颜色
            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
            NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                LogUtil.printPushLog("SupportApplication::isNotificationEnabled IMPORTANCE_NONE flase");
                return false;
            }
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            boolean flag = ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            LogUtil.printPushLog("SupportApplication::isNotificationEnabled flag:"+flag);
            return flag;

        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.printPushLog("SupportApplication::isNotificationEnabled over:false");
        return false;
    }
}