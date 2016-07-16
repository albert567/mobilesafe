package com.itheima.mobilesafe.service;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.receiver.MyWidget;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zyp on 2016/7/16.
 */
public class UpdateWidgetService extends Service {
    private Timer timer;
    private TimerTask task;
    private AppWidgetManager awm;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //得到桌面小控件的管理器
        awm = AppWidgetManager.getInstance(this);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                //由于更新的桌面进程里面的view对象，所以需要把手机安全卫士进程里面的view传递给桌面
                //要求对象是一种特殊的类型，RemoteViews
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
                views.setTextViewText(R.id.process_count,"正在运行的软件：" + getRunningProcessCount());
                views.setTextViewText(R.id.process_memory,"剩余内存控件：" + Formatter.formatFileSize(getApplicationContext(),getAvailMemory()));
                Intent intent = new Intent("com.itheima.mobilesafe.killall");
                //延期的意图，是让另外一个应用程序帮我们执行的意图
                //自定义的广播消息
                PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateWidgetService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                //给按钮注册一个点击事件，事件是一个延期意图
                views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);
                ComponentName provider  = new ComponentName(getApplicationContext(), MyWidget.class);
                awm.updateAppWidget(provider,views);
            }
        };
        timer.schedule(task,0,3000);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取正在运行的进程的数量
     * @return
     */
    private int getRunningProcessCount(){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        return am.getRunningAppProcesses().size();
    }
    /**
     * 获取手机可用的内存空间
     * @return
     */
    private long getAvailMemory(){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);//获取系统当前的内存信息
        return memoryInfo.availMem;
    }
}
