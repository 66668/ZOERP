package com.zhongou.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.db.sqlite.SQLiteScheduledb;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ScheduleModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.common.calendarcommon.CalendarTpyeArray;

/**
 * 日程详情
 * 当日只有一个日程的界面显示
 * <p>
 */
public class ScheduleSingleDetailActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "toDelete")
    TextView tv_right;

    //日程类型
    @ViewInject(id = R.id.scheduleType)
    TextView scheduleType;

    //提醒次数
    @ViewInject(id = R.id.scheduleRemind)
    TextView scheduleRemind;

    //提醒时间
    @ViewInject(id = R.id.tvProTime)
    TextView dateText;


    //日程内容
    @ViewInject(id = R.id.scheduleText)
    TextView scheduleText;

    //    private ScheduleDAO dao = null;
    private SQLiteScheduledb dao = null;
    private String[] scheduleIDs;
    private ScheduleModel scheduleModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_schedule_details);

        initMyView();
    }

    private void initMyView() {

        tv_title.setText("日程详情");
        tv_right.setText("删除");


        //        dao = new ScheduleDAO(this);
        dao = new SQLiteScheduledb(this, UserHelper.getCurrentUser().getEmployeeID() + ".db");

        Intent intent = getIntent();
        scheduleIDs = intent.getStringArrayExtra("scheduleID");
        if (scheduleIDs.length > 0) {
            scheduleModel = dao.getScheduleByID(Integer.parseInt(scheduleIDs[0]));
            setShow(scheduleModel);
        }

    }

    private void setShow(ScheduleModel model) {
        if (model != null) {
            scheduleType.setText(CalendarTpyeArray.sch_type[model.getScheduleTypeID()]);
            scheduleRemind.setText(CalendarTpyeArray.remind[model.getRemindID()]);
            dateText.setText(model.getScheduleDate());
            scheduleText.setText(model.getScheduleContent());
        } else {
            PageUtil.DisplayToast("日程表数据");
        }

    }

    public void forBack(View view) {
        this.finish();
    }


    public void toDelete(View view) {
        new AlertDialog.Builder(ScheduleSingleDetailActivity.this).setTitle("删除日程").setMessage("确认删除").setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dao.delete(Integer.parseInt(scheduleIDs[0]));
                //                update();

                ScheduleSingleDetailActivity.this.finish();
            }
        }).setNegativeButton("取消", null).show();

    }

}
