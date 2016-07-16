package com.itheima.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.ProcessInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 进程信息的业务类
 * Created by zyp on 2016/7/16.
 */
public class ProcessInfoProvider{
    /**
     * 获取所有的正在运行的进程信息
     * @param context
     * @return
     */
    public static List<ProcessInfo> getRunningProcessInfos(Context context){
        List<ProcessInfo> pInfos = new ArrayList<ProcessInfo>();
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        PackageManager pm = (PackageManager) context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info:infos){
            ProcessInfo processInfo = new ProcessInfo();
            //进程名其实就是包名
            String processName = info.processName;
            processInfo.setPackName(processName);
            long memSize = am.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty() * 1024;
            processInfo.setMemSize(memSize);
            try {
                PackageInfo packInfo = pm.getPackageInfo(processName,0);
                Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
                processInfo.setAppIcon(appIcon);
                String appName = packInfo.applicationInfo.loadLabel(pm).toString();
                processInfo.setAppName(appName);
                if((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                    //系统进程
                    processInfo.setUserProcess(false);
                }else{
                    //用户进程
                    processInfo.setUserProcess(true);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                processInfo.setAppName(processName);
                processInfo.setAppIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
            }
            pInfos.add(processInfo);
        }
        return pInfos;
    }
}
