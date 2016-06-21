package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.itheima.mobilesafe.R;

/**
 * 开关ImageView
 * Created by zyp on 2016/6/21.
 */
public class SwitchImageView extends ImageView{
    /**
     * 开关的状态,true打开,false关闭
     */
    private boolean switchStatus = true;

    /**
     * 获取开关的状态
     * @return
     */
    public boolean getSwitchStatus() {
        return switchStatus;
    }

    /**
     * 设置开关的状态
     * @param switchStatus
     */
    public void setSwitchStatus(boolean switchStatus) {
        this.switchStatus = switchStatus;
        if(switchStatus){
            setImageResource(R.drawable.on);
        }else{
            setImageResource(R.drawable.off);
        }
    }

    /**
     * 修改开关的状态
     */
    public void changeSwitchStatus(){
        setSwitchStatus(!switchStatus);
    }

    public SwitchImageView(Context context) {
        super(context);
    }



    public SwitchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
