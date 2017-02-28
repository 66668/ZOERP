package com.zhongou.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ScheduleModel;
import com.zhongou.widget.calendaruse.CalendarConstant;
import com.zhongou.widget.calendaruse.LunarCalendar;
import com.zhongou.widget.calendaruse.ScheduleDAO;
import com.zhongou.widget.calendaruse.ScheduleDateTag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 添加日程主界面
 */
public class ScheduleAddActivity extends BaseActivity {

    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //保存按钮
    @ViewInject(id = R.id.save, click = "saveAndTo")
    Button scheduleSave;

    //日程类型选择
    @ViewInject(id = R.id.scheduleType, click = "getScheduleType")
    TextView scheduleType;

    //提醒时间 设置
    @ViewInject(id = R.id.scheduleDate, click = "getProTime")
    TextView dateText;

    //日程内容
    @ViewInject(id = R.id.scheduleText)
    EditText scheduleText;

    private LunarCalendar lc = null;
    private ScheduleDAO dao = null;
    private static int hour = -1;
    private static int minute = -1;
    private static ArrayList<String> scheduleDate = null;
    ScheduleModel scheduleModel = null;
    private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
    private String scheduleYear = "";
    private String scheduleMonth = "";
    private String scheduleDay = "";
    private String week = "";

    //临时日期时间变量，
    private String tempMonth;
    private String tempDay;

    private String[] sch_type = CalendarConstant.sch_type;
    private String[] remind = CalendarConstant.remind;
    private int sch_typeID = 0;   //日程类型
    private int remindID = 0;     //提醒类型

    private static String schText = "";
    int schTypeID = 0;

    public ScheduleAddActivity() {
        lc = new LunarCalendar();
        dao = new ScheduleDAO(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_schedule_add);
        initMyView();
    }

    //初始设置
    private void initMyView() {
        tv_right.setText("");
        tv_title.setText("添加日程");

        scheduleType.setBackgroundColor(Color.WHITE);
        scheduleType.setText(sch_type[0] + "\t\t\t\t" + remind[remindID]);
        dateText.setBackgroundColor(Color.WHITE);
        scheduleText.setBackgroundColor(Color.WHITE);
        scheduleModel = new ScheduleModel();
        //日程内容
        if (schText != null) {
            //在选择日程类型之前已经输入了日程的信息，则在跳转到选择日程类型之前应当将日程信息保存到schText中，当返回时再次可以取得。
            scheduleText.setText(schText);
            //一旦设置完成之后就应该将此静态变量设置为空，
            schText = "";
        }
        Date date = new Date();
        if (hour == -1 && minute == -1) {
            hour = date.getHours();
            minute = date.getMinutes();
        }
        //显示当前时间
        dateText.setText(getScheduleDate());
    }

    /**
     * 设置 日程类型 提醒次数
     */
    public void getScheduleType(View view) {
        schText = scheduleText.getText().toString();
        Intent intent = new Intent();
        intent.setClass(ScheduleAddActivity.this, ScheduleTypeActivity.class);
        intent.putExtra("sch_remind", new int[]{sch_typeID, remindID});
        startActivityForResult(intent, 0);
    }

