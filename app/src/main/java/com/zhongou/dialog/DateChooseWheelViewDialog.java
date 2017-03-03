package com.zhongou.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.listener.wheelviewListener.OnWheelChangedListener;
import com.zhongou.listener.wheelviewListener.OnWheelScrollListener;
import com.zhongou.adapter.AbstractWheelTextAdapter;
import com.zhongou.widget.wheelwidget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 使用说明：1.showLongTerm()是否显示长期选项
 * 2.setTimePickerGone隐藏时间选择
 * 3.接口DateChooseInterface
 *
 * 用于时间日期的选择
 *
 */
public class DateChooseWheelViewDialog extends Dialog implements View.OnClickListener {
    //控件
    private WheelView mYearWheelView;
    private WheelView mDateWheelView;
    private WheelView mHourWheelView;
    private WheelView mMinuteWheelView;
    private CalendarTextAdapter mDateAdapter;
    private CalendarTextAdapter mHourAdapter;
    private CalendarTextAdapter mMinuteAdapter;
    private CalendarTextAdapter mYearAdapter;
    private TextView mTitleTextView;
    private Button mSureButton;
    private Dialog mDialog;
    private Button mCloseDialog;

    //变量
    private ArrayList<String> arry_date = new ArrayList<String>();
    private ArrayList<String> arry_hour = new ArrayList<String>();
    private ArrayList<String> arry_minute = new ArrayList<String>();
    private ArrayList<String> arry_year = new ArrayList<String>();

    private int nowDateId = 0;
    private int nowHourId = 0;
    private int nowMinuteId = 0;
    private int nowYearId = 0;
    private String mYearStr;
    private String mDateStr;
    private String mHourStr;
    private String mMinuteStr;
    private boolean mBlnBeLongTerm = false;//是否需要长期
    private boolean mBlnTimePickerGone = false;//时间选择是否显示


    //常量
    private final int MAX_TEXT_SIZE = 18;
    private final int MIN_TEXT_SIZE = 16;

    private Context mContext;
    private DateChooseInterface dateChooseInterface;

    public DateChooseWheelViewDialog(Context context, DateChooseInterface dateChooseInterface) {
        super(context);
        this.mContext = context;
        this.dateChooseInterface = dateChooseInterface;
        mDialog = new Dialog(context, R.style.dialog);
        initView();
        initData();
    }


