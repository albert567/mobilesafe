package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.SyncStatusObserver;
import android.net.TrafficStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

import java.sql.SQLOutput;

public class TrafficManagerActivity extends Activity {
    private TextView tv_total_traffic;
    private TextView tv_mobile_traffic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_manager);
        tv_total_traffic = (TextView) findViewById(R.id.tv_total_traffic);
        tv_mobile_traffic = (TextView) findViewById(R.id.tv_mobile_traffic);
        //获取全部的translate的byte，获取全部上传的流量
        long totalTx = TrafficStats.getTotalTxBytes();
        //获取全部的receive的byte，获取全部的下载的流量
        long totalRx = TrafficStats.getTotalRxBytes();
        long total = totalTx + totalRx;
        tv_total_traffic.setText(Formatter.formatFileSize(this,total));
        //获取mobile的translate的byte，获取全部上传的流量
        long mobileTotalTx = TrafficStats.getMobileTxBytes();
        //获取mobile的receive的byte，获取全部的下载的流量
        long mobileTotalRx = TrafficStats.getMobileRxBytes();
        long mobileTotal = mobileTotalTx + mobileTotalRx;
        tv_mobile_traffic.setText(Formatter.formatFileSize(this,mobileTotal));

        //根据uid获取应用程序的流量数据
        long rx = TrafficStats.getUidRxBytes(10078);
        long tx = TrafficStats.getUidTxBytes(10078);
        System.out.println("下载：" + Formatter.formatFileSize(this,rx));
        System.out.println("上传：" + Formatter.formatFileSize(this,tx));
    }
}
