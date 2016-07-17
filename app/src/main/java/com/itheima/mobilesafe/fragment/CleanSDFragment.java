package com.itheima.mobilesafe.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.mobilesafe.R;

import java.io.File;

/**
 * Created by zyp on 2016/7/17.
 */
public class CleanSDFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_clean_sd,null);
    }

    @Override
    public void onStart() {
        super.onStart();
        /*File file = Environment.getExternalStorageDirectory();
        File[] files = file.listFiles();
        for(File f : files){
            if(f.isFile()){//文件
                //.tmp .temp 缓存文件
            }else{

            }
        }*/
    }
}
