package com.tw.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import com.xy.media_lib.base.MService;
import com.xy.media_lib.presenter.MusicPresenter;
import com.xy.media_lib.view.MusicView;

import cn.xy.library.XApp;


public class MusicService extends MService<MusicPresenter> implements MusicView {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter.onServiceCreate();
    }

    @Override
    public MusicPresenter getPresenter() {
        return new MusicPresenter(this);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        mPresenter.onServiceDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(XApp.getApp());
        Intent nfIntent = new Intent(this, MusicActivity.class);
        builder.setContentIntent(PendingIntent.
                getActivity(this, 0, nfIntent, 0))
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_launcher))
//                .setContentTitle("微音正在后台运行")
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentText("要显示的内容")
                .setWhen(System.currentTimeMillis());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("1", "Name",NotificationManager.IMPORTANCE_MIN);
            notificationChannel.enableLights(false);//如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
            notificationChannel.setShowBadge(false);//是否显示角标
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId("1");
        }
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        // 参数一：唯一的通知标识；参数二：通知消息。
         startForeground(110, notification);// 开始前台服务
        return super.onStartCommand(intent, flags, startId);
    }
}
