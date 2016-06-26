package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.ContactInfo;
import com.itheima.mobilesafe.utils.ContactInfoUtils;

import java.util.List;

public class SelectContactActivity extends Activity {
    private LinearLayout ll_loading;
    private ListView lv_select_contact;
    private List<ContactInfo> infos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        lv_select_contact = (ListView)findViewById(R.id.lv_select_contact);
        ll_loading = (LinearLayout)findViewById(R.id.ll_loading);
        //把正在加载的布局显示出来
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                infos = ContactInfoUtils.getAllContactInfos(SelectContactActivity.this);
                //设置适配器属于更新UI操作,不能在子线程里执行,两种方法,
                // 一种通过Handler和Message,一种是runOnUiThread方法(其实内部也是Handler和Message)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.INVISIBLE);
                        lv_select_contact.setAdapter(new ContactAdapter());
                    }
                });
            }
        }.start();

        lv_select_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = infos.get(position).getPhone();
                Intent data = new Intent();
                data.putExtra("phone",phone);
                //设置结果数据
                setResult(0,data);
                //关闭当前界面
                finish();
            }
        });
    }
    private class ContactAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(SelectContactActivity.this,R.layout.item_contact,null);
            TextView tv_name = (TextView)view.findViewById(R.id.tv_contactitem_name);
            TextView tv_phone = (TextView)view.findViewById(R.id.tv_contactitem_phone);
            tv_name.setText(infos.get(position).getName());
            tv_phone.setText(infos.get(position).getPhone());
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
