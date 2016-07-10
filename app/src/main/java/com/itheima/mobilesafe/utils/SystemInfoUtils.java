package com.itheima.mobilesafe.utils;

import android.os.Environment;

import java.io.File;

/**
 * 系统信息工具类
 * 主要获取手机的一些参数信息
 * Created by zyp on 2016/7/10.
 */
public class SystemInfoUtils {

    /**
     * 获取系统内部存储空间的总大小
     * @return
     */
    public static long getInternalStorageSize(){
        File file = Environment.getDataDirectory();
        return file.getTotalSpace();
    }
    /**
     * 获取系统内部存储空间的可用
     * @return
     */
    public static long getInternalStorageFreeSize(){
        File file = Environment.getDataDirectory();
        return file.getFreeSpace();
    }

    /**
     * 获取SD卡存储空间的总大小
     * @return
     */
    public static long getSDStorageSize(){
        File file = Environment.getExternalStorageDirectory();
        return file.getTotalSpace();
    }
    /**
     * 获取SD卡存储空间的可用
     * @return
     */
    public static long getSDStorageFreeSize(){
        File file = Environment.getExternalStorageDirectory();
        return file.getFreeSpace();
    }
}
