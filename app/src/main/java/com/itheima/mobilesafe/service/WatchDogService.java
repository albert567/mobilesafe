package com.itheima.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.itheima.mobilesafe.activities.EnterPasswordActivity;
import com.itheima.mobilesafe.db.Dao.AddressDBDao;
import com.itheima.mobilesafe.db.Dao.ApplockDao;

import java.util.List;

/**
 * Created by zyp on 2016/7/22.
 */
public class WatchDogService extends Service{
    private static final String TAG = "WatchDogService";
    private ActivityManager am;
    private boolean flag = true;
    private ApplockDao applockDao;
    private InnerReceiver receiver;
    private String tempStopProtectPackname;
    private List<String> lockPackNames;
    private MyObserver mObserver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applockDao = new ApplockDao(this);
        //获取数据库锁定的全部包名
        lockPackNames = applockDao.findAll();
        mObserver = new MyObserver(new Handler());
        Uri uri= Uri.parse("content://com.itheima.mobilesafe.applockdb");
        getContentResolver().registerContentObserver(uri,true,mObserver);
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        receiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.itheima.mobilesafe.tempstopprotect");
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver,filter);
        startWatchDog();
    }

    private void startWatchDog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                flag = true;
                List<ActivityManager.RunningTaskInfo> infos;
                String packname;
                Intent intent = new Intent(getApplicationContext(),EnterPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                while(flag){
                    infos = am.getRunningTasks(1);
                    packname = infos.get(0).topActivity.getPackageName();
                    System.out.println("正在运行：" + packname);
                   //if(applockDao.find(packname)){//修改为查询内存
                    if(lockPackNames.contains(packname)){
                        if(packname.equals(tempStopProtectPackname)){
                            SystemClock.sleep(30);
                            continue;
                        }
                        intent.putExtra("packname",packname);
                        startActivity(intent);
                    }
                    SystemClock.sleep(300);
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag=false;
        unregisterReceiver(receiver);
        getContentResolver().unregisterContentObserver(mObserver);
        mObserver = null;
    }

    private class InnerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("com.itheima.mobilesafe.tempstopprotect".equals(action)){
                tempStopProtectPackname =  intent.getStringExtra("packname");
            }else if(Intent.ACTION_SCREEN_ON.equals(action)){//点亮屏幕
                Log.i(TAG,"屏幕激活，开启看门狗线程");
                if(flag==false){
                    startWatchDog();
                }
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){//锁屏
                Log.i(TAG,"屏幕关闭，关闭看门狗线程");
                flag = false;
            }
        }
    }

    private class  MyObserver extends ContentObserver{
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.i(TAG,"内容观察者发现了数据发生了变化");
            lockPackNames = applockDao.findAll();
            super.onChange(selfChange);
        }
    }
}
