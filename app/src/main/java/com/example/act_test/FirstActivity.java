package com.example.act_test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button button1 = (Button) findViewById(R.id.button_1);
        Button button2 = (Button) findViewById(R.id.button_2);
        Button button3 = (Button) findViewById(R.id.button_3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "";
                info += "设备品牌: " + Build.BRAND + "\n";
                info += "设备名称: " + Build.DEVICE + "\n";
                info += "设备型号: " + Build.MODEL + "\n";
                info += "设备制造商和型号: " + Build.PRODUCT + "\n";
                info += "主板名称: " + Build.BOARD + "\n";
                info += "设备引导程序版本号: " + Build.BOOTLOADER + "\n";
                info += "设备显示的版本字符串: " + Build.DISPLAY + "\n";
                info += "设备的唯一标识: " + Build.FINGERPRINT + "\n";
                info += "设备版本号: " + Build.ID + "\n";
                info += "设备制造商: " + Build.MANUFACTURER + "\n";
                info += "设备序列号: " + Build.SERIAL + "\n";
                info += "设备描述标签: " + Build.TAGS + "\n";
                info += "设备类型: " + Build.TYPE + "\n";
                info += "设备所有者的名称: " + Build.USER + "\n";
                TextView textView = new TextView(FirstActivity.this);
                textView.setText(info);
                setContentView(textView);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "";
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                for (ApplicationInfo app : apps) {
                    String appName = app.loadLabel(pm).toString();
                    String packageName = app.packageName;
                    String versionName = "";
                    int versionCode = 0;
                    try {
                        PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                        versionName = packageInfo.versionName;
                        versionCode = packageInfo.versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    info += "App Name: " + appName + ", Package Name: " + packageName + ", Version Name: " + versionName + ", Version Code: " + versionCode + "\n" + "\n";
                }

                ScrollView scrollView = new ScrollView(FirstActivity.this);

                LinearLayout linearLayout = new LinearLayout(FirstActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                TextView textView = new TextView(FirstActivity.this);
                textView.setText(info);
                textView.setTextSize(20);
                linearLayout.addView(textView);

                scrollView.addView(linearLayout);
                setContentView(scrollView);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "";
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId = tm.getDeviceId();
                String phoneNumber = tm.getLine1Number();
                info += "deviceId:" + deviceId +"\n";
                info += "phoneNumber:" + phoneNumber +"\n";
                TextView textView = new TextView(FirstActivity.this);
                textView.setText(info);
                setContentView(textView);
            }
        });
    }
}