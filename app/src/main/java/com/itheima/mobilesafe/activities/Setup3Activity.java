package com.itheima.mobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;

public class Setup3Activity extends SetupBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    @Override
    public void next() {
        openNewActivityAndFinish(Setup4Activity.class);
        //修改Activity切换的动画效果
        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
    }

    @Override
    public void pre() {
        openNewActivityAndFinish(Setup2Activity.class);
        overridePendingTransition(R.anim.anim_pre_in,R.anim.anim_pre_out);
    }
}
