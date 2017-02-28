package com.zhongou.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.widget.calendaruse.CalendarConstant;

/**
 * 日程类型选择
 * <p>
 * 代码动态添加布局
 *
 * @author jack_peng
 */
public class ScheduleTypeActivity extends BaseActivity {

    LinearLayout.LayoutParams params;
    private CalendarConstant cc = null;
    private int sch_typeID = 0;
    private int remindID = 0;
    private LinearLayout layout; // 布局 可以在xml布局中获得
    private LinearLayout layButton;
    private RadioGroup group; // 点选按钮组
    private TextView textTop = null;
    private RadioButton radio = null;
    private TextView btSave = null;
    private TextView btCancel = null;
    private int schType_temp = 0;//日程类型索引
    private int remind_temp = 0;//提醒时间 索引
    private int[] sch_remind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initContentView();
        initListener();
    }

    private void initContentView() {
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        cc = new CalendarConstant();

        layout = new LinearLayout(this); // 实例化布局对象
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.mipmap.ic_launcher);
        layout.setLayoutParams(params);

        //初始化控件
        group = new RadioGroup(this);
        btSave = new TextView(this, null);
        btCancel = new TextView(this, null);
        textTop = new TextView(this, null);//标题
        layButton = new LinearLayout(this);//button 父布局
        //设置控件

        //textTop设置
        textTop.setTextColor(Color.BLACK);
        textTop.setBackgroundResource(R.mipmap.ic_launcher);//图片top_day
        textTop.setText("日程类型");
        textTop.setHeight(47);
        textTop.setGravity(Gravity.CENTER);
        layout.addView(textTop);

        Intent intent = getIntent();
        sch_remind = intent.getIntArrayExtra("sch_remind");  //从ScheduleView传来的值

        if (sch_remind != null) {
            sch_typeID = sch_remind[0];
            remindID = sch_remind[1];
        }
        for (int i = 0; i < cc.sch_type.length; i++) {
            radio = new RadioButton(this);
            if (i == sch_typeID) {
                radio.setChecked(true);
            }

            radio.setText(cc.sch_type[i]);
            radio.setId(i);
            radio.setTextColor(Color.BLACK);
            group.addView(radio);
        }
        layout.addView(group);

        //设置button父布局
        layButton.setOrientation(LinearLayout.HORIZONTAL);
        layButton.setLayoutParams(params);

        //确定按钮
        btSave.setTextColor(Color.BLACK);
        btSave.setBackgroundResource(R.mipmap.ic_launcher);//图片top_day
        btSave.setText("确定");
        btSave.setHeight(47);
        btSave.setWidth(160);
        btSave.setGravity(Gravity.CENTER);
        btSave.setClickable(true);

        //取消按钮
        btCancel.setTextColor(Color.BLACK);
        btCancel.setBackgroundResource(R.mipmap.ic_launcher);//图片top_day
        btCancel.setText("取消");
        btCancel.setHeight(45);
        btCancel.setWidth(160);
        btCancel.setGravity(Gravity.CENTER);
        btCancel.setClickable(true);

        // 确定 取消按钮 添加进父布局
        layButton.addView(btSave);
        layButton.addView(btCancel);
        layout.addView(layButton);
        this.setContentView(layout);
    }

    //监听
    private void initListener() {

        //触发radioButton
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                schType_temp = checkedId;

                //选定时间提醒
                new AlertDialog.Builder(ScheduleTypeActivity.this).setTitle("日程类型")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(
                                new String[]{cc.remind[0], cc.remind[1], cc.remind[2], cc.remind[3], cc.remind[4], cc.remind[5], cc.remind[6], cc.remind[7]}
                                , remindID
                                , new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        remind_temp = which;
                                    }
                                })
                        .setPositiveButton("确认", null)
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //触发确定按钮
        btSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sch_typeID = schType_temp;
                remindID = remind_temp;
                Intent intent = new Intent();
                intent.setClass(ScheduleTypeActivity.this, ScheduleAddActivity.class);
                intent.putExtra("schType_remind", new int[]{sch_typeID, remindID});
                setResult(1, intent);
                //消除界面
                ScheduleTypeActivity.this.finish();
            }
        });

        //触发取消按钮
        btCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ScheduleTypeActivity.this, ScheduleAddActivity.class);
                intent.putExtra("schType_remind", new int[]{sch_typeID, remindID});
                setResult(0, intent);
                //消除界面
                ScheduleTypeActivity.this.finish();
            }
        });

    }

}
