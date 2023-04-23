package com.example.act_test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class SystemInfoCollector {
    private Context mContext;

    public SystemInfoCollector(Context context) {
        mContext = context;
    }

    public String getSystemInfo() {
        String info = "";

        info += "设备名称: " + Build.DEVICE + "\n";
        info += "设备制造商: " + Build.MANUFACTURER + "\n";
        info += "设备型号: " + Build.MODEL + "\n";
        info += "设备品牌: " + Build.BRAND + "\n";
        info += "设备名称: " + Build.DEVICE + "\n";
        info += "产品名称: " + Build.PRODUCT + "\n";
        info += "硬件名称: " + Build.HARDWARE + "\n";
        info += "CPU ABI: " + Build.CPU_ABI + "\n";
        info += "CPU ABI2: " + Build.CPU_ABI2 + "\n";
        info += "主板名称: " + Build.BOARD + "\n";
        info += "设备引导程序版本号: " + Build.BOOTLOADER + "\n";
        info += "设备显示的版本字符串: " + Build.DISPLAY + "\n";
        info += "设备的唯一标识: " + Build.FINGERPRINT + "\n";
        info += "无线电固件版本: " + Build.getRadioVersion() + "\n";
        info += "设备版本号: " + Build.ID + "\n";
        info += "设备序列号: " + Build.SERIAL + "\n";
        info += "设备描述标签: " + Build.TAGS + "\n";
        info += "设备类型: " + Build.TYPE + "\n";
        info += "设备所有者名称: " + Build.USER + "\n";
        info += "编译时间: " + Build.TIME + "\n";
        info += "操作系统版本号: " + Build.VERSION.SDK_INT + "\n";
        info += "操作系统版本名称: " + Build.VERSION.RELEASE + "\n";
        info += "屏幕分辨率: " + getScreenResolution() + "\n";
        info += "可用存储空间: " + getAvailableStorage() + "\n";
        info += "网络连接类型: " + getNetworkType() + "\n";
        info += "IP地址: " + getIPAddress() + "\n";
        info += "MAC地址: " + getMACAddress() + "\n";
        info += getBatteryInfo();
        info += getLocationInfo();
        info += getSensorInfo();
        info += getStorageInfo();

        return info;
    }

    public String getScreenResolution() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return width + "x" + height;
    }

    public String getAvailableStorage() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        long megabytesAvailable = bytesAvailable / (1024 * 1024);
        return megabytesAvailable + "MB";
    }

    public String getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return "Error";
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            int type = activeNetwork.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return "Wi-Fi";
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "无权限";
                }
                int networkType = tm.getNetworkType();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                    case TelephonyManager.NETWORK_TYPE_GSM:
                        return "2G";
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                        return "3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                        return "4G";
                    case TelephonyManager.NETWORK_TYPE_NR:
                        return "5G";
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        return "未知";
                }
            }
        }
        return "未知";
    }

    private String getIPAddress() {
        // 权限：INTERNET、ACCESS_NETWORK_STATE
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : interfaces) {
                for (InetAddress addr : Collections.list(ni.getInetAddresses())) {
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            return "未知: " + e.getMessage();
        }
        return "未知";
    }

    public String getMACAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : interfaces) {
                if (ni.getName().equalsIgnoreCase("wlan0")) {
                    byte[] mac = ni.getHardwareAddress();
                    if (mac == null) {
                        return "未知";
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte aMac : mac) {
                        buf.append(String.format("%02X:", aMac));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString();
                }
            }
        } catch (Exception e) {
            return "未知: " + e.getMessage();
        }
        return "未知";
    }

    public String getBatteryInfo() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale;

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        String info = "";
        info += "电量: " + (int) (batteryPct * 100) + "%\n";
        info += "是否充电: " + (isCharging ? "是" : "否") + "\n";
        info += "USB端口充电中: " + (usbCharge ? "是" : "否") + "\n";
        info += "电源充电中: " + (acCharge ? "是" : "否") + "\n";
        return info;
    }

    public String getLocationInfo() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        String info = "";
        // 获取 GPS 位置信息
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //需要权限 ACCESS_FINE_LOCATION
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                info += "GPS位置信息: 无权限\n";
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                info += "GPS位置信息: \n";
                info += "纬度: " + lastKnownLocation.getLatitude() + "\n";
                info += "经度: " + lastKnownLocation.getLongitude() + "\n";
            }
        }else{
            info += "GPS位置信息: 无\n";
        }

        // 获取网络位置信息
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            info += "网络位置信息: \n";
            info += "纬度: " + lastKnownLocation.getLatitude() + "\n";
            info += "经度: " + lastKnownLocation.getLongitude() + "\n";
        }else{
            info += "网络位置信息: 无\n";
        }
        return info;
    }

    public String getSensorInfo(){
        String info = "";
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(android.hardware.SensorEvent event) {
                // 这里可以更新传感器信息
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            info += "加速度传感器: \n";
            info += "分辨率: " + accelerometer.getResolution() + "\n";
            info += "最大范围: " + accelerometer.getMaximumRange() + "\n";
        }

        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscope != null) {
            sensorManager.registerListener(sensorEventListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            info += "陀螺仪传感器: \n";
            info += "分辨率: " + gyroscope.getResolution() + "\n";
            info += "最大范围: " + gyroscope.getMaximumRange() + "\n";
        }

        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(sensorEventListener, magneticField, SensorManager.SENSOR_DELAY_NORMAL);
            info += "磁场传感器: \n";
            info += "分辨率: " + magneticField.getResolution() + "\n";
            info += "最大范围: " + magneticField.getMaximumRange() + "\n";
        }

        return info;
    }

    public String getStorageInfo(){
        String info = "";
        File externalStorage = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(externalStorage.getAbsolutePath());

        long blockSize = statFs.getBlockSizeLong();
        long totalBlocks = statFs.getBlockCountLong();
        long availableBlocks = statFs.getAvailableBlocksLong();

        info += "存储信息: \n";
        info += "总容量: " + blockSize * totalBlocks / 1024 / 1024 + " MB\n";
        info += "可用容量: " + blockSize * availableBlocks / 1024 / 1024 + " MB\n";
        return info;
    }

}
