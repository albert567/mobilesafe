package com.itheima.mobilesafe;

import android.app.Application;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

/**
 * 需要在清单中配置
 *  代表手机卫士的应用程序
 * Created by zyp on 2016/7/23.
 */
public class MobilesafeApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //重写系统的异常处理器
        Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHandler());
    }

    private class MyExceptionHandler implements Thread.UncaughtExceptionHandler{
        //当发现了未捕获异常的时候调用的方法
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            System.out.println("发生了异常，被我捕获了");

            StringBuilder sb = new StringBuilder();
            sb.append("time:");
            sb.append(System.currentTimeMillis()+"\n");
            Field[] fields = Build.class.getDeclaredFields();
            for(Field field:fields){
                try {
                    String name = field.getName();
                    String value = (String)field.get(null);
                    sb.append(name+"="+value+"\n");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            sb.append(sw.toString());
            File file = new File(Environment.getExternalStorageDirectory(),"error.log");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //自杀
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }
}
