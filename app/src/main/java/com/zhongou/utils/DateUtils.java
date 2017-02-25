package com.zhongou.utils;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sjy on 2016/12/1.
 * 处理服务端返回数据，判断是本日 本周 本月
 */

public class DateUtils {

    //格式：2017/02/24 14:25:46
    public static boolean isToday(String formatdate) {
        if (TextUtils.isEmpty(formatdate)) {
            return false;
        }
        String dateYear = "";
        String dateMonth = "";
        String dateDay = "";

        String curYear = "";
        String curMonth = "";
        String curDay = "";

        Date date = StrToDate(formatdate);

        dateYear = String.valueOf(date.getYear());
        dateMonth = String.valueOf(date.getMonth());
        dateDay = String.valueOf(date.getDay());
        Calendar c = Calendar.getInstance();
        curYear = String.valueOf(c.get(Calendar.YEAR));
        curMonth = String.valueOf(c.get(Calendar.MONTH));
        curDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));

        if (dateYear.equals(curYear)) {

            if (dateMonth.equals(curMonth)) {
                if (dateDay.equals(curDay)) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //格式：2017/2/24 14:25:46
    public static boolean isThisMonth(String formatdate) {
        if (TextUtils.isEmpty(formatdate)) {
            return false;
        }

        Date date = StrToDate(formatdate);

        String dateYear = String.valueOf(date.getYear());
        String dateMonth = String.valueOf(date.getMonth());

        Calendar c = Calendar.getInstance();

        String curYear = String.valueOf(c.get(Calendar.YEAR));
        String curMonth = String.valueOf(c.get(Calendar.MONTH));

        if (dateYear.equals(curYear)) {

            if (dateMonth.equals(curMonth)) {
                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否是本周数据
     * <p>
     * 逻辑步骤：
     * 先获取当前时间的周一和周日的日期，格式 yyyy/MM/dd HH:mm:ss
     * 再根据传入的字符串，比较是否在周一周日之间
     *
     * @param formatdate
     * @return
     */
    public static Boolean isThisWeek(String formatdate) {
        //01 先计算出当前时间所在日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date curdate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(curdate);

        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        //当前周一所在的日期
        String imptimeBegin = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        //当前周日所在日期
        String imptimeEnd = sdf.format(cal.getTime());

        //再比较 传入数据是否在这个范围

        //数据的年月日
        Date date = StrToDate(formatdate);
        int dateYear = date.getYear();
        int dateMonth = date.getMonth();
        int dateDay = date.getDay();

        Date monDayDate = StrToDate(imptimeBegin);
        int mondayYear = monDayDate.getYear();
        int mondayMonth = monDayDate.getMonth();
        int mondayDay = monDayDate.getDay();

        Date weekDate = StrToDate(imptimeEnd);
        int weekYear = weekDate.getYear();
        int weekMonth = weekDate.getMonth();
        int weekDay = weekDate.getDay();

        //注意极端情况
        if (mondayYear <= dateYear && dateYear <= weekDay) {
            if (mondayMonth == dateMonth && dateMonth == weekMonth) {//同月
                if (mondayDay <= dateDay && dateDay <= weekDay) {
                    return true;
                } else {
                    return false;
                }
            } else if (mondayMonth < dateMonth && dateMonth == weekMonth) {
                if (dateDay <= weekDay) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    private static Date StrToDate(String str) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            date = format.parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}