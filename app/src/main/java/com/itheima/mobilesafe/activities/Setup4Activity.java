package com.itheima.mobilesafe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.itheima.mobilesafe.R;

public class Setup4Activity extends SetupBaseActivity {
    private CheckBox cb_setup4_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cb_setup4_status = (CheckBox)findViewById(R.id.cb_setup4_status);
        cb_setup4_status.setChecked(sp.getBoolean("protecting",false));
        //设置cb_setup4_status的状态监听器
        cb_setup4_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
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
