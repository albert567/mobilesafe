package com.itheima.mobilesafe.db.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单数据库，增删改查的api
 * Created by zyp on 2016/7/2.
 */
public class BlackNumberDao {
    private BlackNumberDBOpenHelper helper;

    /**
     * 在构造方法里初始化helper对象
     * @param context
     */
    public BlackNumberDao(Context context) {
        helper = new BlackNumberDBOpenHelper(context);
    }

    /**
     * 添加黑名单号码
     * @param phone 黑名单号码
     * @param mode 拦截模式
     */
    public boolean add(String phone,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone",phone);
        values.put("mode",mode);
        long result = db.insert("blacknumber",null,values);
        db.close();
        if(result!=-1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 删除黑名单号码
     * @param phone 号码
     * @return
     */
    public boolean delete(String phone){
        SQLiteDatabase db = helper.getWritableDatabase();
        int result = db.delete("blacknumber","phone=?",new String[]{phone});
        db.close();
        if(result>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 修改黑名单号码的拦截模式
     * @param phone 号码
     * @param mode 拦截模式
     * @return
     */
    public boolean updateMode(String phone,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        int result = db.update("blacknumber",values,"phone=?",new String[]{phone});
        db.close();
        if(result>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询某个号码的拦截模式
     * @param phone 号码
     * @return 拦截模式，如果返回null，代表当前号码不是黑名单号码
     */
    public String find(String phone){
        String mode = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber",new String[]{"mode"},"phone=?",new String[]{phone},null,null,null);
        if(cursor.moveToNext()){
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 获取全部的黑名单号码信息
     * @return
     */
    public List<BlackNumberInfo> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        Cursor cursor = db.query("blacknumber",new String[]{"_id","phone","mode"},null,null,null,null,null);
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String phone = cursor.getString(1);
            String mode = cursor.getString(2);
            BlackNumberInfo info = new BlackNumberInfo(id,phone,mode);
            infos.add(info);
        }
        cursor.close();
        return infos;
    }
}
