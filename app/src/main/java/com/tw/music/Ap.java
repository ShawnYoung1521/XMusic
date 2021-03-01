package com.tw.music;

import android.app.Application;
import cn.xy.library.XApp;

public class Ap extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        XApp.init(this);
    }
}
