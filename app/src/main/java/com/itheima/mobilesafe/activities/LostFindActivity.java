package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;


public class LostFindActivity extends Activity {
    private TextView tv_lostfind_number;
    private ImageView iv_lostfind_status;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find);
        tv_lostfind_number = (TextView)findViewById(R.id.tv_lostfind_number);
        iv_lostfind_status = (ImageView)findViewById(R.id.iv_lostfind_status);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        tv_lostfind_number.setText(sp.getString("safenumber",""));
        boolean protecting = sp.getBoolean("protecting",false);
        if(protecting){
            iv_lostfind_status.setImageResource(R.drawable.lock);
        }else{
            iv_lostfind_status.setImageResource(R.drawable.unlock);
        }

    }

    public void reloadSetupUI(View view){
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
    }

    public void changeProtectStatus(View view){
        boolean protecting = sp.getBoolean("protecting",false);
        if(protecting){
            //关闭保护
            iv_lostfind_status.setImageResource(R.drawable.unlock);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("protecting",false);
            editor.commit();
        }else{
            //开启保护
            iv_lostfind_status.setImageResource(R.drawable.lock);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("protecting",true);
            editor.commit();
        }
    }
}
