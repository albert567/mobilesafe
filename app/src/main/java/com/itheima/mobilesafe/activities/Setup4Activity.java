package com.itheima.mobilesafe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;

public class Setup4Activity extends AppCompatActivity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    /**
     * 显示手机防盗界面
     * @param view
     */
    public void showNext(View view){
        Intent intent = new Intent(Setup4Activity.this,LostFindActivity.class);
        startActivity(intent);
        //做一个标记 告诉应用程序已经走过一遍设置向导
        sp = getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("configed",true);
        editor.commit();
    }
}
