package com.itheima.mobilesafe.db.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 号码归属地的数据库data access object
 * Created by zyp on 2016/7/7.
 */
public class AddressDBDao {
    /**
     * 查询手机号码的归属地信息
     * @param number
     * @return
     */
    public static String findLocation(String number){
        String location = "查无此号";
        String path = "/data/data/com.itheima.mobilesafe/files/address.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        //判断number是不是一个手机号码
        boolean result = number.matches("^1[34578]\\d{9}$");

        if(result){
            Cursor cursor = db.rawQuery(
                    "select location from data2 where id = (select outkey from data1 where id = ?)",new String[]{number.substring(0,7)});

            if(cursor.moveToNext()){
                location = cursor.getString(0);
            }
            cursor.close();
        }else{//非手机号码
            switch (number.length()){
                case 3:
                    location = "报警电话";
                    break;
                case 4:
                    location = "模拟器";
                    break;
                case 5:
                    location = "商业客服电话";
                    break;
                case 7:
                case 8:
                    location = "本地电话";
                    break;
                default:
                    if(number.length()>=10&&number.startsWith("0")){
                        Cursor cursor = db.rawQuery("select location from data2 where area = ?",
                                new String[]{number.startsWith("0")?number.substring(1,3):number.substring(0,4)});
                        if(cursor.moveToNext()){
                            location = cursor.getString(0);
                        }
                        cursor.close();
                    }
                    break;
            }
        }
        db.close();
        return location;
    }
}
