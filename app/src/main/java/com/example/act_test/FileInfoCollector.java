package com.example.act_test;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.database.Cursor;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Date;
import java.text.SimpleDateFormat;

public class FileInfoCollector {
    private Context mContext;

    public FileInfoCollector(Context context) {
        mContext = context;
    }

    public String getpicInfo(){
        String info = "";
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有获取，请求用户授权
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return "没有权限，请求用户授权";
        }
        // 获取存储卡目录
        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        // 查询相册中的图片信息
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
        };
        String selection = MediaStore.Images.Media.DATA + " like ?";
        String[] selectionArgs = { storageDir + "/DCIM/Camera/%" };
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        Cursor cursor = null;

        try {
            cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, selection, selectionArgs, sortOrder);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    long dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

                    info += "照片名称：" + name + "\n";
                    info += "照片路径：" + path + "\n";
                    info += "拍摄时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateAdded * 1000)) + "\n\n";
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            info += "读取照片信息失败：" + e.getMessage() + "\n\n";
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return info;

    }
}
