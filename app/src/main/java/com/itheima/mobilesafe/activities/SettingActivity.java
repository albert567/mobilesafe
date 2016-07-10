package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.CallSmsSafeService;
import com.itheima.mobilesafe.service.ShowAddressService;
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
    //归属地显示设置的控件声明
    private SwitchImageView siv_showlocation;
    private RelativeLayout rl_showlocation;
    //修改归属地风格
    private RelativeLayout rl_setting_changestyle;
    private String[] bgNames = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};

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
                Intent intent = new Intent(SettingActivity.this, CallSmsSafeService.class);
                if(status){
                    startService(intent);
                }else{
                    stopService(intent);
                }
            }
        });

        //初始化归属地显示的设置
        siv_showlocation = (SwitchImageView)findViewById(R.id.siv_showlocation);
        rl_showlocation = (RelativeLayout)findViewById(R.id.rl_showlocation);
        //获取当前服务运行的状态，根据状态去修改界面显示的内容
        boolean showAddressStatus = ServiceStatusUtils.isServiceRunning(this,
                "com.itheima.mobilesafe.service.ShowAddressService");
        siv_showlocation.setSwitchStatus(showAddressStatus);
        rl_showlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv_showlocation.changeSwitchStatus();
                boolean status = siv_showlocation.getSwitchStatus();
                Intent intent = new Intent(SettingActivity.this, ShowAddressService.class);
                if(status){
                    startService(intent);
                }else{
                    stopService(intent);
                }
            }
        });

        //修改归属地风格
        rl_setting_changestyle = (RelativeLayout)findViewById(R.id.rl_setting_changestyle);
        rl_setting_changestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出修改归属地显示风格
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setIcon(R.drawable.dialog_title_default_icon);
                builder.setTitle("归属地提示框风格");
                builder.setSingleChoiceItems(bgNames, sp.getInt("which",0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("which",which);
                        editor.commit();
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}
