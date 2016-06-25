package com.itheima.mobilesafe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;

public class Setup3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }
    /**
     * 显示下一个界面
     * @param view
     */
    public void showNext(View view){
        Intent intent = new Intent(Setup3Activity.this,Setup4Activity.class);
        startActivity(intent);
    }
}
