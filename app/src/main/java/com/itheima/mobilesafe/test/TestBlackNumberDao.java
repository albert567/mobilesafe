package com.itheima.mobilesafe.test;

import android.test.AndroidTestCase;

import com.itheima.mobilesafe.db.Dao.BlackNumberDao;

/**
 * Created by zyp on 2016/7/2.
 */
public class TestBlackNumberDao extends AndroidTestCase{
    public void testAdd() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.add("5558","1");
    }
    public void testDelete() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.delete("5558");
    }
    public void testUpdate() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.updateMode("5558","2");
    }
    public void testFind() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(getContext());
        String mode = dao.find("5558");
        if("1".equals(mode)){
            System.out.println("电话拦截");
        }else if("2".equals(mode)){
            System.out.println("短信拦截");
        }else if("3".equals(mode)){
            System.out.println("全部拦截");
        }else{
            System.out.println("不是黑名单号码");
        }
    }
}
