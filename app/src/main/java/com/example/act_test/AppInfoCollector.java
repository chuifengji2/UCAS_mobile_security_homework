package com.example.act_test;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.util.Date;
import java.util.List;

public class AppInfoCollector {
    private Context mContext;

    public AppInfoCollector(Context context) {
        mContext = context;
    }

    public String getAppInfo() {
        String info = "";
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : apps) {
            String result = "";

            // Drawable icon = app.loadIcon(pm);
            String label = app.loadLabel(pm).toString();
            result += "应用程序名称: " + label + "\n";

            String packageName = app.packageName;
            String versionName = "";
            int versionCode = 0;
            try {
                versionName = pm.getPackageInfo(packageName, 0).versionName;
                versionCode = pm.getPackageInfo(packageName, 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            result += "应用程序包名: " + packageName + "\n";
            result += "应用程序版本号: " + versionCode + "\n";
            result += "应用程序版本名称: " + versionName + "\n";

            // 获取应用程序安装时间和更新时间
            long installTime = 0;
            long updateTime = 0;
            try {
                installTime = pm.getPackageInfo(packageName, 0).firstInstallTime;
                updateTime = pm.getPackageInfo(packageName, 0).lastUpdateTime;
                result += "应用程序安装时间: " + new Date(installTime) + "\n";
                result += "应用程序更新时间: " + new Date(updateTime) + "\n";
            } catch (PackageManager.NameNotFoundException e) {
                result += "应用程序安装时间、更新时间获取失败: " + e.getMessage() + "\n";
            }

            // 获取应用程序权限和签名信息
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS | PackageManager.GET_SIGNATURES);
                String[] permissions = packageInfo.requestedPermissions;
                Signature[] signatures = packageInfo.signatures;
                if (permissions != null && permissions.length > 0) {
                    result += "应用程序权限: " + "\n";
                    for (String permission : permissions) {
                        result += "   " + permission + "\n";
                    }
                }
                if (signatures != null && signatures.length > 0) {
                    result += "应用程序签名信息: " + "\n";
                    for (Signature signature : signatures) {
                        result += "   " + signature.toCharsString() + "\n";
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                result += "应用程序权限、签名信息获取失败: " + e.getMessage() + "\n";
            }
            result += "\n";
            info += result;
        }
        return info;
    }
}
