package com.zhongou.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.zhongou.R;
import com.zhongou.adapter.ScheduleMainAdapter;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;
import com.zhongou.widget.calendaruse.ScheduleDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 日程安排
 * Created by sjy on 2017/1/9.
 */

public class ScheduleMainActivity extends BaseActivity implements GestureDetector.OnGestureListener {

    //back
    @ViewInject(id = R.id.imgBack, click = "forBack")
    ImageView imgBack;

    //
    @ViewInject(id = R.id.tv_title, click = "toRefresh")
    TextView tv_title;

    //日程表
    @ViewInject(id = R.id.tv_right, click = "toList")
    TextView tvRight;

    //顶部时间
    @ViewInject(id = R.id.tv_CalendarCenter, click = "jumpTo")
    TextView topText;

    private ViewFlipper flipper = null;
    private GestureDetector gestureDetector = null;
    private ScheduleMainAdapter adapter = null;
    private GridView gridView = null;

    private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0;//滑动跨越一年，则增加或者减去一年,默认为0(即当前年)

    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;

    private String currentDate = "";
    private ScheduleDAO dao = null;


    public ScheduleMainActivity() {
        //获取年 月 日
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

        dao = new ScheduleDAO(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_schedule_main);

        initMyView();
        addGridView();
        gridView.setAdapter(adapter);
        flipper.addView(gridView, 0);
        addTextToTopTextView(topText);
    }

    private void initMyView() {
        tv_title.setText("日程");
        tvRight.setText("日程表");
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        adapter = new ScheduleMainAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);

