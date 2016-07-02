package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;

public class CallSmsSafeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsms_safe);
    }
    public void addBlackNumber(View view){
        Intent intent = new Intent(this,AddBlackNumberActivity.class);
        startActivityForResult(intent,0);
    }
}
