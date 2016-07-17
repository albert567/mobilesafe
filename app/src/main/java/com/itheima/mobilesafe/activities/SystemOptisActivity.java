package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.fragment.CleanCacheFragment;
import com.itheima.mobilesafe.fragment.CleanSDFragment;

public class SystemOptisActivity extends Activity implements View.OnClickListener{
    private LinearLayout ll_cache_clean;
    private LinearLayout ll_sd_clean;
    private ImageView iv_cache_clean;
    private ImageView iv_sd_clean;
    private TextView tv_cache_clean;
    private TextView tv_sd_clean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_optis);
        ll_cache_clean = (LinearLayout) findViewById(R.id.ll_cache_clean);
        ll_sd_clean = (LinearLayout) findViewById(R.id.ll_sd_clean);
        iv_cache_clean = (ImageView) findViewById(R.id.iv_cache_clean);
        iv_sd_clean = (ImageView) findViewById(R.id.iv_sd_clean);
        tv_cache_clean = (TextView) findViewById(R.id.tv_cache_clean);
        tv_sd_clean = (TextView) findViewById(R.id.tv_sd_clean);
        ll_cache_clean.setOnClickListener(this);
        ll_sd_clean.setOnClickListener(this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        CleanCacheFragment f1 = new CleanCacheFragment();
        ft.replace(R.id.fl_container,f1);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.ll_cache_clean:
                ll_sd_clean.setBackgroundResource(R.drawable.antispam_report_button);
                ll_cache_clean.setBackgroundDrawable(null);
                iv_cache_clean.setImageResource(R.drawable.clean_cache_icon_pressed);
                iv_sd_clean.setImageResource(R.drawable.clean_sdcard_icon);
                tv_cache_clean.setTextColor(0xFF7bB226);
                tv_sd_clean.setTextColor(0x99000000);
                CleanCacheFragment f1 = new CleanCacheFragment();
                ft.replace(R.id.fl_container,f1);
                ft.commit();
                break;
            case R.id.ll_sd_clean:
                ll_cache_clean.setBackgroundResource(R.drawable.antispam_report_button);
                ll_sd_clean.setBackgroundDrawable(null);
                iv_cache_clean.setImageResource(R.drawable.clean_cache_icon);
                iv_sd_clean.setImageResource(R.drawable.clean_sdcard_icon_pressed);
                tv_cache_clean.setTextColor(0x99000000);
                tv_sd_clean.setTextColor(0xFF7bB226);
                CleanSDFragment f2 = new CleanSDFragment();
                ft.replace(R.id.fl_container,f2);
                ft.commit();
                break;

        }
    }
}