    /**
     * 设置提醒时间
     */
    public void getProTime(View view) {
        new TimePickerDialog(ScheduleAddActivity.this
                , new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int min) {

                hour = hourOfDay;
                minute = min;
                dateText.setText(getScheduleDate());
            }

        }
                , hour
                , minute
                , true).show();

    }

    //获取日程类型 回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            switch (requestCode) {
                case 0://取消

                    //获取ScheduleTypeActivity传来的值
                    int[] schType_remind0 = data.getIntArrayExtra("schType_remind");
                    if (schType_remind0 != null) {
                        sch_typeID = schType_remind0[0];
                        remindID = schType_remind0[1];
                        scheduleType.setText(sch_type[sch_typeID] + "\t\t\t\t" + remind[remindID]);
                    }
                    break;
                case 1://确定

                    //获取ScheduleTypeActivity传来的值
                    int[] schType_remind1 = data.getIntArrayExtra("schType_remind");
                    if (schType_remind1 != null) {
                        sch_typeID = schType_remind1[0];
                        remindID = schType_remind1[1];
                        scheduleType.setText(sch_type[sch_typeID] + "\t\t\t\t" + remind[remindID]);
                    }
                    break;

            }

        }

    }

    /**
     * 保存 新建日程
     *
     * @param view
     */
    public void saveAndTo(View view) {
        schText = scheduleText.getText().toString();

        if (TextUtils.isEmpty(scheduleText.getText().toString())) {
            //判断输入框是否为空
            new AlertDialog.Builder(ScheduleAddActivity.this)
                    .setTitle("输入日程")
                    .setMessage("日程信息不能为空")
                    .setPositiveButton("确认", null).show();
            return;
        }

        //设置 时间提醒次数 显示结果
        String showDate = setRemindCount(Integer.parseInt(scheduleYear), Integer.parseInt(tempMonth), Integer.parseInt(tempDay), hour, minute, week, remindID);

        //model赋值
        scheduleModel.setScheduleTypeID(sch_typeID);
        scheduleModel.setRemindID(remindID);
        scheduleModel.setScheduleDate(showDate);
        scheduleModel.setScheduleContent(scheduleText.getText().toString());

        //数据保存到sql中 待修改
        int scheduleID = dao.save(scheduleModel);

        //将scheduleID保存到数据中(因为在CalendarActivity中点击gridView中的一个Item可能会对应多个标记日程(scheduleID))
        String[] scheduleIDs = new String[]{String.valueOf(scheduleID)};

        //设置日程标记日期(将所有日程标记日期封装到list中)
        setScheduleDateTag(remindID, scheduleYear, tempMonth, tempDay, scheduleID);

        //保存后页面跳转 详情
        Intent intent = new Intent();
        intent.setClass(ScheduleAddActivity.this, ScheduleDetailActivity.class);
        intent.putExtra("scheduleID", scheduleIDs);
        startActivity(intent);
        this.finish();

    }

    /**
     * 设置日程标记日期
     *
     * @param remindID
     * @param year
     * @param month
     * @param day
     */
    public void setScheduleDateTag(int remindID, String year, String month, String day, int scheduleID) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        String d = year + "-" + month + "-" + day;
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //封装要标记的日期
        if (remindID >= 0 && remindID <= 3) {
            //"提醒一次","隔10分钟","隔30分钟","隔一小时"（只需标记当前这一天）
            ScheduleDateTag dateTag = new ScheduleDateTag();
            dateTag.setYear(Integer.parseInt(year));
            dateTag.setMonth(Integer.parseInt(month));
            dateTag.setDay(Integer.parseInt(day));
            dateTag.setScheduleID(scheduleID);
            dateTagList.add(dateTag);
        } else if (remindID == 4) {
            //每天重复(从设置的日程的开始的之后每一天多要标记)
            for (int i = 0; i <= (2049 - Integer.parseInt(year)) * 12 * 4 * 7; i++) {
                if (i == 0) {
                    cal.add(Calendar.DATE, 0);
                } else {
                    cal.add(Calendar.DATE, 1);
                }
                handleDate(cal, scheduleID);
            }
        } else if (remindID == 5) {
            //每周重复(从设置日程的这天(星期几)，接下来的每周的这一天多要标记)
            for (int i = 0; i <= (2049 - Integer.parseInt(year)) * 12 * 4; i++) {
                if (i == 0) {
                    cal.add(Calendar.WEEK_OF_MONTH, 0);
                } else {
                    cal.add(Calendar.WEEK_OF_MONTH, 1);
                }
                handleDate(cal, scheduleID);
            }
        } else if (remindID == 6) {
            //每月重复(从设置日程的这天(几月几号)，接下来的每月的这一天多要标记)
            for (int i = 0; i <= (2049 - Integer.parseInt(year)) * 12; i++) {
                if (i == 0) {
                    cal.add(Calendar.MONTH, 0);
                } else {
                    cal.add(Calendar.MONTH, 1);
                }
                handleDate(cal, scheduleID);
            }
        } else if (remindID == 7) {
            //每年重复(从设置日程的这天(哪一年几月几号)，接下来的每年的这一天多要标记)
            for (int i = 0; i <= 2049 - Integer.parseInt(year); i++) {
                if (i == 0) {
                    cal.add(Calendar.YEAR, 0);
                } else {
                    cal.add(Calendar.YEAR, 1);
                }
                handleDate(cal, scheduleID);
            }
        }

        //将标记日期存入数据库中
        dao.saveTagDate(dateTagList);
    }

    /**
     * 日程标记日期的处理
     *
     * @param cal
     */
    public void handleDate(Calendar cal, int scheduleID) {
        Log.d("ss", " 日程标记日期的处理--handleDate");

        ScheduleDateTag dateTag = new ScheduleDateTag();
        dateTag.setYear(cal.get(Calendar.YEAR));
        dateTag.setMonth(cal.get(Calendar.MONTH) + 1);
        dateTag.setDay(cal.get(Calendar.DATE));
        dateTag.setScheduleID(scheduleID);
        dateTagList.add(dateTag);
    }

    /**
     * 通过选择提醒次数 设置显示结果
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param week
     * @param remindID
     */
    public String setRemindCount(int year, int month, int day, int hour, int minute, String week, int remindID) {
        String remindType = remind[remindID];     //提醒类型
        String show = "";
        if (0 <= remindID && remindID <= 4) {
            //提醒一次,隔10分钟,隔30分钟,隔一小时
            show = year + "-" + month + "-" + day + "\t" + hour + ":" + minute + "\t" + week + "\t\t" + remindType;
        } else if (remindID == 5) {
            //每周
            show = "每周" + week + "\t" + hour + ":" + minute;
        } else if (remindID == 6) {
            //每月
            show = "每月" + day + "号" + "\t" + hour + ":" + minute;
        } else if (remindID == 7) {
            //每年
            show = "每年" + month + "-" + day + "\t" + hour + ":" + minute;
        }
        return show;
    }

    /**
     * 日程类型 提醒类型显示
     *
     * @return
     */
    public String getScheduleDate() {

        Intent intent = getIntent();//
        // intent.getp
        if (intent.getStringArrayListExtra("scheduleDate") != null) {
            scheduleDate = intent.getStringArrayListExtra("scheduleDate");
        }
        //从scheduleActivity中传来的值（包含年与日信息）

        // 得到年月日和星期
        scheduleYear = scheduleDate.get(0);
        Log.d("ss", "添加日程--年=" + scheduleYear);
        scheduleMonth = scheduleDate.get(1);
        tempMonth = scheduleMonth;
        if (Integer.parseInt(scheduleMonth) < 10) {
            scheduleMonth = "0" + scheduleMonth;
        }
        scheduleDay = scheduleDate.get(2);
        tempDay = scheduleDay;
        if (Integer.parseInt(scheduleDay) < 10) {
            scheduleDay = "0" + scheduleDay;
        }
        week = scheduleDate.get(3);
        String hour_c = String.valueOf(hour);
        String minute_c = String.valueOf(minute);
        if (hour < 10) {
            hour_c = "0" + hour_c;
        }
        if (minute < 10) {
            minute_c = "0" + minute_c;
        }


        // 得到对应的阴历日期
        String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
                Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
        String scheduleLunarMonth = lc.getLunarMonth(); // 得到阴历的月份
        StringBuffer scheduleDateStr = new StringBuffer();

        /*
         * 返回String 格式：
         * 2010-02-01 11:11:11
         * 一月廿五 星期八
         *
         */
        scheduleDateStr.append(scheduleYear).append("-").append(scheduleMonth).append("-").append(scheduleDay)//2010-01-01
                .append(" ").append(hour_c).append(":").append(minute_c)//11:11:11
                .append("\n").append(scheduleLunarMonth).append(scheduleLunarDay)//农历日期
                .append(" ").append(week);//星期
        return scheduleDateStr.toString();
    }

    /**
     * 根据日期的年月日返回阴历日期
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public String getLunarDay(int year, int month, int day) {
        String lunarDay = lc.getLunarDate(year, month, day, true);
        // {由于在取得阳历对应的阴历日期时，如果阳历日期对应的阴历日期为"初一"，就被设置成了月份(如:四月，五月。。。等)},所以在此就要判断得到的阴历日期是否为月份，如果是月份就设置为"初一"
        if (lunarDay.substring(1, 2).equals("月")) {
            lunarDay = "初一";
        }
        return lunarDay;
    }

    /**
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }
}
