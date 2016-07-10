package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.activities.SettingActivity;
import com.itheima.mobilesafe.db.Dao.AddressDBDao;

/**
 * Created by zyp on 2016/7/8.
 */
public class ShowAddressService extends Service {
    private TelephonyManager tm;
    private MyPhoneListener listener;
    private OutCallReceiver receiver;
    private WindowManager wm;
    private View view;

    private WindowManager.LayoutParams params;
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
            //Toast.makeText(ShowAddressService.this,address,1).show();
            showMyToast(address);
        }
    }
    private int[] bgIcons = {R.drawable.call_locate_white,R.drawable.call_locate_orange,
            R.drawable.call_locate_blue,R.drawable.call_locate_gray,
            R.drawable.call_locate_green,};
    /**
     *显示自定义的吐司
     * @param text
     */
    public void showMyToast(String text){
        //自定义View
        view = View.inflate(this, R.layout.toast_address,null);
        view.setOnTouchListener(new View.OnTouchListener() {
            int startX,startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int)event.getX();
                        startY = (int)event.getY();
                    break;
                    case MotionEvent.ACTION_UP:

                    break;
                    case MotionEvent.ACTION_MOVE:
                        int newX = (int)event.getX();
                        int newY = (int)event.getY();
                        int dx = newX - startX;
                        int dy = newY - startY;
                        params.x += dx;
                        params.y += dy;
                        wm.updateViewLayout(view,params);
                        startX = newX;
                        startY = newY;
                    break;
                }
                return true;
            }
        });
        SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
        int which = sp.getInt("which",0);
        view.setBackgroundResource(bgIcons[which]);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast_address);
        tv.setText(text);
        params = new WindowManager.LayoutParams();
        params.gravity = Gravity.LEFT + Gravity.TOP;
        params.x = 50;//水平方向的距离
        params.y = 50;//垂直方向的距离
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //窗体类型，可以被点击和触摸的窗体
        //params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                //| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.format = PixelFormat.TRANSLUCENT;
        wm.addView(view,params);
    }
}
