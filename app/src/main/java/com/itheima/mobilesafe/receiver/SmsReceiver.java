package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.itheima.mobilesafe.R;

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
                abortBroadcast();
            }else if("#*alarm*#".equals(body)){
                Log.i(TAG,"播放报警音乐");
                MediaPlayer player = MediaPlayer.create(context,R.raw.awbz);
                player.setLooping(true);
                player.start();
                abortBroadcast();
            }else if("#*wipedata*#".equals(body)){
                Log.i(TAG,"立刻清除数据");
                abortBroadcast();
            }else if("#*lockscreen*#".equals(body)){
                Log.i(TAG,"立刻锁屏");
                abortBroadcast();
            }
        }
    }
}
