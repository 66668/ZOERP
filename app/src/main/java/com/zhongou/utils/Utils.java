package com.zhongou.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;

import com.zhongou.application.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * 获取存储空间
 *
 * @author JackSong
 */
public class Utils {

    private Utils() {
    }

    /**
     * dip/dp convert to pixel
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * pixel convert to dip/dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * get the screen width(px) for specific-mobile
     *
     * @param activity
     * @return px
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (dm.widthPixels);
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (dm.heightPixels);
    }

    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public static String toSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 获取时间戳 单位（秒）
     *
     * @return
     */
    public static Integer getTimeStamp() {
        return ((Long) (System.currentTimeMillis() / 1000)).intValue();
    }

    /**
     * 获取今天的时间戳(秒)
     *
     * @return
     */
    //	public static Integer getTodayTimeStamp() {
    //		return ((Long) (Timestamp.valueOf(getCurrentDate() + " 00:00:00.0")
    //				.getTime() / 1000)).intValue();
    //	}

    /**
     * 获取时间戳 单位（毫秒）
     *
     * @return
     */
    public static long getMillisTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 把时间戳转成日期字符串
     *
     * @param timeStamp
     * @return yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String toDateTime(long timeStamp) {
        Timestamp ts = new Timestamp(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //	public static String getCurrentTime() {
    //		Date date = new Date();
    //		String strDate = new String(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
    //				Locale.CHINA).format(date));
    //		return strDate;
    //	}
    //
    //	public static String getCurrentDate() {
    //		Date date = new Date();
    //		String strDate = new String(new SimpleDateFormat("yyyy-MM-dd",
    //				Locale.CHINA).format(date));
    //		return strDate;
    //	}

    /**
     * 是否是同一天
     *
     * @param t1 毫秒
     * @param t2 毫秒
     * @return
     */
    public static boolean isSameDay(long t1, long t2) {
        return getDate(t1, "yyyy-MM-dd").equals(getDate(t2, "yyyy-MM-dd"));
    }

    /**
     * 获取日期yyyy-MM-dd HH:mm:ss
     *
     * @param datetime 毫秒
     * @return
     */
    public static String getDate(long datetime) {
        return getDate(datetime, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取日期
     *
     * @param datetime 毫秒
     * @param format
     * @return
     */
    public static String getDate(long datetime, String format) {
        Date date = new Date(datetime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(date);

    }

    public static int customDay = 0;

    public static int dateDiff(String fromDate, String toDate, int custom) {

        // return customDay;
        int days = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.CHINA);
            java.util.Date from = df.parse(fromDate);
            java.util.Date to = df.parse(toDate);
            days = (int) Math.abs((to.getTime() - from.getTime())
                    / (24 * 60 * 60 * 1000)) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    public static String formatSize(long size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        try {
            return Build.PRODUCT;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机分辨率
     *
     * @return
     */
    public static String getPhoneResolution() {
        try {
            DisplayMetrics dm = MyApplication.getInstance().getResources()
                    .getDisplayMetrics();
            return dm.widthPixels + "x" + dm.heightPixels;
        } catch (Exception ex) {
            return "";
        }
    }

    public String getProvidersName(Context context) {
        String ProvidersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        // 返回唯一的用户ID;就是这张卡的编号
        String IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;

    }

    /**
     * 获取网络信息
     *
     * @return
     */
    public static String getNetworkInfo() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) MyApplication
                    .getInstance().getSystemService(
                            Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            String typeName = info[i].getTypeName();
                            if (typeName.equals("WIFI")) {
                                WifiManager wifiManager = (WifiManager) MyApplication
                                        .getInstance().getSystemService(Context.WIFI_SERVICE);
                                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                typeName += " SSID:" + wifiInfo.getSSID() + " MAC:" + wifiInfo.getMacAddress();
                                return typeName;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取当前网络状态
     *
     * @return NetworkInfo
     */
    public static NetworkInfo getCurrentNetStatus(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }

    /**
     * 获取网络连接状态
     *
     * @param ctx
     * @return true:有网 false：没网
     */
    public static boolean isNetworkAvailable(Context ctx) {
        NetworkInfo nki = getCurrentNetStatus(ctx);
        if (nki != null) {
            return nki.isAvailable();
        } else
            return false;
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    @SuppressWarnings("deprecation")
    public static int getAPILevel() {
        return Integer.parseInt(Build.VERSION.SDK);
    }

    public static JSONObject getJsonObject(JSONObject json, String key) {
        try {
            return json.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray getJsonArray(JSONObject json, String key) {
        try {
            return json.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getJsonInt(JSONObject json, String key) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getJsonString(JSONObject json, String key) {
        try {
            if (!json.has(key))
                return "";
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Boolean getJsonBoolean(JSONObject json, String key) {
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 秒转成分钟形式
     *
     * @param seconds
     * @return
     */
    public static String toMin(int seconds) {
        String result = "";
        long minute = seconds % 3600 / 60;
        long second = seconds % 60;
        if (minute < 10) {
            result += "0" + minute;
        } else {
            {
                result += minute;
            }
        }
        result += ":";
        if (second < 10) {
            result += "0" + second;
        } else {
            {
                result += second;
            }
        }
        return result;
    }

    /**
     * 测量View尺寸
     *
     * @param v
     */
    public static void measure(View v) {
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    /**
     * MD5
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    //


    /**
     * @param
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化成绩
     *
     * @param score
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatScore(int score) {
        return String.format("%04d", score);
    }


    /**
     * 格式化数字
     *
     * @param
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatInt(int i, int len) {
        return String.format("%0" + len + "d", i);
    }

    /**
     * 字符串是否空
     *
     * @param searchKeyWords
     * @return
     */
    public static boolean isNullOrEmpty(String searchKeyWords) {
        if (searchKeyWords == null || searchKeyWords.length() == 0) {
            return true;
        }
        return false;
    }

    public static String toSQL(String sql) {
        return sql.replace("'", "''");
    }

    /**
     * 数组包含关系
     *
     * @param strings
     * @param str
     * @return
     */
    public static boolean contains(String[] strings, String str) {
        for (String string : strings) {
            if (string.equals(str))
                return true;
        }
        return false;
    }

    /**
     * 打乱List
     *
     * @param list
     * @return
     */
    public static <T extends Object> List<T> breakList(List<T> list) {
        List<T> resultList = new ArrayList<T>();
        Random rand = new Random();
        while (list.size() > 0) {
            int i = rand.nextInt(list.size());
            resultList.add(list.get(i));
            list.remove(i);
        }
        return resultList;
    }

    public static Bitmap readBitMap(Context context, String filePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = computeSampleSize(opt, -1, 100 * 100); // 计算出图片使用的inSampleSize
        opt.inJustDecodeBounds = false;
        // 获取资源图片
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(is, null, opt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // InputStream is = context.getResources().openRawResource(resId);

    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = computeSampleSize(opt, -1, 100 * 100); // 计算出图片使用的inSampleSize
        opt.inJustDecodeBounds = false;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public static JSONObject getDeviceData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("networkType", getNetworkInfo());
            jsonObject.put("deviceUUID", getDeviceId());
            jsonObject.put("token", getNetworkInfo());
            jsonObject.put("deviceModel", getPhoneModel());
            jsonObject.put("resolution", getPhoneResolution());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        // int height = view.getMeasuredHeight();
        // if(0 < height){
        // return height;
        // }
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
        // int width = view.getMeasuredWidth();
        // if(0 < width){
        // return width;
        // }
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        // int width =
        // View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // int height =
        // View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // view.measure(width,height);

        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    /**
     * 获取可用存储空间大小 若存在SD卡则返回SD卡剩余空间大小 否则返回手机内存剩余空间大小
     *
     * @return
     */
    public static long getAvailableStorageSpace() {
        long externalSpace = getExternalStorageSpace();
        if (externalSpace == -1L) {
            return getInternalStorageSpace();
        }

        return externalSpace;
    }


    /**
     * 生成全球唯一识别码uuid
     *
     * @return
     */
    public static String getMyUUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }

    /**
     * 获取SD卡可用空间
     *
     * @return availableSDCardSpace 可用空间(MB)。-1L:没有SD卡
     */
    public static long getExternalStorageSpace() {
        long availableSDCardSpace = -1L;
        // 存在SD卡
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory()
                    .getPath());
            long blockSize = sf.getBlockSize();// 块大小,单位byte
            long availCount = sf.getAvailableBlocks();// 可用块数量
            availableSDCardSpace = availCount * blockSize / 1024 / 1024;// 可用SD卡空间，单位MB
        }

        return availableSDCardSpace;
    }

    /**
     * 获取机器内部可用空间
     *
     * @return availableSDCardSpace 可用空间(MB)。-1L:没有SD卡
     */
    //CreateUserActivity--UpdateAvatar--MyApplication--Utils该方法
    public static long getInternalStorageSpace() {
        long availableInternalSpace = -1L;//-1L:没有SD卡

        StatFs sf = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = sf.getBlockSize();// 块大小,单位byte
        long availCount = sf.getAvailableBlocks();// 可用块数量
        availableInternalSpace = availCount * blockSize / 1024 / 1024;// 可用SD卡空间，单位MB

        return availableInternalSpace;
    }

    /**
     * 获取手机唯一设备号（个推的clientID值不是这个）
     *
     * @return
     */
    private static String deviceId = null;

    public synchronized static String getDeviceId() {
        if (deviceId == null || deviceId.length() == 0) {
            //实例化通讯类对象
            final TelephonyManager tm = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            //获取Android设备(手机)唯一标识码，注意:获取DEVICE_ID需要注册READ_PHONE_STATE权限
            deviceId = tm.getDeviceId();//IMEI,MEID
        }
        return deviceId;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {

        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {

        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        return time;

    }

    /**
     * 类型安全转换(八大类型)
     */
    //(1)int型
    public final static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

}

