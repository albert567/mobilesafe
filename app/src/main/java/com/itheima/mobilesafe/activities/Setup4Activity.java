package com.itheima.mobilesafe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;

public class Setup4Activity extends SetupBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    @Override
    public void next() {
        //做一个标记 告诉应用程序已经走过一遍设置向导
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("configed",true);
        editor.commit();
        openNewActivityAndFinish(LostFindActivity.class);
        //修改Activity切换的动画效果
        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
    }

    @Override
    public void pre() {
        openNewActivityAndFinish(Setup3Activity.class);
        overridePendingTransition(R.anim.anim_pre_in,R.anim.anim_pre_out);
    }
}
