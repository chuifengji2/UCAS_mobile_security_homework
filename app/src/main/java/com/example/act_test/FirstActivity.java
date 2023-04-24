package com.example.act_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button btn_sysinfo = (Button) findViewById(R.id.btn_sysinfo);
        Button btn_appinfo = (Button) findViewById(R.id.btn_appinfo);
        Button btn_userinfo = (Button) findViewById(R.id.btn_userinfo);
        Button btn_sms = (Button) findViewById(R.id.btn_sms);
        Button btn_contacts = (Button) findViewById(R.id.btn_contacts);
        Button btn_callhist = (Button) findViewById(R.id.btn_callhist);

        LinearLayout btnsLayout = (LinearLayout) findViewById(R.id.btnsLayout);

        TextView txt_sysinfo = new TextView(FirstActivity.this);
        txt_sysinfo.setTextSize(16);
        txt_sysinfo.setVisibility(View.GONE);

        TextView txt_appinfo = new TextView(FirstActivity.this);
        txt_appinfo.setTextSize(16);
        txt_appinfo.setVisibility(View.GONE);

        TextView txt_userinfo = new TextView(FirstActivity.this);
        txt_userinfo.setTextSize(16);
        txt_userinfo.setVisibility(View.GONE);

        TextView txt_sms = new TextView(FirstActivity.this);
        txt_sms.setTextSize(16);
        txt_sms.setVisibility(View.GONE);

        TextView txt_contacts = new TextView(FirstActivity.this);
        txt_contacts.setTextSize(16);
        txt_contacts.setVisibility(View.GONE);

        TextView txt_callhist = new TextView(FirstActivity.this);
        txt_callhist.setTextSize(16);
        txt_callhist.setVisibility(View.GONE);

        LinearLayout linear_sysinfo = new LinearLayout(FirstActivity.this);
        linear_sysinfo.setOrientation(LinearLayout.VERTICAL);
        linear_sysinfo.addView(txt_sysinfo);
        ScrollView scroll_sysinfo = new ScrollView(FirstActivity.this);
        scroll_sysinfo.addView(linear_sysinfo);

        LinearLayout linear_appinfo = new LinearLayout(FirstActivity.this);
        linear_appinfo.setOrientation(LinearLayout.VERTICAL);
        linear_appinfo.addView(txt_appinfo);
        ScrollView scroll_appinfo = new ScrollView(FirstActivity.this);
        scroll_appinfo.addView(linear_appinfo);

        LinearLayout linear_sms = new LinearLayout(FirstActivity.this);
        linear_sms.setOrientation(LinearLayout.VERTICAL);
        linear_sms.addView(txt_sms);
        ScrollView scroll_sms = new ScrollView(FirstActivity.this);
        scroll_sms.addView(linear_sms);

        LinearLayout linear_contacts = new LinearLayout(FirstActivity.this);
        linear_contacts.setOrientation(LinearLayout.VERTICAL);
        linear_contacts.addView(txt_contacts);
        ScrollView scroll_contacts = new ScrollView(FirstActivity.this);
        scroll_contacts.addView(linear_contacts);

        LinearLayout linear_callhist = new LinearLayout(FirstActivity.this);
        linear_callhist.setOrientation(LinearLayout.VERTICAL);
        linear_callhist.addView(txt_callhist);
        ScrollView scroll_callhist = new ScrollView(FirstActivity.this);
        scroll_callhist.addView(linear_callhist);

        btnsLayout.addView(scroll_sysinfo, btnsLayout.indexOfChild(btn_sysinfo)+1);
        btnsLayout.addView(scroll_appinfo, btnsLayout.indexOfChild(btn_appinfo)+1);
        btnsLayout.addView(txt_userinfo, btnsLayout.indexOfChild(btn_userinfo)+1);
        btnsLayout.addView(scroll_sms, btnsLayout.indexOfChild(btn_sms)+1);
        btnsLayout.addView(scroll_contacts, btnsLayout.indexOfChild(btn_contacts)+1);
        btnsLayout.addView(scroll_callhist, btnsLayout.indexOfChild(btn_callhist)+1);

        SystemInfoCollector sysinfoCollector = new SystemInfoCollector(this);
        AppInfoCollector appinfoCollector = new AppInfoCollector(this);
        UserInfoCollector userInfoCollector = new UserInfoCollector(this);

        btn_sysinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String info = sysinfoCollector.getSystemInfo();

                if (txt_sysinfo.getVisibility() != View.VISIBLE) {
                    txt_sysinfo.setText(info);
                    txt_sysinfo.setVisibility(View.VISIBLE);
                } else {
                    txt_sysinfo.setVisibility(View.GONE);
                }
            }
        });

        btn_appinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = appinfoCollector.getAppInfo();

                if (txt_appinfo.getVisibility() != View.VISIBLE) {
                    txt_appinfo.setText(info);
                    txt_appinfo.setVisibility(View.VISIBLE);
                } else {
                    txt_appinfo.setVisibility(View.GONE);
                }

            }
        });

        btn_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = userInfoCollector.getTelephonyInfo();

                if (txt_userinfo.getVisibility() != View.VISIBLE) {
                    txt_userinfo.setText(info);
                    txt_userinfo.setVisibility(View.VISIBLE);
                } else {
                    txt_userinfo.setVisibility(View.GONE);
                }

            }
        });
        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = userInfoCollector.getSMSInfo();

                if (txt_sms.getVisibility() != View.VISIBLE) {
                    txt_sms.setText(info);
                    txt_sms.setVisibility(View.VISIBLE);
                } else {
                    txt_sms.setVisibility(View.GONE);
                }

            }
        });
        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = userInfoCollector.getContactsInfo();

                if (txt_contacts.getVisibility() != View.VISIBLE) {
                    txt_contacts.setText(info);
                    txt_contacts.setVisibility(View.VISIBLE);
                } else {
                    txt_contacts.setVisibility(View.GONE);
                }

            }
        });
        btn_callhist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = userInfoCollector.getCallhistInfo();

                if (txt_callhist.getVisibility() != View.VISIBLE) {
                    txt_callhist.setText(info);
                    txt_callhist.setVisibility(View.VISIBLE);
                } else {
                    txt_callhist.setVisibility(View.GONE);
                }

            }
        });
    }
}