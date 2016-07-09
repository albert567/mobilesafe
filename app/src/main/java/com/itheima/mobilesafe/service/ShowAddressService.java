package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.GpsStatus;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.db.Dao.AddressDBDao;

/**
 * Created by zyp on 2016/7/8.
 */
public class ShowAddressService extends Service {
    private TelephonyManager tm;
    private MyPhoneListener listener;
    private OutCallReceiver receiver;
    private WindowManager wm;
    private TextView view;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        receiver = new OutCallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver,filter);
        //注册一个电话监听的的服务
        tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        tm.listen(listener,PhoneStateListener.LISTEN_NONE);
        listener = null;
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }

    private class MyPhoneListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = AddressDBDao.findLocation(incomingNumber);
                    //Toast.makeText(ShowAddressService.this,address,1).show();
                    showMyToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //把窗体上的view移除
                    if(view!=null){
                        wm.removeView(view);
                        view = null;
                    }

            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private class OutCallReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            String address = AddressDBDao.findLocation(number);
            Toast.makeText(ShowAddressService.this,address,1).show();
            showMyToast(address);
        }
    }

    /**
     *显示自定义的吐司
     * @param text
     */
    public void showMyToast(String text){
        view = new TextView(this);
        view.setText(text);
        view.setTextColor(Color.RED);
        view.setTextSize(20);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.format = PixelFormat.TRANSLUCENT;
        wm.addView(view,params);
    }
}
