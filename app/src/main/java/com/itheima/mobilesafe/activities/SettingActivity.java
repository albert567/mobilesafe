package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.CallSmsSafeService;
import com.itheima.mobilesafe.ui.SwitchImageView;
import com.itheima.mobilesafe.utils.ServiceStatusUtils;

public class SettingActivity extends Activity {
    //共享参数
    private SharedPreferences sp;
    //自动更新的控件声明
    private SwitchImageView siv_setting_update;
    private RelativeLayout rl_setting_update;
    //骚扰拦截的控件声明
    private SwitchImageView siv_callsmssafe;
    private RelativeLayout rl_callsmssafe;

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
        //初始化骚扰拦截的设置
        siv_callsmssafe = (SwitchImageView)findViewById(R.id.siv_callsmssafe);
        rl_callsmssafe = (RelativeLayout)findViewById(R.id.rl_callsmssafe);
        //获取当前服务运行的状态，根据状态去修改界面显示的内容
        boolean status = ServiceStatusUtils.isServiceRunning(this,
                "com.itheima.mobilesafe.service.CallSmsSafeService");
        siv_callsmssafe.setSwitchStatus(status);
        rl_callsmssafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv_callsmssafe.changeSwitchStatus();
                boolean status = siv_callsmssafe.getSwitchStatus();
                //保存开关状态到sp
                Intent intent = new Intent(SettingActivity.this, CallSmsSafeService.class);
                if(status){
                    startService(intent);
                }else{
                    stopService(intent);
                }
            }
        });
    }
}
