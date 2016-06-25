package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by zyp on 2016/6/25.
 */
public abstract class SetupBaseActivity extends Activity {
    private static final String TAG = "SetupBaseActivity";
    //1.定义一个手势识别器
    private GestureDetector mGestureDetector;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        //2.初始化手势识别器
        mGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            /**
             * 当用户手指在屏幕上滑动时执行
             * @param e1  手指第一次触摸到屏幕的屏幕
             * @param e2  手指离开屏幕一瞬间对应的数据
             * @param velocityX 水平方向的速度
             * @param velocityY 竖直方向的速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(Math.abs(velocityX)<200){
                    Log.i(TAG,"移动的太慢,无效动作");
                    return true;
                }
                //Y轴移动太大
                if(Math.abs(e2.getRawY()-e1.getRawY())>50){
                    Log.i(TAG,"垂直方向移动过大,无效操作");
                    return true;
                }
                if((e1.getX()-e2.getX())>200){
                    Log.i(TAG,"向左滑动,显示下一个界面");
                    next();
                    return true;
                }
                if((e2.getX()-e1.getX())>200){
                    Log.i(TAG,"向左滑动,显示上一个界面");
                    pre();
                    return true;
                }

                return false;
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //让手势识别器识别传进来的事件
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 显示下一个
     */
    public abstract void next();

    /**
     * 显示上一个
     */
    public abstract void pre();

    /**
     * 显示下一个界面
     * @param view
     */
    public void showNext(View view){
        next();
    }
    /**
     * 显示上一个界面
     * @param view
     */
    public void showPre(View view){
        pre();
    }

    /**
     * 打开新的界面并且关闭掉当前界面
     * @param cls
     */
    public void openNewActivityAndFinish(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
        finish();
    }
}
