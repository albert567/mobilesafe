package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.Dao.AddressDBDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zyp on 2016/7/7.
 */
public class NumberQueryActivity extends Activity{
    private EditText et_number;
    private TextView tv_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_query);

        et_number = (EditText)findViewById(R.id.et_number);
        tv_location = (TextView)findViewById(R.id.tv_location);
        //给文本输入框注册一个内容变化的监听器
        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=10){
                    String location = AddressDBDao.findLocation(s.toString());
                    tv_location.setText("归属地为："+location);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void query(View view){
        String number = et_number.getText().toString().trim();
        if(TextUtils.isEmpty(number)){
            //Toast.makeText(NumberQueryActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
            Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
            findViewById(R.id.et_number).startAnimation(shake);
        }
        String location = AddressDBDao.findLocation(number);
        tv_location.setText("归属地为："+location);
    }
}
