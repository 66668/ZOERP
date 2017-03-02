package com.zhongou.utils;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 最简 判断String 是本日 本周 本月
 * <p>
 * <p>
 * Created by sjy on 2016/12/1.
 */

public class DateUtils {


    //判断选择的日期是否是本周
    public static boolean isThisWeek(String paramsStr) {
        Log.d("SJY", "str=" + paramsStr);

        Date date = String2Date(paramsStr);
        long paramsTime = date.getTime();

        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        calendar.setTime(new Date(paramsTime));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        Log.d("SJY", "isThisWeek--paramWeek=" + paramWeek + "--currentWeek" + currentWeek);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    //判断选择的日期是否是今天
    public static boolean isToday(String paramsStr) {

        Date date = String2Date(paramsStr);
        long paramsTime = date.getTime();
        Log.d("SJY", "isToday");
        return isThisTime(paramsTime, "yyyy-MM-dd");
    }

    //判断选择的日期是否是本月
    public static boolean isThisMonth(String paramsStr) {
        //str格式：

        Date date = String2Date(paramsStr);
        long paramsTime = date.getTime();
        Log.d("SJY", "isThisMonth");
        return isThisTime(paramsTime, "yyyy-MM");
    }

    private static boolean isThisTime(long paramsTime, String pattern) {
        Date date = new Date(paramsTime);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String paramsDate = sdf.format(date);//参数时间
        String curDate = sdf.format(new Date());//当前时间
        Log.d("SJY", "isThisTime--paramsDate=" + paramsDate + "--curDate=" + curDate);
        if (paramsDate.equals(curDate)) {
            return true;
        }
        return false;
    }

    // 从字符串, 获取日期, 如time = "2016-3-16 4:12:16"
    public static Date String2Date(String paramsStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = sdf.parse(paramsStr);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

}