package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by zyp on 2016/6/26.
 */
public class BootCompleteReceiver extends BroadcastReceiver{
    private static final String TAG = "BootCompleteReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"手机启动完毕了");
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean protecting = sp.getBoolean("protecting",false);
        if(protecting){
            Log.i(TAG,"防盗保护已经开启,检测SIM卡是否一致");
            //用户绑定的sim卡串号
            String savedSim = sp.getString("sim","");
            //获取当前手机里面的sim卡串号
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String currentSim = tm.getSimSerialNumber()+"aa";
            if(savedSim.equals(currentSim)){
                Log.i(TAG,"sim卡一致,还是您的手机");
            }else{
                Log.i(TAG,"sim卡不一致,手机可能被盗,发送报警短信");
                SmsManager smsManager = SmsManager.getDefault();
                String safenumber = sp.getString("safenumber","");
                smsManager.sendTextMessage(safenumber,null,"help!",null,null);
            }
        }
    }
}