    private void initData() {
        initYear();
        initDate();
        initHour();
        initMinute();
        initListener();
    }
    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_date_choose, null);
        mDialog.setContentView(view);
        mYearWheelView = (WheelView) view.findViewById(R.id.year_wv);
        mDateWheelView = (WheelView) view.findViewById(R.id.date_wv);
        mHourWheelView = (WheelView) view.findViewById(R.id.hour_wv);
        mMinuteWheelView = (WheelView) view.findViewById(R.id.minute_wv);
        mTitleTextView = (TextView) view.findViewById(R.id.title_tv);
        mSureButton = (Button) view.findViewById(R.id.sure_btn);
        mCloseDialog = (Button) view.findViewById(R.id.date_choose_close_btn);

        mSureButton.setOnClickListener(this);
        mCloseDialog.setOnClickListener(this);
    }
    /**
     * 初始化年
     */
    private void initYear() {
        Calendar nowCalendar = Calendar.getInstance();
        int nowYear = nowCalendar.get(Calendar.YEAR);
        arry_year.clear();
        for (int i = 0; i <= 99; i++) {
            int year = nowYear -30 + i;
            arry_year.add(year + "年");
            if (nowYear == year) {
                nowYearId = arry_year.size() - 1;
            }
        }
        mYearAdapter = new CalendarTextAdapter(mContext, arry_year, nowYearId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        mYearWheelView.setVisibleItems(5);
        mYearWheelView.setViewAdapter(mYearAdapter);
        mYearWheelView.setCurrentItem(nowYearId);
        mYearStr = arry_year.get(nowYearId);
    }

    /**
     * 初始化日期
     */
    private void initDate() {
        Calendar nowCalendar = Calendar.getInstance();
        int nowYear = nowCalendar.get(Calendar.YEAR);
        arry_date.clear();
        setDate(nowYear);
        mDateAdapter = new CalendarTextAdapter(mContext, arry_date, nowDateId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        mDateWheelView.setVisibleItems(5);
        mDateWheelView.setViewAdapter(mDateAdapter);
        mDateWheelView.setCurrentItem(nowDateId);

        mDateStr = arry_date.get(nowDateId);
        setTextViewStyle(mDateStr, mDateAdapter);
    }

    public void setDateDialogTitle(String title) {
        mTitleTextView.setText(title);
    }

    /**
     * 初始化 时
     */
    private void initHour() {
        Calendar nowCalendar = Calendar.getInstance();
        int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
        arry_hour.clear();
        for (int i = 0; i <= 23; i++) {
            arry_hour.add(i + "");
            if (nowHour == i){
                nowHourId = arry_hour.size() - 1;
            }
        }

        mHourAdapter = new CalendarTextAdapter(mContext, arry_hour, nowHourId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        mHourWheelView.setVisibleItems(5);
        mHourWheelView.setViewAdapter(mHourAdapter);
        mHourWheelView.setCurrentItem(nowHourId);
        mHourStr = arry_hour.get(nowHourId) + "";
        setTextViewStyle(mHourStr, mHourAdapter);
    }

    /**
     * 初始化 分
     */
    private void initMinute() {
        Calendar nowCalendar = Calendar.getInstance();
        int nowMinite = nowCalendar.get(Calendar.MINUTE);
        arry_minute.clear();
        for (int i = 0; i <= 59; i++) {
            arry_minute.add(i + "");
            if (nowMinite == i){
                nowMinuteId = arry_minute.size() - 1;
            }
        }

        mMinuteAdapter = new CalendarTextAdapter(mContext, arry_minute, nowMinuteId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        mMinuteWheelView.setVisibleItems(5);
        mMinuteWheelView.setViewAdapter(mMinuteAdapter);
        mMinuteWheelView.setCurrentItem(nowMinuteId);
        mMinuteStr = arry_minute.get(nowMinuteId) + "";
        setTextViewStyle(mMinuteStr, mMinuteAdapter);

    }

    /**
     * 初始化滚动监听事件
     */
    private void initListener() {
        //年份*****************************
        mYearWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mYearAdapter);
                mYearStr = arry_year.get(wheel.getCurrentItem()) + "";

            }
        });

        mYearWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mYearAdapter);
            }
        });

        //日期********************
        mDateWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mDateAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mDateAdapter);
