package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.service.carrier.CarrierMessagingService;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobilesafe.db.Dao.BlackNumberDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zyp on 2016/7/3.
 */
public class CallSmsSafeService extends Service{
    public static final String TAG = "CallSmsSafeService";
    private TelephonyManager tm;
    private MyPhoneStateListener listener;
    private BlackNumberDao dao;
    private InnerSmsReceiver receiver;

    private class InnerSmsReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"服务内部广播接受者接到短信");
            Object[] objs = (Object[])intent.getExtras().get("pdus");
            for(Object obj:objs){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj);
                String body = smsMessage.getMessageBody();
                String sender = smsMessage.getOriginatingAddress();
                Log.i(TAG, sender+body);
                String mode = dao.find(sender);
                if("2".equals(mode)||"3".equals(mode)){
                    Log.i(TAG,"发现黑名单短信，拦截");
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new InnerSmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(receiver,filter);
        //注册一个电话状态的监听器
        tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
        dao = new BlackNumberDao(this);
        Log.i(TAG,"onCreate()");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"骚扰拦截服务已经关闭");
        super.onDestroy();
        unregisterReceiver(receiver);
        receiver = null;
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
                        //从1.5版本后，挂断电话的api被隐藏起来了
                        endCall();//-->调用系统底层的服务方法挂断电话
                        //利用内容提供者清除呼叫记录
                        deleteCallLog(incomingNumber);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接通电话状态

                    break;
            }
        }

        private void deleteCallLog(final String incomingNumber) {
            final ContentResolver resolver = getContentResolver();
            final Uri uri = Uri.parse("content://call_log/calls/");
            //利用内容观察者 观察呼叫记录的数据库，如果生成了呼叫记录就立刻删除呼叫记录
            resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    //当内容观察者观察到数据库的内容变化的时候调用的方法
                    super.onChange(selfChange);
                    resolver.delete(uri,"number=?",new String[]{incomingNumber});
                }
            });

        }

        private void endCall() {
            try {
                Class clazz = getClassLoader().loadClass("android.os.ServiceManager");
                Method method = clazz.getDeclaredMethod("getService",String.class);
                IBinder iBinder = (IBinder)method.invoke(null, Context.TELEPHONY_SERVICE);
                ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                iTelephony.endCall();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
