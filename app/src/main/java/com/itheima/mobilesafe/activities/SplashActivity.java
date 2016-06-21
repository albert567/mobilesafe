package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.PackageInfoUtils;
import com.itheima.mobilesafe.utils.StreamTools;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.os.SystemClock.*;


public class SplashActivity extends Activity {
    public static final String TAG = "SplashActivity";
    public static final int SHOW_UPDATE_DIALOG = 1;
    public static final int ERROR = 2;
    private String downloadpath;
    private TextView tv_splash_version;
    private ProgressDialog progressDialog;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_UPDATE_DIALOG://显示应用更新对话框
                    String desc = (String)msg.obj;
                    showUpdateDialog(desc);
                    break;
                case ERROR:
                    Toast.makeText(SplashActivity.this, "错误码:"+msg.obj, Toast.LENGTH_SHORT).show();
                    loadMainUI();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String version = PackageInfoUtils.getPackageVersion(this);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本:" + version);
        //检查sp里面的状态,看自动更新是否开启
        SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
        boolean update = sp.getBoolean("update",true);
        if(update){
            //开启子线程获取服务器的版本信息
            new Thread(new CheckVersionTask()).start();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1500);
                    loadMainUI();
                }
            }).start();
        }
    }

    /**
     * 显示自动更新的对话框
     * @param desc 描述
     */
    protected void showUpdateDialog(String desc){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("升级提醒");
        builder.setMessage(desc);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = new ProgressDialog(SplashActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                HttpUtils http = new HttpUtils();
                File sdDir = Environment.getExternalStorageDirectory();
                File file = new File(sdDir, uptimeMillis()+".apk");
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    http.download(downloadpath, file.getAbsolutePath(), new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            progressDialog.dismiss();
                            Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT).show();

                            //自动安装
                            Intent intent = new Intent();

                            intent.setAction("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setDataAndType(Uri.fromFile(responseInfo.result),"application/vnd.android.package-archive");
                            startActivity(intent);
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            progressDialog.setMax((int) total);
                            progressDialog.setProgress((int) current);
                            super.onLoading(total, current, isUploading);
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            progressDialog.dismiss();
                            Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                            loadMainUI();
                        }
                    });
                }else{
                    Toast.makeText(SplashActivity.this,"sd卡不可用,无法自动更新",Toast.LENGTH_SHORT).show();
                    loadMainUI();
                }

            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadMainUI();
            }
        });
        builder.show();
    }

    private void loadMainUI() {
        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 获取服务器配置的最新版本号
     */
    private class CheckVersionTask implements Runnable{
        @Override
        public void run() {
            Message msg = handler.obtainMessage();
            long startTime = System.currentTimeMillis();
            try {
                String path = getResources().getString(R.string.url);
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(2000);
                int code = conn.getResponseCode();
                if(code==200){
                    InputStream is = conn.getInputStream();
                    String result = StreamTools.readStream(is);
                    JSONObject json = new JSONObject(result);
                    String serverVersion = json.getString("version");
                    String desc = json.getString("description");
                    downloadpath = json.getString("downloadpath");

                    String localVersion = PackageInfoUtils.getPackageVersion(SplashActivity.this);
                    if(localVersion.equals(serverVersion)){
                        Log.i(TAG,"版本号一致,无需升级,进入程序主界面");
                        loadMainUI();
                    }else{
                        Log.i(TAG,"版本号不一致,提示用户升级");
                        msg = handler.obtainMessage();
                        msg.what = SHOW_UPDATE_DIALOG;
                        msg.obj = desc;
                    }
                }else{
                    msg = handler.obtainMessage();
                    msg.what = ERROR;
                    msg.obj = "code:410";
                }

            } catch (MalformedURLException e) {
                msg = handler.obtainMessage();
                msg.what = ERROR;
                msg.obj = "code:404";
            } catch (IOException e) {
                msg = handler.obtainMessage();
                msg.what = ERROR;
                msg.obj = "code:408";
            } catch (JSONException e) {
                msg = handler.obtainMessage();
                msg.what = ERROR;
                msg.obj = "code:409";
            } finally{
                //计算代码走到这所用的时间
                long endTime = System.currentTimeMillis();
                long dTime = endTime - startTime;
                if(dTime > 2000){

                }else{
                    SystemClock.sleep(2000-dTime);
                }
                handler.sendMessage(msg);
            }


        }
    }

}
