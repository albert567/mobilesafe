package com.itheima.mobilesafe.engine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.itheima.mobilesafe.domain.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务方法，用于获取系统里面所有的应用程序信息
 * Created by zyp on 2016/7/10.
 */
public class AppInfoProvider {

    /**
     *  获取系统所有的应用程序信息集合
     *  @param context 上下文
     * @return
     */
    public static List<AppInfo> getAllAppInfo(Context context){
        //PackageManager 包管理器，管理手机里的应用程序信息
        PackageManager pm = context.getPackageManager();
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        for(PackageInfo packInfo:packInfos){
            String packName = packInfo.packageName;
            Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
            String appName = packInfo.applicationInfo.loadLabel(pm).toString();
            String apkPath = packInfo.applicationInfo.sourceDir;
            File file = new File(apkPath);
            long apkSize = file.length();
            AppInfo appInfo = new AppInfo();
            appInfo.setAppIcon(appIcon);
            appInfo.setAppName(appName);
            appInfo.setAppSize(apkSize);
            appInfo.setPackName(packName);
            appInfos.add(appInfo);
        }
        return appInfos;
    }

}
