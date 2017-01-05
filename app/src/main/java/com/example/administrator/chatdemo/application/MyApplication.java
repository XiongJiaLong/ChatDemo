package com.example.administrator.chatdemo.application;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Administrator on 2017/1/3.
 */

public class MyApplication extends Application {
    private static MyApplication context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        AVOSCloud.initialize(this,"ycRsfhmSIX7m3wwJWDXIwbpr-gzGzoHsz","KpE2acKGWQNp8dMKj1VRqajR");
    }
    public static MyApplication getInstance(){
        return context;
    }
}
