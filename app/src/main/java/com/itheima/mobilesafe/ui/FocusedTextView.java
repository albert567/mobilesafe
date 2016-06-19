package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义view重写view复写里面的方法
 * Created by zyp on 2016/6/19.
 */
public class FocusedTextView extends TextView{
    public FocusedTextView(Context context) {
        super(context);
    }

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写父类的方法,欺骗系统textview直接获取焦点
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
