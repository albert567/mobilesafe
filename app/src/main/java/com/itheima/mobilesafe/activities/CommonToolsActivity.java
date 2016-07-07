package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;

public class CommonToolsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tools);

    }

    public void enterNumberQueryActivity(View view){
        Intent intent = new Intent(this,NumberQueryActivity.class);
        startActivity(intent);
    }

}
