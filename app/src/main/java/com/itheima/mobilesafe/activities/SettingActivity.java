package com.itheima.mobilesafe.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.ui.SwitchImageView;

public class SettingActivity extends AppCompatActivity {
    //共享参数
    private SharedPreferences sp;
    private SwitchImageView siv_setting_update;
    private RelativeLayout rl_setting_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //初始化sp
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        siv_setting_update = (SwitchImageView)findViewById(R.id.siv_setting_update);
        siv_setting_update.setSwitchStatus(sp.getBoolean("update",true));
        rl_setting_update = (RelativeLayout)findViewById(R.id.rl_setting_update);
        rl_setting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv_setting_update.changeSwitchStatus();
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("update",siv_setting_update.getSwitchStatus());
                editor.commit();
                //保存开关状态到sp
            }
        });
    }
}
