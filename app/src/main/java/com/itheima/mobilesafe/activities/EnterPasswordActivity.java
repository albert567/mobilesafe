package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;

public class EnterPasswordActivity extends Activity {
    private ImageView iv_icon;
    private TextView tv_name;
    private EditText et_password;
    private String packname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_password = (EditText)findViewById(R.id.et_password);

        Intent intent = getIntent();
        packname = intent.getStringExtra("packname");

        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(packname,0);
            iv_icon.setImageDrawable(appinfo.loadIcon(pm));
            tv_name.setText(appinfo.loadLabel(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void enter(View view){
        String password = et_password.getText().toString();
        if("123".equals(password)){
            Toast.makeText(this,"密码输入正确",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("com.itheima.mobilesafe.tempstopprotect");
            intent.putExtra("packname",packname);
            sendBroadcast(intent);
            finish();
        }else{
            Toast.makeText(this,"密码输入错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
