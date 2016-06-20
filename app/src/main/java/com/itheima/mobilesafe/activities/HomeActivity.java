package com.itheima.mobilesafe.activities;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

public class HomeActivity extends AppCompatActivity {
    private ImageView iv_home_logo;
    private GridView gv_home_item;
    final String[] names = new String[]{"手机防盗","骚扰拦截","软件管家","进程管理",
            "流量统计","手机杀毒","系统加速","常用工具"};
    final int[] icons = new int[]{R.drawable.sjfd,R.drawable.srlj,R.drawable.rjgj,R.drawable.jcgl,
            R.drawable.lltj,R.drawable.sjsd,R.drawable.xtjs,R.drawable.cygj};
    final String[] desc = new String[]{"远程定位手机","全面拦截骚扰","管理您的软件","管理正在运行",
            "流量一目了然","病毒无法藏身","系统快如闪电","常用工具大全"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv_home_logo = (ImageView)findViewById(R.id.iv_home_logo);
        ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo,"rotationY",45,90,135,180,225,270,315);
        oa.setDuration(3000);
        oa.setRepeatCount(ObjectAnimator.INFINITE);
        oa.setRepeatMode(ObjectAnimator.RESTART);
        oa.start();

        gv_home_item = (GridView)findViewById(R.id.gv_home_item);

        gv_home_item.setAdapter(new HomeAdapter());
    }

    private class HomeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder viewHolder;
            if(view == null){
                view = View.inflate(HomeActivity.this,R.layout.item_home,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_homeitem_icon = (ImageView)view.findViewById(R.id.iv_homeitem_icon);
                viewHolder.tv__homeitem_title = (TextView)view.findViewById(R.id.tv_homeitem_title);
                viewHolder.tv_homeitem_desc = (TextView)view.findViewById(R.id.tv_homeitem_desc);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)view.getTag();
            }
            viewHolder.iv_homeitem_icon.setImageResource(icons[position]);
            viewHolder.tv__homeitem_title.setText(names[position]);
            viewHolder.tv_homeitem_desc.setText(desc[position]);
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

        class ViewHolder{
            ImageView iv_homeitem_icon;
            TextView tv__homeitem_title,tv_homeitem_desc;
        }
    }

}
