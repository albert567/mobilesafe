package com.itheima.mobilesafe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;

public class Setup2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }

    /**
     * 显示下一个界面
     * @param view
     */
    public void showNext(View view){
        Intent intent = new Intent(Setup2Activity.this,Setup3Activity.class);
        startActivity(intent);
    }
}
