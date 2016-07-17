package com.itheima.mobilesafe.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zyp on 2016/7/17.
 */
public class CleanCacheFragment extends Fragment{
    private static final int SCANING = 1;
    private static final int SCAN_FINISH = 2;
    private static final int FOUND_CACHE = 3;//发现缓存
    private ProgressBar pb;
    private TextView tv_scan_status;
    private LinearLayout ll_container;
    private Button bt_clean_all;
    private PackageManager pm;
    private Method myMethod;
    private Method cleanMethod;
    private Method freeMethod;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SCANING:
                    String name = (String)msg.obj;
                    tv_scan_status.setText("正在扫描：" + name);
                    break;
                case SCAN_FINISH:
                    tv_scan_status.setText("扫描完毕");
                    break;
                case FOUND_CACHE://发现缓存
                    final CacheInfo cacheInfo = (CacheInfo)msg.obj;
                    /*TextView tv = new TextView(getActivity());
                    tv.setText(cacheInfo.appName+"--"+cacheInfo.cacheSize);*/
                    View view = View.inflate(getActivity(),R.layout.item_cache_info,null);
                    view.setBackgroundResource(R.drawable.dg_cancel_selector);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //自己无法清理缓存
                            //deleteApplicationCacheFiles(cacheInfo.packName);
                            showApplicationInfo(cacheInfo.packName);
                        }
                    });
                    ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                    iv_icon.setImageDrawable(cacheInfo.icon);
                    TextView tv_appname = (TextView) view.findViewById(R.id.tv_appname);
                    tv_appname.setText(cacheInfo.appName);
                    TextView tv_cache_size = (TextView) view.findViewById(R.id.tv_cache_size);
                    tv_cache_size.setText(Formatter.formatFileSize(getActivity(),cacheInfo.cacheSize));
                    ll_container.addView(view,0);
                    break;
            }
        }
    };

    //创建fragment显示内容的方法
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_clean_cache,null);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        tv_scan_status = (TextView) view.findViewById(R.id.tv_scan_status);
        ll_container = (LinearLayout) view.findViewById(R.id.ll_container);
        bt_clean_all = (Button) view.findViewById(R.id.bt_clean_all);
        bt_clean_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeStorageAndNotify(Integer.MAX_VALUE);
            }
        });
        pm = getActivity().getPackageManager();
        return view;
    }

    /**
     * 界面可用调用的方法
     */
    @Override
    public void onStart() {
        super.onStart();
        //清空容器，重新加载
        ll_container.removeAllViews();
        new Thread(){
            @Override
            public void run() {
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                pb.setMax(infos.size());
                int progress = 0;
                for(PackageInfo info:infos){
                    String packname = info.packageName;
                    getPackSizeInfo(packname);
                    SystemClock.sleep(50);
                    progress ++;
                    pb.setProgress(progress);
                    String name = info.applicationInfo.loadLabel(pm).toString();
                    Message msg = handler.obtainMessage();
                    msg.what = SCANING;
                    msg.obj = name;
                    handler.sendMessage(msg);
                }
                Message msg = handler.obtainMessage();
                msg.what = SCAN_FINISH;
                handler.sendMessage(msg);
            }
        }.start();

    }
    /**
     * 利用反射申请释放内存
     * @param freeStorageSize
     */
    private void freeStorageAndNotify(long freeStorageSize){
        if(freeMethod == null) {
            //根据包名利用反射获取缓存信息
            Method[] methods = PackageManager.class.getDeclaredMethods();
            for (Method method : methods) {
                if ("freeStorageAndNotify".equals(method.getName())&&method.getParameterTypes().length==2) {
                    myMethod = method;
                    break;
                }
            }
        }
        try {
            freeMethod.invoke(pm, freeStorageSize, new IPackageDataObserver.Stub(){
                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
        SystemClock.sleep(2000);
        Toast.makeText(getActivity(), "清理完毕", Toast.LENGTH_SHORT).show();
        tv_scan_status.setText("系统已经优化完毕，没有缓存文件存在了");
        ll_container.removeAllViews();
    }

    /**
     * 利用反射清除缓存
     * @param packname
     */
    private void deleteApplicationCacheFiles(final String packname){
        if(cleanMethod == null) {
            //根据包名利用反射获取缓存信息
            Method[] methods = PackageManager.class.getDeclaredMethods();
            for (Method method : methods) {
                if ("deleteApplicationCacheFiles".equals(method.getName())) {
                    cleanMethod = method;
                    break;
                }
            }
        }
        try {
            cleanMethod.invoke(pm, packname, new IPackageDataObserver.Stub(){

                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                    if(succeeded){
                        Toast.makeText(getActivity(), "清理成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "清理失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用反射获取包的大小信息
     * @param packname
     */
    private void getPackSizeInfo(final String packname){
        if(myMethod == null) {
            //根据包名利用反射获取缓存信息
            Method[] methods = PackageManager.class.getDeclaredMethods();
            for (Method method : methods) {
                if ("getPackageSizeInfo".equals(method.getName())&&method.getParameterTypes().length==2) {
                    myMethod = method;
                    break;
                }
            }
        }
        try {
            myMethod.invoke(pm, packname, new IPackageStatsObserver.Stub() {
                @Override
                public void onGetStatsCompleted(PackageStats pStats,
                                                boolean succeeded) throws RemoteException {
                    long cacheSize = pStats.cacheSize;
                    if(cacheSize>0){
                        CacheInfo cacheInfo = new CacheInfo();
                        cacheInfo.packName = packname;
                        try {
                            cacheInfo.appName = pm.getApplicationInfo(packname,0).loadLabel(pm).toString();
                            cacheInfo.icon = pm.getApplicationIcon(packname);

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        cacheInfo.cacheSize = cacheSize;
                        Message msg = handler.obtainMessage();
                        msg.what = FOUND_CACHE;
                        msg.obj = cacheInfo;
                        handler.sendMessage(msg);
                    }
                    //System.out.println(packname+"---缓存大小："+cacheSize);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    class CacheInfo{
        String packName;
        String appName;
        long cacheSize;
        Drawable icon;
    }

    /**
     * 显示应用程序详细信息
     */
    private void showApplicationInfo(String packname) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+packname));
        startActivity(intent);
    }
}
