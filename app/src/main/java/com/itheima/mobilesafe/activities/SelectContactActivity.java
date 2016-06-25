package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.ContactInfo;
import com.itheima.mobilesafe.utils.ContactInfoUtils;

import java.util.List;

public class SelectContactActivity extends Activity {
    private ListView lv_select_contact;
    private List<ContactInfo> infos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        lv_select_contact = (ListView)findViewById(R.id.lv_select_contact);
        infos = ContactInfoUtils.getAllContactInfos(this);
        lv_select_contact.setAdapter(new ContactAdapter());
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
