package com.itheima.mobilesafe.db.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.itheima.mobilesafe.db.ApplockDBOpenHelper;
import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 程序锁数据库，增删改查的api
 * Created by zyp on 2016/7/2.
 */
public class ApplockDao {
    private ApplockDBOpenHelper helper;
    private Context context;
    /**
     * 在构造方法里初始化helper对象
     * @param context
     */
    public ApplockDao(Context context) {
        helper = new ApplockDBOpenHelper(context);
        this.context = context;
    }

    /**
     * 添加一条要锁定的应用程序信息
     * @param packname 包名
     */
    public boolean add(String packname){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packname",packname);
        long result = db.insert("lockinfo",null,values);
        db.close();
        //发送一个通知，通知内容观察者某个路径的数据变化了
        Uri uri = Uri.parse("content://com.itheima.mobilesafe.applockdb");
        context.getContentResolver().notifyChange(uri,null);
        if(result!=-1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 删除一条要锁定的应用程序信息
     * @param packname 包名
     * @return
     */
    public boolean delete(String packname){
        SQLiteDatabase db = helper.getWritableDatabase();
        int result = db.delete("lockinfo","packname=?",new String[]{packname});
        db.close();
        //发送一个通知，通知内容观察者某个路径的数据变化了
        Uri uri = Uri.parse("content://com.itheima.mobilesafe.applockdb");
        context.getContentResolver().notifyChange(uri,null);
        if(result>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询一个包名是否要被锁定
     * @param packname  包名
     * @return 如果找到，返回true，否则返回false
     */
    public boolean find(String packname){
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from lockinfo where packname = ?",new String[]{packname});
        if(cursor.moveToNext()){
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 获取全部的已加锁应用程序包名
     * @return
     */
    public List<String> findAll(){
        List<String> infos = new ArrayList<String>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("lockinfo",new String[]{"packname"},null,null,null,null,null);
        while(cursor.moveToNext()){
            String packinfo = cursor.getString(0);
            infos.add(packinfo);
        }
        cursor.close();
        db.close();
        return infos;
    }
}
