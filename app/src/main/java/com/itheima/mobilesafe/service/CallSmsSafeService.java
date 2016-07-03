package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.itheima.mobilesafe.db.Dao.BlackNumberDao;

/**
 * Created by zyp on 2016/7/3.
 */
public class CallSmsSafeService extends Service{
    public static final String TAG = "CallSmsSafeService";
    private TelephonyManager tm;
    private MyPhoneStateListener listener;

    private BlackNumberDao dao;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册一个电话状态的监听器
        tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
        dao = new BlackNumberDao(this);
        Log.i(TAG,"onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(listener,PhoneStateListener.LISTEN_NONE);
        listener = null;
    }

    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE://空闲状态

                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    //1.电话拦截2.短信拦截3.全部拦截
                    String mode = dao.find(incomingNumber);
                    if(!TextUtils.isEmpty(mode)){
                        Log.i(TAG,"挂断电话");
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接通电话状态

                    break;
            }
        }
    }
}
