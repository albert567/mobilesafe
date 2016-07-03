package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.Dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import java.util.List;

public class CallSmsSafeActivity extends Activity {
    private BlackNumberDao dao;
    private List<BlackNumberInfo> infos;
    private CallSmsSafeAdapter adapter;

    private ListView lv_callsms_safe;
    private ImageView iv_callsms_safe_empty;
    private LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsms_safe);

        lv_callsms_safe = (ListView)findViewById(R.id.lv_callsms_safe);
        iv_callsms_safe_empty = (ImageView) findViewById(R.id.iv_callsms_safe_empty);
        loading = (LinearLayout) findViewById(R.id.loading);
        //获取全部的黑名单号码
        dao = new BlackNumberDao(this);
        loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                infos = dao.findAll();
                adapter = new CallSmsSafeAdapter();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv_callsms_safe.setAdapter(adapter);
                        loading.setVisibility(View.INVISIBLE);
                    }
                });

            }
        }.start();

    }
    private class CallSmsSafeAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            int size = infos.size();
            if(size>0){
                //ListView里有数据 隐藏图片
                iv_callsms_safe_empty.setVisibility(View.INVISIBLE);
            }else{
                //ListView里面没有数据 显示图片
                iv_callsms_safe_empty.setVisibility(View.VISIBLE);
            }
            return size;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //复用历史缓存的view对象，减少view对象创建的格式
            View view = convertView;
            ViewHolder viewHolder;
            final BlackNumberInfo info = infos.get(position);
            if(view==null){
                view = View.inflate(CallSmsSafeActivity.this,R.layout.item_call_smssafe,null);
                //查找自孩子会比较消耗时间，需要对下面的代码进行优化
                //相当于姓名本，用来记录孩子的引用
                viewHolder = new ViewHolder();
                viewHolder.tv_item_blacknumber = (TextView) view.findViewById(R.id.tv_item_blacknumber);
                viewHolder.tv_item_mode = (TextView) view.findViewById(R.id.tv_item_mode);
                viewHolder.iv_item_delete = (ImageView) view.findViewById(R.id.iv_item_delete);
                viewHolder.iv_item_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从数据库把记录给移除
                        boolean result = dao.delete(info.getPhone());
                        if(result){
                            Toast.makeText(CallSmsSafeActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                            //更新UI界面
                            infos = dao.findAll();
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(CallSmsSafeActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            String mode = info.getMode();//1电话 2短信 3全部拦截
            viewHolder.tv_item_blacknumber.setText(info.getPhone());
            if("1".equals(mode)){
                viewHolder.tv_item_mode.setText("电话拦截");
            }else if("2".equals(mode)){
                viewHolder.tv_item_mode.setText("短信拦截");
            }else{
                viewHolder.tv_item_mode.setText("全部拦截");
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

    /**
     * view 的容器，用来存储自孩子的引用
     */
    class ViewHolder{
        private TextView tv_item_blacknumber;
        private TextView tv_item_mode;
        private ImageView iv_item_delete;
    }

    /**
     * 添加黑名单号码
     * @param view
     */
    public void addBlackNumber(View view){
        Intent intent = new Intent(this,AddBlackNumberActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            boolean flag = data.getBooleanExtra("flag",false);
            if(flag){
                //重新获取所有黑名单号码
                //infos = dao.findAll();
                String phone = data.getStringExtra("phone");
                String mode = data.getStringExtra("mode");
                BlackNumberInfo info = new BlackNumberInfo();
                info.setPhone(phone);
                info.setMode(mode);
                infos.add(info);//把新添加的数据直接加入到集合里面就可以了，避免了重新查询数据库，应用程序的效率得到了提高
                //通知ListView刷新界面
                adapter.notifyDataSetChanged();
                Toast.makeText(CallSmsSafeActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(CallSmsSafeActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
