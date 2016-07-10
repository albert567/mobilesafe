package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.itheima.mobilesafe.R;

/**
 * Created by zyp on 2016/7/10.
 */
public class RocketService extends Service{
    private WindowManager wm;
    private ImageView iv;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        iv = new ImageView(this);
        iv.setBackgroundResource(R.drawable.rocket);
        AnimationDrawable rocketAnimation = (AnimationDrawable)iv.getBackground();
        rocketAnimation.start();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        wm.addView(iv,params);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(iv);
        iv = null;
    }
}
