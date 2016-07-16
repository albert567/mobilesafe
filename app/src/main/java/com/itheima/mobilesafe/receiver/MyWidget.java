package com.itheima.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.itheima.mobilesafe.service.UpdateWidgetService;

/**
 * Created by zyp on 2016/7/16.
 */
public class MyWidget extends AppWidgetProvider{
    @Override
    public void onEnabled(Context context) {
        Intent i = new Intent(context, UpdateWidgetService.class);
        context.startService(i);
        super.onEnabled(context);
        System.out.println("widget被创建了");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        System.out.println("widget被销毁了");
        Intent i = new Intent(context, UpdateWidgetService.class);
        context.stopService(i);
    }
}
