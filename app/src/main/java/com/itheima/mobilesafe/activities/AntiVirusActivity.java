package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AntiVirusActivity extends Activity {

    private static final int FOUND_VIRUS = 1;
    private static final int NOT_VIRUS = 2;
    private static final int SCAN_FINISH = 3;
    private ImageView iv_scan;
    private ProgressBar pb_scan_status;
    private LinearLayout ll_container;
    private TextView tv_scan_status;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            PackageInfo info;
            switch (msg.what){
                case FOUND_VIRUS:
                    info = (PackageInfo) msg.obj;
                    TextView tv = new TextView(getApplicationContext());
                    tv.setTextColor(Color.RED);
                    tv.setText("发现病毒："+info.applicationInfo.loadLabel(getPackageManager()));
                    ll_container.addView(tv,0);
                    break;
                case NOT_VIRUS:
                    info = (PackageInfo) msg.obj;
                    TextView tv2 = new TextView(getApplicationContext());
                    tv2.setTextColor(Color.BLACK);
                    tv2.setText("扫描安全："+info.applicationInfo.loadLabel(getPackageManager()));
                    ll_container.addView(tv2,0);
                    break;
                case SCAN_FINISH:
                    iv_scan.clearAnimation();
                    iv_scan.setVisibility(View.INVISIBLE);
                    tv_scan_status.setText("扫描完毕");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        pb_scan_status = (ProgressBar) findViewById(R.id.pb_scan_status);
        iv_scan = (ImageView) findViewById(R.id.iv_scan);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
        RotateAnimation ra = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(2000);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setRepeatMode(Animation.RESTART);
        iv_scan.setAnimation(ra);

        scanVirus();
    }

    /**
     * 查杀病毒
     */
    private void scanVirus() {
        new Thread(){
            @Override
            public void run() {
                //遍历系统里面的每一个应用程序apk，获取它的特征码
                PackageManager pm = getPackageManager();
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                pb_scan_status.setMax(infos.size());
                int progress = 0;
                for (PackageInfo info : infos){
                    String path = info.applicationInfo.sourceDir;
                    String md5 = getFileMd5(path);
                    //查询数据库里面是不是有这个记录，如果有就是病毒
                    SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath()+"/antivirus.db",null,SQLiteDatabase.OPEN_READONLY);
                    Cursor cursor = db.rawQuery("select desc from datable where md5=?",new String[]{md5});
                    if(cursor.moveToNext()){
                        String desc = cursor.getString(0);
                        System.out.println("发现病毒："+desc+"--"+info.applicationInfo.loadLabel(pm));
                        Message msg = handler.obtainMessage();
                        msg.what = FOUND_VIRUS;
                        msg.obj = info;
                        handler.sendMessage(msg);
                    }else{
                        System.out.println("扫描安全："+info.applicationInfo.loadLabel(pm));
                        Message msg = handler.obtainMessage();
                        msg.what = NOT_VIRUS;
                        msg.obj = info;
                        handler.sendMessage(msg);
                    }
                    progress++;
                    SystemClock.sleep(50);
                    pb_scan_status.setProgress(progress);
                }
                Message msg = handler.obtainMessage();
                msg.what = SCAN_FINISH;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private String getFileMd5(String path){
        try {
            File file = new File(path);
            //得到一个数字摘要器
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = fis.read(buffer))!=-1){
                digest.update(buffer,0,len);
            }
            byte[] result = digest.digest();
            StringBuilder sb = new StringBuilder();
            for(byte b:result){
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if(str.length()==1){
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

}