//                mDateCalendarTextView.setText(" " + arry_date.get(wheel.getCurrentItem()));
                mDateStr = arry_date.get(wheel.getCurrentItem());
            }
        });

        mDateWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mDateAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mDateAdapter);
            }
        });

        //小时***********************************
        mHourWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mHourAdapter);
                mHourStr = arry_hour.get(wheel.getCurrentItem()) + "";
            }
        });

        mHourWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mHourAdapter);
            }
        });

        //分钟********************************************
        mMinuteWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mMinuteAdapter);
                mMinuteStr = arry_minute.get(wheel.getCurrentItem()) + "";
            }
        });

        mMinuteWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mMinuteAdapter);
            }
        });
    }
    /**
     * 设置文字的大小
     * @param curriteItemText
     * @param adapter
     */
    public void setTextViewStyle(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(MAX_TEXT_SIZE);
                textvew.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
                textvew.setTextSize(MIN_TEXT_SIZE);
                textvew.setTextColor(mContext.getResources().getColor(R.color.gray));
            }
        }
    }
    /**
     * 将改年的所有日期写入数组
     * @param year
     */
    private void setDate(int year){
        boolean isRun = isRunNian(year);
        Calendar nowCalendar = Calendar.getInstance();
        int nowMonth = nowCalendar.get(Calendar.MONTH) + 1;
        int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
        for (int month = 1; month <= 12; month++){
            switch (month){
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    for (int day = 1; day <= 31; day++){
                        arry_date.add(month + "月" + day + "日");

                        if (month == nowMonth && day == nowDay){
                            nowDateId = arry_date.size() - 1;
                        }
                    }
                    break;
                case 2:
                    if (isRun){
                        for (int day = 1; day <= 29; day++){
                            arry_date.add(month + "月" + day + "日");
                            if (month == nowMonth && day == nowDay){
                                nowDateId = arry_date.size() - 1;
                            }
                        }
                    }else {
                        for (int day = 1; day <= 28; day++){
                            arry_date.add(month + "月" + day + "日");
                            if (month == nowMonth && day == nowDay){
                                nowDateId = arry_date.size() - 1;
                            }
                        }
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    for (int day = 1; day <= 30; day++){
                        arry_date.add(month + "月" + day + "日");
                        if (month == nowMonth && day == nowDay){
                            nowDateId = arry_date.size() - 1;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 判断是否是闰年
     * @param year
     * @return
     */
    private boolean isRunNian(int year){
        if(year % 4 == 0 && year % 100 !=0 || year % 400 == 0){
            return true;
        }else {
            return false;
        }
    }

    //不显示时 分设置
    public void setTimePickerGone(boolean isGone) {
        mBlnTimePickerGone = isGone;
        if (isGone) {
            LinearLayout.LayoutParams yearParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            yearParams.rightMargin = 22;

            LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mYearWheelView.setLayoutParams(yearParams);
            mDateWheelView.setLayoutParams(dateParams);

            mHourWheelView.setVisibility(View.GONE);
            mMinuteWheelView.setVisibility(View.GONE);
        } else {
            mHourWheelView.setVisibility(View.VISIBLE);
            mMinuteWheelView.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_btn://确定选择按钮监听
                if (mBlnTimePickerGone) {
                    dateChooseInterface.getDateTime(strTimeToDateFormat(mYearStr, mDateStr), mBlnBeLongTerm);
                } else {
                    dateChooseInterface.getDateTime(strTimeToDateFormat(mYearStr, mDateStr , mHourStr , mMinuteStr), mBlnBeLongTerm);
                }
                dismissDialog();
                break;
            case R.id.date_choose_close_btn://关闭日期选择对话框
                dismissDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 对话框消失
     */
    private void dismissDialog() {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            return;
        }

        if (null == mDialog || !mDialog.isShowing() || null == mContext
                || ((Activity) mContext).isFinishing()) {

            return;
        }

        mDialog.dismiss();
        this.dismiss();
    }

    /**
     * 显示日期选择dialog
     */
    public void showDateChooseDialog() {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            return;
        }

        if (null == mContext || ((Activity) mContext).isFinishing()) {

            // 界面已被销毁
            return;
        }

        if (null != mDialog) {

            mDialog.show();
            return;
        }

        if (null == mDialog) {

            return;
        }

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    /**
     *  xx年xx月xx日xx时xx分转成yyyy-MM-dd HH:mm
     * @param yearStr
     * @param dateStr
     * @param hourStr
     * @param minuteStr
     * @return
     */
    private String strTimeToDateFormat(String yearStr, String dateStr, String hourStr, String minuteStr) {
        String day = dayType(dateStr.substring(dateStr.indexOf("月")+1, dateStr.indexOf("日")));
        String month = monthType(dateStr.substring(0, dateStr.indexOf("月")));
        return yearStr.replace("年", "-") + month+"-"+day
                +" "+ hourType(hourStr) + ":" + minuteType(minuteStr);
    }

    private String strTimeToDateFormat(String yearStr, String dateStr) {
        String day = dayType(dateStr.substring(dateStr.indexOf("月")+1, dateStr.indexOf("日")));
        String month = monthType(dateStr.substring(0, dateStr.indexOf("月")));
        return yearStr.replace("年", "-") + month+"-"+day;
    }

    private String dayType(String day){
        if(day.length()<2){
            return "0"+day;
        }else {
            return day;
        }
    }
    private String monthType(String month){
        if(month.length()<2){
            return "0"+month;
        }else {
            return month;
        }
    }
    private String hourType(String hour){
        if(hour.length()<2){
            return "0"+hour;
        }else {
            return hour;
        }
    }
    private String minuteType(String min){
        if(min.length()<2){
            return "0"+min;
        }else {
            return min;
        }
    }
    /**
     * 滚轮的adapter
     */
    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_textview, R.id.tempValue, currentItem, maxsize, minsize);
            this.list = list;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            String str = list.get(index) + "";
            return str;
        }
    }

    /**
     * 回调选中的时间（默认时间格式"yyyy-MM-dd HH:mm:ss"）
     */
    public interface DateChooseInterface{
        void getDateTime(String time, boolean longTimeChecked);
    }

}
