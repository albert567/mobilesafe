package com.itheima.mobilesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 包信息的工具类
 * Created by zyp on 2016/6/18.
 */
public class PackageInfoUtils {

    /**
     * 获取应用程序apk包的版本信息
     * @param context 上下文
     * @return
     */
    public static String getPackageVersion(Context context){
        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "解析版本号错误";
        }
    }
}
