package com.itheima.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by zyp on 2016/7/16.
 */
public class KillAllReceiver extends BroadcastReceiver{
    private static final String TAG = "KillAllReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"自定义广播消息收到");
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info:infos){
            am.killBackgroundProcesses(info.processName);
        }
        Toast.makeText(context,"进程清理完毕",Toast.LENGTH_SHORT).show();
    }
}
