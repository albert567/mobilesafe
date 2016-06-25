package com.itheima.mobilesafe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itheima.mobilesafe.R;

public class Setup2Activity extends SetupBaseActivity {
    private RelativeLayout rl_setup2_bind;
    private ImageView iv_setup2_status;
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        rl_setup2_bind = (RelativeLayout)findViewById(R.id.rl_setup2_bind);
        iv_setup2_status = (ImageView)findViewById(R.id.iv_setup2_status);
        //获取系统里面电话管理的服务
        tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        //判断用户是否绑定过sim卡
        String bindsim = sp.getString("sim",null);
        if(TextUtils.isEmpty(bindsim)){
            //没有绑定,需要绑定
            iv_setup2_status.setImageResource(R.drawable.unlock);
        }else{
            //已绑定
            iv_setup2_status.setImageResource(R.drawable.lock);
        }
        rl_setup2_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bindsim = sp.getString("sim",null);
                SharedPreferences.Editor editor = sp.edit();
                if(TextUtils.isEmpty(bindsim)){
                    //没有绑定,需要绑定,把sim卡串号存入到sp
                    String sim = tm.getSimSerialNumber();
                    editor.putString("sim",sim);
                    editor.commit();
                    iv_setup2_status.setImageResource(R.drawable.lock);
                }else{
                    //解除绑定
                    editor.putString("sim",null);
                    editor.commit();
                    iv_setup2_status.setImageResource(R.drawable.unlock);
                }
            }
        });
    }

    @Override
    public void next() {
        //判断用户是否绑定了sim卡
        String bindsim = sp.getString("sim",null);
        if(TextUtils.isEmpty(bindsim)){
            Toast.makeText(this,"手机防盗生效,必须先绑定sim卡",Toast.LENGTH_SHORT).show();
            return;
        }
        openNewActivityAndFinish(Setup3Activity.class);
        //修改Activity切换的动画效果
        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
    }

    @Override
    public void pre() {
        openNewActivityAndFinish(Setup1Activity.class);
        overridePendingTransition(R.anim.anim_pre_in,R.anim.anim_pre_out);
    }
}
