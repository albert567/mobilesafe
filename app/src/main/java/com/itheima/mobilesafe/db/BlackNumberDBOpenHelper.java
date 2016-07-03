package com.itheima.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zyp on 2016/7/2.
 */
public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {
    public BlackNumberDBOpenHelper(Context context) {
        super(context, "itheima.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //id主键自增长，phone电话号码，mode拦截模式
        db.execSQL("create table blacknumber(_id integer primary key autoincrement,phone varchar(20),mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
