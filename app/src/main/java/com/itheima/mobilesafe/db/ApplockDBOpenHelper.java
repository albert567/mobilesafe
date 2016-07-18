package com.itheima.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 程序加锁
 * Created by zyp on 2016/7/2.
 */
public class ApplockDBOpenHelper extends SQLiteOpenHelper {
    public ApplockDBOpenHelper(Context context) {
        super(context, "itheima.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //id主键自增长，packname被加锁应用程序的包名
        db.execSQL("create table lockinfo(_id integer primary key autoincrement,packname varchar(20),mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
