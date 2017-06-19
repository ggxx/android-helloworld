package com.plagh.ggxx.helloworld.call;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;

import com.plagh.ggxx.helloworld.util.PermissionsChecker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ggxx on 2017/6/18.
 */

public class CallRecord {

    private String name;
    private String number;
    private String date;
    private String type;
    private int duration;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public static List<CallRecord> listAll(Context context) {

        List<CallRecord> list = new ArrayList<CallRecord>();

        // 1.获得ContentResolver
        ContentResolver resolver = context.getContentResolver();

        //PermissionsChecker checker = new PermissionsChecker(context);
        //checker.lacksPermissions(Manifest.permission.READ_CALL_LOG);
        Cursor cursor = buildCursor(context, resolver);

        if (cursor == null) {
            return list;
        }

        // 3.通过Cursor获得数据
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            String date = new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String typeString = "";
            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    typeString = "拨入";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    typeString = "拨出";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    typeString = "未接";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    typeString = "拒接";
                    break;
                default:
                    typeString = "未知";
                    break;
            }
            CallRecord callRecord = new CallRecord();
            callRecord.setName(name == null ? "未知联系人" : name);
            callRecord.setNumber(number);
            callRecord.setDate(date);
            callRecord.setType(typeString);
            callRecord.setDuration(duration);
            list.add(callRecord);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }


    private static Cursor buildCursor(Context context, ContentResolver resolver) {
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         *
         */
        Cursor cursor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                cursor = resolver.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                        new String[]{CallLog.Calls.CACHED_NAME// 通话记录的联系人
                                , CallLog.Calls.NUMBER// 通话记录的电话号码
                                , CallLog.Calls.DATE// 通话记录的日期
                                , CallLog.Calls.DURATION// 通话时长
                                , CallLog.Calls.TYPE}// 通话类型
                        , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
                );
            } else {
                return null;
            }
        } else {
            cursor = resolver.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                    new String[]{CallLog.Calls.CACHED_NAME// 通话记录的联系人
                            , CallLog.Calls.NUMBER// 通话记录的电话号码
                            , CallLog.Calls.DATE// 通话记录的日期
                            , CallLog.Calls.DURATION// 通话时长
                            , CallLog.Calls.TYPE}// 通话类型
                    , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
            );
        }

        return cursor;
    }
}