        gestureDetector = new GestureDetector(this);//设置监听


    }

    //选择跳转月
    public void jumpTo(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                //1901-1-1 ----> 2049-12-31
                if (year < 1901 || year > 2049) {
                    //不在查询范围内
                    new AlertDialog.Builder(ScheduleMainActivity.this).setTitle("错误日期").setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
                } else {
                    int gvFlag = 0;
                    addGridView();   //添加一个gridView
                    adapter = new ScheduleMainAdapter(ScheduleMainActivity.this, ScheduleMainActivity.this.getResources(), year, monthOfYear + 1, dayOfMonth);
                    gridView.setAdapter(adapter);
                    addTextToTopTextView(topText);
                    gvFlag++;
                    flipper.addView(gridView, gvFlag);
                    if (year == year_c && monthOfYear + 1 == month_c) {
                        //nothing to do
                    }
                    if ((year == year_c && monthOfYear + 1 > month_c) || year > year_c) {
                        ScheduleMainActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(ScheduleMainActivity.this, R.anim.push_left_in));
                        ScheduleMainActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(ScheduleMainActivity.this, R.anim.push_left_out));
                        ScheduleMainActivity.this.flipper.showNext();
                    } else {
                        ScheduleMainActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(ScheduleMainActivity.this, R.anim.push_right_in));
                        ScheduleMainActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(ScheduleMainActivity.this, R.anim.push_right_out));
                        ScheduleMainActivity.this.flipper.showPrevious();
                    }
                    flipper.removeViewAt(0);
                    //跳转之后将跳转之后的日期设置为当期日期
                    year_c = year;
                    month_c = monthOfYear + 1;
                    day_c = dayOfMonth;
                    jumpMonth = 0;
                    jumpYear = 0;
                }
            }
        }, year_c, month_c - 1, day_c).show();
    }

    //跳转日程表

    public void toList(View view) {
        //日程表
        Intent intent = new Intent();
        intent.setClass(ScheduleMainActivity.this, ScheduleListActivity.class);
        startActivity(intent);

    }

    //点击标题刷新

    public void toRefresh(View view) {
        String viewTag = String.valueOf(view.getTag());
        //跳转到今天
        int xMonth = jumpMonth;
        int xYear = jumpYear;
        int gvFlag = 0;
        jumpMonth = 0;
        jumpYear = 0;
        addGridView();   //添加一个gridView
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        adapter = new ScheduleMainAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(adapter);
        addTextToTopTextView(topText);
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        if (xMonth == 0 && xYear == 0) {
            //nothing to do
        } else if ((xYear == 0 && xMonth > 0) || xYear > 0) {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
            this.flipper.showNext();
        } else {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
            this.flipper.showPrevious();
        }
        flipper.removeViewAt(0);

    }


    /**
     * 添加头部的年份 闰哪月等信息
     */
    private void addTextToTopTextView(TextView view) {

        StringBuffer textDate = new StringBuffer();
        textDate.append(adapter.getShowYear()).append("年").append(adapter.getShowMonth()).append("月").append("\t");

        //        //闰x月
        //        if (!adapter.getLeapMonth().equals("") && adapter.getLeapMonth() != null) {
        //            textDate.append("闰").append(adapter.getLeapMonth()).append("月").append("\t");
        //        }
        //        //生肖 年（天干地支 年）
        //        textDate.append(adapter.getAnimalsYear()).append("年").append("(").append(adapter.getCyclical()).append("年)");

        view.setText(textDate);
        view.setTextColor(Color.BLACK);
        view.setTypeface(Typeface.DEFAULT_BOLD);
    }

    //动态添加gridview
    private void addGridView() {

        // 02代码添加布局
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        //取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(this);
        gridView.setNumColumns(7);
        gridView.setColumnWidth(46);

        if (Width == 480 && Height == 800) {
            gridView.setColumnWidth(69);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setBackgroundColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.gray));//设置背景色

        gridView.setOnTouchListener(new View.OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return ScheduleMainActivity.this.gestureDetector
                        .onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //gridView中的每一个item的点击事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = adapter.getStartPositon();
                int endPosition = adapter.getEndPosition();
                if (startPosition <= position && position <= endPosition) {
                    String scheduleDay = adapter.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
                    String scheduleYear = adapter.getShowYear();
                    String scheduleMonth = adapter.getShowMonth();
                    String week = "";

                    // 当 X日有多个日程时，点击X日的任意一条日程，都会跳转显示X日的所有日程信息
                    //通过日期查询这一天是否被标记，如果标记了日程 查询这天在sql所有对应的位置id
                    String[] scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));

                    if (scheduleIDs != null && scheduleIDs.length > 0) {
                        if (scheduleIDs.length == 1) {
                            Intent intent = new Intent();
                            intent.setClass(ScheduleMainActivity.this, ScheduleSingleDetailActivity.class);
                            intent.putExtra("scheduleID", scheduleIDs);
                            startActivity(intent);
                        } else {
                            //跳转到显示这一天的所有日程信息界面
                            Intent intent = new Intent();
                            intent.setClass(ScheduleMainActivity.this, ScheduleMultDetailActivity.class);
                            intent.putExtra("scheduleID", scheduleIDs);
                            startActivity(intent);
                        }

                    } else {
                        //直接跳转到需要添加日程的界面

                        //得到这一天是星期几
                        switch (position % 7) {
                            case 0:
                                week = "星期日";
                                break;
                            case 1:
                                week = "星期一";
                                break;
                            case 2:
                                week = "星期二";
                                break;
                            case 3:
                                week = "星期三";
                                break;
                            case 4:
                                week = "星期四";
                                break;
                            case 5:
                                week = "星期五";
                                break;
                            case 6:
                                week = "星期六";
                                break;
                        }

                        ArrayList<String> scheduleDate = new ArrayList<String>();
                        scheduleDate.add(scheduleYear);
                        scheduleDate.add(scheduleMonth);
                        scheduleDate.add(scheduleDay);
                        scheduleDate.add(week);
                        //scheduleDate.add(scheduleLunarDay);

                        /*
                         * 页面跳转
                         */
                        Log.d("ss", "进入 日程添加界面");
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("scheduleDate", scheduleDate);
                        intent.setClass(ScheduleMainActivity.this, ScheduleAddActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        //        gridView.setLayoutParams(params);


    }

    //OnGestureListener接口方法01
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    //OnGestureListener接口方法02
    @Override
    public void onShowPress(MotionEvent e) {

    }

    //OnGestureListener接口方法03
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    //OnGestureListener接口方法04
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    //OnGestureListener接口方法05
    @Override
    public void onLongPress(MotionEvent e) {

    }

    /*
     * OnGestureListener接口方法06
     * 切换上个月 下个月日历
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
        if (e1.getX() - e2.getX() > 120) {
            Log.d("ss", "像左滑动");
            //像左滑动
            addGridView();   //添加一个gridView
            jumpMonth++;     //下一个月

            adapter = new ScheduleMainAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
            gridView.setAdapter(adapter);
            //flipper.addView(gridView);
            addTextToTopTextView(topText);
            gvFlag++;
            flipper.addView(gridView, gvFlag);
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
            this.flipper.showNext();
            flipper.removeViewAt(0);
            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            Log.d("ss", "向右滑动");
            //向右滑动
            addGridView();   //添加一个gridView
            jumpMonth--;     //上一个月

            adapter = new ScheduleMainAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
            gridView.setAdapter(adapter);
            gvFlag++;
            addTextToTopTextView(topText);
            //flipper.addView(gridView);
            flipper.addView(gridView, gvFlag);

            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
            this.flipper.showPrevious();
            flipper.removeViewAt(0);
            return true;
        }
        return false;
    }

    //页面刷新
    @Override
    protected void onResume() {
        super.onResume();
        //跳转到今天
        int xMonth = jumpMonth;
        int xYear = jumpYear;
        int gvFlag = 0;
        jumpMonth = 0;
        jumpYear = 0;
        addGridView();   //添加一个gridView
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        adapter = new ScheduleMainAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(adapter);
        addTextToTopTextView(topText);
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        if (xMonth == 0 && xYear == 0) {
            //nothing to do
        } else if ((xYear == 0 && xMonth > 0) || xYear > 0) {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
            this.flipper.showNext();
        } else {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
            this.flipper.showPrevious();
        }
        flipper.removeViewAt(0);
    }

    //
    public void forBack(View view) {
        this.finish();
    }

}
