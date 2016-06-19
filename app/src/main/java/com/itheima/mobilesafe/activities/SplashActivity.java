package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.PackageInfoUtils;



public class SplashActivity extends Activity {
    private TextView tv_splash_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String version = PackageInfoUtils.getPackageVersion(this);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本:" + version);

    }

}
