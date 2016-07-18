package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.SmsTools;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonToolsActivity extends Activity {
    private static final String TAG = "CommonToolsActivity";
    protected static final int SUCCESS = 1;
    protected static final int ERROR = 2;
    private ProgressDialog pd;

   /* private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    Toast.makeText(CommonToolsActivity.this,"备份成功",Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(CommonToolsActivity.this,"备份失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tools);

    }

    /**
     * 进入程序锁
     * @param view
     */
    public void enterAppLock(View view){
        Intent intent = new Intent(this,AppLockActivity.class);
        startActivity(intent);
    }

    /**
     * 进入号码归属地查询
     * @param view
     */
    public void enterNumberQueryActivity(View view){
        Intent intent = new Intent(this,NumberQueryActivity.class);
        startActivity(intent);
    }

    /**
     * 短信备份
     *  @param view
     * <xml头>
     *     <infos>
     *         <info>
     *             <address></address>
     *             <body></body>
     *             <type></type>
     *             <date></date>
     *         </info>
     *     </infos>
     * </xml头>
     * @param view
     * 问君能有几多愁，恰是修完bug改需求
     */
    public void smsBackup(View view){
        pd = new ProgressDialog(this);
        //指定对话框样式
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();
        new Thread(){
            @Override
            public void run() {
                SmsTools.backUpSms(CommonToolsActivity.this, new SmsTools.BackupCallback() {
                    @Override
                    public void beforeSmsBackup(int max) {
                        pd.setMax(max);
                    }

                    @Override
                    public void onSmsBackup(int progress) {
                        pd.setProgress(progress);
                    }
                });
                pd.dismiss();
            }
        }.start();
    }

    /**
     * 短信还原
     * <xml头>
     *     <infos>
     *         <info>
     *             <address></address>
     *             <body></body>
     *             <type></type>
     *             <date></date>
     *         </info>
     *     </infos>
     * </xml头>
     * @param view
     * 问君能有几多愁，恰是修完bug改需求
     */
    public void smsRestore(View view){

    }

}
