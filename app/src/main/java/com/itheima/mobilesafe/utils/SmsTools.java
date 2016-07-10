package com.itheima.mobilesafe.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 系统的短信工具类
 * Created by zyp on 2016/7/10.
 */
public class SmsTools {

    public interface BackupCallback{
        /**
         * 短信备份前调用的代码
         * @param max 一共多少条短信需要备份
         */
        void beforeSmsBackup(int max);

        /**
         * 短信备份过程中调用的代码
         * @param progress 当前备份的进度
         */
        void onSmsBackup(int progress);
    }

    /**
     * 短信的备份
     * @param context 上下文
     * @param callback 回调
     */
    public static void backUpSms(Context context, BackupCallback callback){
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        XmlSerializer serializer = Xml.newSerializer();
        File file = new File(Environment.getExternalStorageDirectory(),"smsbackup.xml");
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            serializer.setOutput(os,"utf-8");
            serializer.startDocument("utf-8",true);
            serializer.startTag(null,"infos");
            Cursor cursor = resolver.query(uri,new String[]{"address","body","type","date"},null,null,null);
            //pd.setMax(cursor.getCount());
            callback.beforeSmsBackup(cursor.getCount());
            int progress = 0;
            while(cursor.moveToNext()){
                serializer.startTag(null,"info");
                String address =  cursor.getString(0);
                serializer.startTag(null,"address");
                serializer.text(address);
                serializer.endTag(null,"address");
                String body = cursor.getString(1);
                serializer.startTag(null,"body");
                serializer.text(body);
                serializer.endTag(null,"body");
                String type = cursor.getString(2);
                serializer.startTag(null,"type");
                serializer.text(type);
                serializer.endTag(null,"type");
                String date = cursor.getString(3);
                serializer.startTag(null,"date");
                serializer.text(date);
                serializer.endTag(null,"date");
                serializer.endTag(null,"info");
                SystemClock.sleep(2000);
                progress++;
                //pd.setProgress(progress);
                callback.onSmsBackup(progress);
            }
            serializer.endTag(null,"infos");
            serializer.endDocument();
            os.close();
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
