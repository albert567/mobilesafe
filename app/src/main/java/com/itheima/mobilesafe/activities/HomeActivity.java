package com.itheima.mobilesafe.activities;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.itheima.mobilesafe.R;

public class HomeActivity extends AppCompatActivity {

    private ImageView iv_home_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv_home_logo = (ImageView)findViewById(R.id.iv_home_logo);
        ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo,"rotationY",45,90,135,180,225,270,315);
        oa.setDuration(3000);
        oa.setRepeatCount(ObjectAnimator.INFINITE);
        oa.setRepeatMode(ObjectAnimator.RESTART);
        oa.start();
    }
}
