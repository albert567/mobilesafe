package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import java.text.Format;
import java.util.List;

public class AppManagerActivity extends Activity {
    private TextView tv_internal_size;
    private TextView tv_sd_size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        tv_internal_size = (TextView) findViewById(R.id.tv_internal_size);
        tv_sd_size = (TextView) findViewById(R.id.tv_sd_size);
        String internal_size = Formatter.formatFileSize(this, SystemInfoUtils.getInternalStorageFreeSize());
        String sd_size = Formatter.formatFileSize(this,SystemInfoUtils.getSDStorageFreeSize());
        tv_internal_size.setText("机身内存可用：" + internal_size);
        tv_sd_size.setText("SD卡可用：" + sd_size);
        List<AppInfo> appInfos = AppInfoProvider.getAllAppInfo(this);
        for(AppInfo app:appInfos){
            System.out.println(app.toString());;
        }
    }
}
