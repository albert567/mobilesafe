package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends Activity {
    private TextView tv_internal_size;
    private TextView tv_sd_size;
    private ListView lv_app;
    private TextView tv_status;
    private LinearLayout ll_loading;
    /**
     * 代表的是程序信息的悬浮窗体
     * 需求：在Activity上只有一个悬浮窗体存在。
     */
    private PopupWindow popupWindow;
    //所有的应用程序
    private List<AppInfo> appInfos;
    //用户应用程序
    private List<AppInfo> userAppInfos;
    //系统应用程序
    private List<AppInfo> systemAppInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        tv_internal_size = (TextView) findViewById(R.id.tv_internal_size);
        tv_sd_size = (TextView) findViewById(R.id.tv_sd_size);
        tv_status = (TextView) findViewById(R.id.tv_status);
        lv_app = (ListView) findViewById(R.id.lv_app);
        lv_app.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userAppInfos != null&&systemAppInfos != null){
                    if(firstVisibleItem>userAppInfos.size()){
                        tv_status.setText("系统程序：" + systemAppInfos.size());
                    }else{
                        tv_status.setText("用户程序：" + userAppInfos.size());
                    }
                }
                if(popupWindow != null){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });
        setAppInfoItemClickListener();

        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        systemAppInfos = new ArrayList<AppInfo>();
        userAppInfos = new ArrayList<AppInfo>();

        String internal_size = Formatter.formatFileSize(this, SystemInfoUtils.getInternalStorageFreeSize());
        String sd_size = Formatter.formatFileSize(this,SystemInfoUtils.getSDStorageFreeSize());
        tv_internal_size.setText("机身内存可用：" + internal_size);
        tv_sd_size.setText("SD卡可用：" + sd_size);
        //显示正在努力加载中
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                appInfos = AppInfoProvider.getAllAppInfo(AppManagerActivity.this);
                for(AppInfo appinfo:appInfos){
                    if(appinfo.isSystemApp()){
                        systemAppInfos.add(appinfo);
                    }else{
                        userAppInfos.add(appinfo);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.INVISIBLE);
                        lv_app.setAdapter(new AppManagerAdapter());
                    }
                });
            }
        }.start();
    }
    //给界面上的ListView注册一个点击事件
    private void setAppInfoItemClickListener() {
        lv_app.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appinfo;
                if(position==0){//第0个位置是一个textview标签
                    return ;
                }else if(position==(userAppInfos.size()+1)){
                    return ;
                }else if(position<=userAppInfos.size()){//用户程序
                    int newPosition = position - 1;//减去用户的标签textview占据的位置
                    appinfo = userAppInfos.get(newPosition);
                }else{//系统程序
                    int newPosition = position - 1 - userAppInfos.size() - 1;
                    appinfo = systemAppInfos.get(newPosition);
                }
                TextView contentView = new TextView(AppManagerActivity.this);
                contentView.setText(appinfo.getPackName());
                contentView.setBackgroundColor(Color.GRAY);
                int[] location = new int[2];
                view.getLocationInWindow(location);
                if(popupWindow != null){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                popupWindow = new PopupWindow(contentView,-2,-2);
                popupWindow.showAtLocation(parent, Gravity.LEFT+Gravity.TOP,20,location[1]);
            }
        });
    }

    private class AppManagerAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            //两个text标签
            return 1+userAppInfos.size()+1+systemAppInfos.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appinfo;

            if(position==0){//第0个位置是一个textview标签
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("用户程序："+userAppInfos.size());
                return tv;
            }else if(position==(userAppInfos.size()+1)){
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("系统程序："+systemAppInfos.size());
                return tv;
            }else if(position<=userAppInfos.size()){//用户程序
                int newPosition = position - 1;//减去用户的标签textview占据的位置
                appinfo = userAppInfos.get(newPosition);
            }else{//系统程序
                int newPosition = position - 1 - userAppInfos.size() - 1;
                appinfo = systemAppInfos.get(newPosition);
            }

            View view;
            ViewHolder viewHolder;
            if(convertView != null&&convertView instanceof RelativeLayout) {//在复用历史缓存view对象的时候，不仅要检查是否为空，还要检查类型是否为相对布局
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(AppManagerActivity.this, R.layout.item_appinfo, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
                viewHolder.tv_appname = (TextView) view.findViewById(R.id.tv_appname);
                viewHolder.tv_appsize = (TextView) view.findViewById(R.id.tv_appsize);
                viewHolder.iv_install_location = (ImageView) view.findViewById(R.id.iv_install_location);
                view.setTag(viewHolder);
            }
            viewHolder.iv_appicon.setImageDrawable(appinfo.getAppIcon());
            viewHolder.tv_appname.setText(appinfo.getAppName());
            viewHolder.tv_appsize.setText("程序大小：" + Formatter.formatFileSize(AppManagerActivity.this,appinfo.getAppSize()));
            if(appinfo.isInRom()){
                viewHolder.iv_install_location.setImageResource(R.drawable.memory);
            }else{
                viewHolder.iv_install_location.setImageResource(R.drawable.sd);
            }
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
        ImageView iv_appicon,iv_install_location;
        TextView tv_appname,tv_appsize;
    }
}
