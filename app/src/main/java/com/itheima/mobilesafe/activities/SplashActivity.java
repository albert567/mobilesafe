package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.PackageInfoUtils;
import com.itheima.mobilesafe.utils.StreamTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends Activity {
    public static final String TAG = "SplashActivity";
    public static final int SHOW_UPDATE_DIALOG = 1;
    public static final int ERROR = 2;
    private TextView tv_splash_version;

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
                default:
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

        new Thread(new CheckVersionTask()).start();
    }

    /**
     * 显示自动更新的对话框
     * @param desc 描述
     */
    protected void showUpdateDialog(String desc){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("升级提醒");
        builder.setMessage(desc);
        builder.setPositiveButton("立刻升级",null);
        builder.setNegativeButton("下次再说",null);
        builder.show();
    }

    /**
     * 获取服务器配置的最新版本号
     */
    private class CheckVersionTask implements Runnable{
        @Override
        public void run() {
            try {
                String path = getResources().getString(R.string.url);
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int code = conn.getResponseCode();
                if(code==200){
                    InputStream is = conn.getInputStream();
                    String result = StreamTools.readStream(is);
                    JSONObject json = new JSONObject(result);
                    String serverVersion = json.getString("version");
                    String desc = json.getString("description");
                    Log.i(TAG,"服务器版本:"+serverVersion);

                    String localVersion = PackageInfoUtils.getPackageVersion(SplashActivity.this);
                    if(localVersion.equals(serverVersion)){
                        Log.i(TAG,"版本号一致,无需升级,进入程序主界面");
                    }else{
                        Log.i(TAG,"版本号不一致,提示用户升级");
                        Message msg = handler.obtainMessage();
                        msg.what = SHOW_UPDATE_DIALOG;
                        msg.obj = desc;
                        handler.sendMessage(msg);
                    }
                }else{
                    Message msg = handler.obtainMessage();
                    msg.what = ERROR;
                    msg.obj = "code:410";
                    handler.sendMessage(msg);
                }

            } catch (MalformedURLException e) {
                Message msg = handler.obtainMessage();
                msg.what = ERROR;
                msg.obj = "code:404";
                handler.sendMessage(msg);
            } catch (IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = ERROR;
                msg.obj = "code:408";
                handler.sendMessage(msg);
            } catch (JSONException e) {
                Message msg = handler.obtainMessage();
                msg.what = ERROR;
                msg.obj = "code:409";
                handler.sendMessage(msg);
            }


        }
    }

}
