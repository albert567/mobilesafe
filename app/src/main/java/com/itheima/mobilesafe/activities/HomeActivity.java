package com.itheima.mobilesafe.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.ui.Md5Utils;

public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";
    private SharedPreferences sp;
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

        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        iv_home_logo = (ImageView)findViewById(R.id.iv_home_logo);
        ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo,"rotationY",45,90,135,180,225,270,315);
        oa.setDuration(3000);
        oa.setRepeatCount(ObjectAnimator.INFINITE);
        oa.setRepeatMode(ObjectAnimator.RESTART);
        oa.start();

        gv_home_item = (GridView)findViewById(R.id.gv_home_item);
        gv_home_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position){
                    case 0://手机防盗
                        Log.i(TAG,"进入手机防盗");
                        if(isSetupPwd()){
                            Log.i(TAG,"弹出输入密码的界面");
                            showEnterPwdDialog();
                        }else{
                            Log.i(TAG,"弹出设置密码的界面");
                            showSetupPwdDialog();
                        }
                        break;
                    case 1://骚扰拦截
                        intent = new Intent(HomeActivity.this,CallSmsSafeActivity.class);
                        startActivity(intent);
                        break;
                    case 2://软件管家
                        intent = new Intent(HomeActivity.this,AppManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 3://进程管理器
                        intent = new Intent(HomeActivity.this,ProcessManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 4://流量统计
                        intent = new Intent(HomeActivity.this,TrafficManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 7://常用工具
                        intent = new Intent(HomeActivity.this,CommonToolsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        gv_home_item.setAdapter(new HomeAdapter());
    }

    /**
     * 点击进入设置页面
     * @param view
     */
    public void enterSettingActivity(View view){
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    private AlertDialog dialog;
    /**
     * 显示设置密码对话框,自定义对话框
     */
    protected void showSetupPwdDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.dialog_setup_pwd,null);
        builder.setView(view);
        final EditText et_dialog_pwd = (EditText)view.findViewById(R.id.et_dialog_pwd);
        final EditText et_dialog_pwd_confirm = (EditText)view.findViewById(R.id.et_dialog_pwd_confirm);
        Button bt_dialog_ok = (Button)view.findViewById(R.id.bt_dialog_ok);
        bt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_dialog_pwd.getText().toString().trim();
                String pwd_confirm = et_dialog_pwd_confirm.getText().toString().trim();

                if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwd_confirm)){
                    Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pwd.equals(pwd_confirm)){
                    Toast.makeText(HomeActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("pwd", Md5Utils.encode(pwd));
                editor.commit();
                //关闭对话框
                dialog.dismiss();
                //弹出输入密码对话框
                showEnterPwdDialog();
            }
        });
        Button bt_dialog_cancel = (Button)view.findViewById(R.id.bt_dialog_cancel);
        bt_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //显示对话框,把对话框的引用赋给类的成员变量
        dialog = builder.show();
    }

    protected void showEnterPwdDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.dialog_enter_pwd,null);
        builder.setView(view);
        final EditText et_dialog_pwd = (EditText)view.findViewById(R.id.et_dialog_pwd);
        Button bt_dialog_ok = (Button)view.findViewById(R.id.bt_dialog_ok);
        bt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_dialog_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Md5Utils.encode(pwd).equals(sp.getString("pwd",""))){
                    dialog.dismiss();
                    //Toast.makeText(HomeActivity.this, "密码输入正确,进入手机防盗界面", Toast.LENGTH_SHORT).show();
                    //判断用户是否进入过设置向导界面,如果用户是第一次使用手机防盗功能,定向页面到设置向导
                    boolean configed = sp.getBoolean("configed",false);
                    if(configed){
                        Log.i(TAG,"用户完成过设置向导,进入手机防盗的ui界面");
                        Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
                        startActivity(intent);
                    }else{
                        Log.i(TAG,"用户没有完成过设置向导,进入设置向导界面");
                        Intent intent = new Intent(HomeActivity.this,Setup1Activity.class);
                        startActivity(intent);
                    }


                }else{
                    Toast.makeText(HomeActivity.this, "密码输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button bt_dialog_cancel = (Button)view.findViewById(R.id.bt_dialog_cancel);
        bt_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.show();
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

    /**
     * 判断用户是否设置过密码
     * @return
     */
    private boolean isSetupPwd(){
        String pwd = sp.getString("pwd","");
        return TextUtils.isEmpty(pwd)?false:true;
    }

}
