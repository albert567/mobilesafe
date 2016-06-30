package com.itheima.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.LocationService;

/**
 * Created by zyp on 2016/6/26.
 */
public class SmsReceiver extends BroadcastReceiver{
    private static final String TAG = "SmsReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"短信到来了");
        Object[] objs = (Object[])intent.getExtras().get("pdus");
        for(Object obj:objs){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj);
            String body = smsMessage.getMessageBody();
            if("#*location*#".equals(body)){
                Log.i(TAG,"返回手机的位置");
                //开启一个后台的服务,在服务里面获取手机的位置
                Intent i = new Intent(context, LocationService.class);
                context.startService(i);
                abortBroadcast();
            }else if("#*alarm*#".equals(body)){
                Log.i(TAG,"播放报警音乐");
                MediaPlayer player = MediaPlayer.create(context,R.raw.awbz);
                player.setLooping(true);
                player.start();
                abortBroadcast();
            }else if("#*wipedata*#".equals(body)){
                Log.i(TAG,"立刻清除数据");
                DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                //前提必须先开启超级管理员权限
                dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//内存和外存,0只内存
                abortBroadcast();
            }else if("#*lockscreen*#".equals(body)){
                Log.i(TAG,"立刻锁屏");
                DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                dpm.resetPassword("123",0);
                dpm.lockNow();
                abortBroadcast();
            }
        }
    }
}
