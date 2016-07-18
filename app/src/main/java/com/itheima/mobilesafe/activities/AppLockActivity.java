package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.Dao.ApplockDao;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends Activity implements View.OnClickListener{
    private LinearLayout ll_locked;
    private LinearLayout ll_unlock;
    private TextView tv_locked;
    private TextView tv_unlock;
    private TextView tv_unlock_count;
    private TextView tv_locked_count;
    /**
     * 已加锁列表
     */
    private ListView lv_locked_items;
    /**
     * 未加锁列表
     */
    private ListView lv_unclock_items;
    private PackageManager pm;
    /**
     * 所有的应用程序
     */
    List<PackageInfo> packinfos;
    /**
     * 未加锁条目集合
     */
    List<PackageInfo> unlockPackinfos;
    /**
     * 已加锁条目集合
     */
    List<PackageInfo> lockedPackinfos;
    private UnlockItemsAdapter unlockAdapter;
    private LockedItemsAdapter lockedAdapter;

    private ApplockDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        dao = new ApplockDao(this);
        ll_locked = (LinearLayout) findViewById(R.id.ll_locked);
        ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
        tv_locked = (TextView) findViewById(R.id.tv_locked);
        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        lv_unclock_items = (ListView) findViewById(R.id.lv_unlock_items);
        lv_locked_items = (ListView) findViewById(R.id.lv_locked_items);
        tv_unlock_count = (TextView) findViewById(R.id.tv_unlock_count);
        tv_locked_count = (TextView) findViewById(R.id.tv_locked_count);
        tv_locked.setOnClickListener(this);
        tv_unlock.setOnClickListener(this);
        //得到系统里面的所有应用程序
        pm = getPackageManager();
        unlockAdapter = new UnlockItemsAdapter();
        lockedAdapter = new LockedItemsAdapter();
        packinfos = pm.getInstalledPackages(0);
        unlockPackinfos = new ArrayList<PackageInfo>();
        lockedPackinfos = new ArrayList<PackageInfo>();
        for(PackageInfo info : packinfos){
            if(dao.find(info.packageName)){
                //被锁定的应用程序
                lockedPackinfos.add(info);
            }else{
                //没有被锁定的应用程序
                unlockPackinfos.add(info);
            }
        }
        lv_unclock_items.setAdapter(unlockAdapter);
        lv_locked_items.setAdapter(lockedAdapter);
        //给未加锁的item注册点击事件
        lv_unclock_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageInfo info = unlockPackinfos.get(position);
                dao.add(info.packageName);
                //从当前页面把应用程序条目给移除
                unlockPackinfos.remove(position);
                unlockAdapter.notifyDataSetChanged();
                //把数据加入到已加锁的列表里面
                lockedPackinfos.add(info);
                lockedAdapter.notifyDataSetChanged();
            }
        });
        lv_locked_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageInfo info = lockedPackinfos.get(position);
                dao.delete(info.packageName);
                //从当前页面把应用程序条目给移除
                lockedPackinfos.remove(position);
                lockedAdapter.notifyDataSetChanged();
                unlockPackinfos.add(info);
                unlockAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_unlock:
                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tv_locked.setBackgroundResource(R.drawable.tab_right_default);
                ll_unlock.setVisibility(View.VISIBLE);
                ll_locked.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_locked:
                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                tv_locked.setBackgroundResource(R.drawable.tab_right_pressed);
                ll_unlock.setVisibility(View.INVISIBLE);
                ll_locked.setVisibility(View.VISIBLE);
                break;
        }
    }

    private class UnlockItemsAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            tv_unlock_count.setText("未加锁程序："+unlockPackinfos.size()+"个");
            return unlockPackinfos.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PackageInfo info = unlockPackinfos.get(position);
            View view;
            ViewHolder holder;
            if(convertView != null&&convertView instanceof RelativeLayout){
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(getApplicationContext(),R.layout.item_unlock,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(holder);
            }
            holder.iv_icon.setImageDrawable(info.applicationInfo.loadIcon(pm));
            holder.tv_name.setText(info.applicationInfo.loadLabel(pm));
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

    private class LockedItemsAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            tv_locked_count.setText("已加锁程序："+lockedPackinfos.size()+"个");
            return lockedPackinfos.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PackageInfo info = lockedPackinfos.get(position);
            View view;
            ViewHolder holder;
            if(convertView != null&&convertView instanceof RelativeLayout){
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(getApplicationContext(),R.layout.item_locked,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(holder);
            }
            holder.iv_icon.setImageDrawable(info.applicationInfo.loadIcon(pm));
            holder.tv_name.setText(info.applicationInfo.loadLabel(pm));
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

    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
    }
}
