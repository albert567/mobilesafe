package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.ProcessInfo;
import com.itheima.mobilesafe.engine.ProcessInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 进程管理ui界面
 */
public class ProcessManagerActivity extends Activity {
    /**
     * 进程数量
     */
    private TextView tv_process_count;
    /**
     * 内存状态
     */
    private TextView tv_memory_status;
    /**
     * 进程列表
     */
    private ListView lv_processinfos;
    /**
     * 正在加载布局
     */
    private LinearLayout ll_loading;
    /**
     * 进程集合
     */
    private List<ProcessInfo> processInfos;
    /**
     * 用户应用集合
     */
    private List<ProcessInfo> userInfos;
    /**
     * 系统应用结合
     */
    private List<ProcessInfo> systemInfos;

    private ProcessInfoAdapter adapter;
    /**
     * 记录正在运行的进程数量
     */
    private int runningProcessCount;
    /**
     * 记录剩余的内存空间
     */
    private long availMem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        tv_memory_status = (TextView) findViewById(R.id.tv_memory_status);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        fillData();
        lv_processinfos = (ListView)findViewById(R.id.lv_processinfos);
        //给ListView注册条目点击事件
        lv_processinfos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //得到ListView某个位置的对象
                Object obj = lv_processinfos.getItemAtPosition(position);
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
                if(obj != null){
                    ProcessInfo info = (ProcessInfo)obj;
                    if(info.getPackName().equals(getPackageName())){
                        return;
                    }
                    if(info.isChecked()){
                        //取消checkbox的勾选
                        cb.setChecked(false);
                        info.setChecked(false);
                    }else{
                        //勾选checkbox
                        cb.setChecked(true);
                        info.setChecked(true);
                    }
                }
            }
        });
    }

    /**
     * 获取正在运行的进程的数量
     * @return
     */
    private int getRunningProcessCount(){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        return am.getRunningAppProcesses().size();
    }
    /**
     * 获取手机可用的内存空间
     * @return
     */
    private long getAvailMemory(){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);//获取系统当前的内存信息
        return memoryInfo.availMem;
    }
    /**
     * 获取手机总的内存空间
     * @return
     */
    private long getTotalMemory(){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);//获取系统当前的内存信息
        return memoryInfo.totalMem;
    }

    /**
     * 清理掉所有的选中的进程
     * @param view
     */
    public void killSelected(View view){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ProcessInfo> killedProcessInfos = new ArrayList<ProcessInfo>();
        for(ProcessInfo info:userInfos){
            if(info.isChecked()){
                am.killBackgroundProcesses(info.getPackName());
                killedProcessInfos.add(info);
            }
        }
        for(ProcessInfo info:systemInfos){
            if(info.isChecked()){
                am.killBackgroundProcesses(info.getPackName());
                killedProcessInfos.add(info);

            }
        }

        //进程清理完毕，刷新listview的界面
        //fillData();
        long total = 0;
        for(ProcessInfo info : killedProcessInfos){
            total += info.getMemSize();
            if(info.isUserProcess()){
                userInfos.remove(info);
            }else{
                systemInfos.remove(info);
            }
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "清理了" + killedProcessInfos.size()
                + "个进程，释放了" + Formatter.formatFileSize(this,total)
                + "的内存", Toast.LENGTH_SHORT).show();
        runningProcessCount -= killedProcessInfos.size();
        tv_process_count.setText("运行中进程："+runningProcessCount+"个");
        availMem += total;
        tv_memory_status.setText("可用内存："+ Formatter.formatFileSize(this,availMem));
    }

    /**
     * 一键全选
     * @param view
     */
    public void selectAll(View view){
        for(ProcessInfo info : userInfos){
            if(info.getPackName().equals(getPackageName())){
                continue;
            }
            info.setChecked(true);
        }
        for(ProcessInfo info : systemInfos){
            info.setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }
    /**
     * 一键全选
     * @param view
     */
    public void selectOther(View view){
        for(ProcessInfo info : userInfos){
            if(info.getPackName().equals(getPackageName())){
                continue;
            }
            info.setChecked(!info.isChecked());
        }
        for(ProcessInfo info : systemInfos){
            info.setChecked(!info.isChecked());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 填充数据
     */
    private void fillData(){
        runningProcessCount = getRunningProcessCount();
        tv_process_count.setText("运行中进程："+runningProcessCount+"个");
        availMem = getAvailMemory();
        tv_memory_status.setText("可用内存："+ Formatter.formatFileSize(this,availMem));
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                processInfos = ProcessInfoProvider.getRunningProcessInfos(ProcessManagerActivity.this);
                userInfos = new ArrayList<ProcessInfo>();
                systemInfos = new ArrayList<ProcessInfo>();
                for(ProcessInfo info : processInfos){
                    if(info.isUserProcess()){
                        userInfos.add(info);
                    }else{
                        systemInfos.add(info);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.INVISIBLE);
                        adapter = new ProcessInfoAdapter();
                        lv_processinfos.setAdapter(adapter);
                    }
                });
            }
        }.start();
    }

    private class ProcessInfoAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 1 + userInfos.size() + 1 + systemInfos.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProcessInfo info;
            if(position == 0){
                //用户应用标签
                TextView tv = new TextView(ProcessManagerActivity.this);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("用户进程：" + userInfos.size() + "个");
                return tv;
            }else if(position <= userInfos.size()){
                //用户应用集合
                info = userInfos.get(position - 1);
            }else if(position == userInfos.size() + 1){
                //系统应用标签
                TextView tv = new TextView(ProcessManagerActivity.this);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("系统进程：" + systemInfos.size() + "个");
                return tv;
            }else{
                //系统应用集合
                info = systemInfos.get(position - 1 - userInfos.size() - 1);
            }
            View view;
            ViewHolder holder;
            if(convertView != null&& convertView instanceof RelativeLayout){
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(ProcessManagerActivity.this,R.layout.item_process_item,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_process_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_process_name);
                holder.tv_mem = (TextView) view.findViewById(R.id.tv_process_memsize);
                holder.cb = (CheckBox) view.findViewById(R.id.cb);
                view.setTag(holder);
            }
            if(info.getPackName().equals(getPackageName())){
                holder.cb.setVisibility(View.INVISIBLE);
            }
            holder.iv_icon.setImageDrawable(info.getAppIcon());
            holder.tv_name.setText(info.getAppName());
            holder.tv_mem.setText("占用内存：" + Formatter.formatFileSize(getApplicationContext(),info.getMemSize()));
            //通过item里面保存的状态，更新checkbox状态
            holder.cb.setChecked(info.isChecked());
            return view;
        }

        @Override
        public Object getItem(int position) {
            ProcessInfo info;
            if(position == 0){
                //用户应用标签
                return null;
            }else if(position <= userInfos.size()){
                //用户应用集合
                info = userInfos.get(position - 1);
            }else if(position == userInfos.size() + 1){
                //系统应用标签
                return null;
            }else{
                //系统应用集合
                info = systemInfos.get(position - 1 - userInfos.size() - 1);
            }
            return info;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_mem;
        CheckBox cb;
    }

}
