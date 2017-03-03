package com.zhongou.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.db.sqlite.SQLiteScheduledb;
import com.zhongou.helper.UserHelper;
import com.zhongou.model.ScheduleModel;
import com.zhongou.common.calendarcommon.CalendarTpyeArray;

/**
 * 日程详情
 * <p>
 * 动态添加布局
 */
public class ScheduleMultDetailActivity extends Activity {

    private LinearLayout layout = null;
    private TextView textTop = null;
    private TextView info = null;
    private TextView date = null;
    private TextView type = null;
    private TextView editInfo = null;

    //    private ScheduleDAO dao = null;
    private SQLiteScheduledb dao = null;


    private ScheduleModel scheduleModel = null;

    private String scheduleInfo = "";    //日程信息被修改前的内容
    private String scheduleChangeInfo = "";  //日程信息被修改之后的内容
    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        dao = new ScheduleDAO(this);
        dao = new SQLiteScheduledb(this, UserHelper.getCurrentUser().getEmployeeID() + ".db");

        params.setMargins(0, 5, 0, 0);
        layout = new LinearLayout(this); // 实例化布局对象
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(getResources().getColor(R.color.common_bg_examination));
        layout.setLayoutParams(params);

        textTop = new TextView(this, null);
        textTop.setTextColor(Color.BLACK);
        textTop.setBackgroundColor(getResources().getColor(R.color.common_topbar_bgcolor));
        textTop.setText("日程详情");
        textTop.setHeight(47);
        textTop.setGravity(Gravity.CENTER);


        editInfo = new TextView(ScheduleMultDetailActivity.this, null);
        editInfo.setTextColor(Color.BLACK);
        editInfo.setBackgroundColor(Color.WHITE);
        editInfo.setHeight(200);
        editInfo.setGravity(Gravity.TOP);
        editInfo.setLayoutParams(params);
        editInfo.setPadding(10, 5, 10, 5);

        layout.addView(textTop);


        Intent intent = getIntent();
        //scheduleID = Integer.parseInt(intent.getStringExtra("scheduleID"));
        //当日可能对应多个标记日程(scheduleID)
        String[] scheduleIDs = intent.getStringArrayExtra("scheduleID");

        //显示日程详细信息
        for (int i = 0; i < scheduleIDs.length; i++) {

            handlerInfo(Integer.parseInt(scheduleIDs[i]));
        }
        setContentView(layout);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1, menu.FIRST, menu.FIRST, "所有日程");
        menu.add(1, menu.FIRST + 1, menu.FIRST + 1, "添加日程");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case Menu.FIRST:
                Log.d("ss", "onOptionsItemSelected--跳转--ScheduleAllActivity");
                Intent intent = new Intent();
                intent.setClass(ScheduleMultDetailActivity.this, ScheduleListActivity.class);
                startActivity(intent);
                break;
            case Menu.FIRST + 1:

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 显示日程所有信息
     */
    public void handlerInfo(int scheduleID) {

        TextView date = new TextView(this, null);
        date.setTextColor(Color.BLACK);
        date.setBackgroundColor(Color.WHITE);
        date.setLayoutParams(params);
        date.setGravity(Gravity.CENTER_VERTICAL);
        date.setHeight(80);
        date.setPadding(10, 0, 10, 0);

        TextView type = new TextView(this, null);
        type.setTextColor(Color.BLACK);
        type.setBackgroundColor(Color.WHITE);
        type.setLayoutParams(params);
        type.setGravity(Gravity.CENTER);
        type.setHeight(80);
        type.setPadding(10, 0, 10, 0);
        type.setTag(scheduleID);

        final TextView info = new TextView(this, null);
        info.setTextColor(Color.BLACK);
        info.setBackgroundColor(Color.WHITE);
        info.setGravity(Gravity.CENTER_VERTICAL);
        info.setLayoutParams(params);
        info.setPadding(10, 5, 10, 5);


        layout.addView(type);
        layout.addView(date);
        layout.addView(info);
        /*Intent intent = getIntent();
        int scheduleID = Integer.parseInt(intent.getStringExtra("scheduleID"));*/
        scheduleModel = dao.getScheduleByID(scheduleID);
        date.setText(scheduleModel.getScheduleDate());
        type.setText(CalendarTpyeArray.sch_type[scheduleModel.getScheduleTypeID()]);
        info.setText(scheduleModel.getScheduleContent());


        //长时间按住日程类型textview就提示是否删除日程信息
        type.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Log.d("ss", "长按删除");
                final String scheduleID = String.valueOf(v.getTag());

                new AlertDialog.Builder(ScheduleMultDetailActivity.this).setTitle("删除日程").setMessage("确认删除").setPositiveButton("确认", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ss", "跳转--ScheduleAllActivity");
                        dao.delete(Integer.parseInt(scheduleID));
                        Intent intent1 = new Intent();
                        intent1.setClass(ScheduleMultDetailActivity.this, ScheduleListActivity.class);
                        startActivity(intent1);
                    }
                }).setNegativeButton("取消", null).show();

                return true;
            }
        });


    }
}
