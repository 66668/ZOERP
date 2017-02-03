package com.zhongou.view;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

/**
 * 外出考勤
 * Created by JackSong on 2016/10/25.
 */

public class MapAttendAcitivity extends BaseActivity {

    //back
    @ViewInject(id = R.id.imgBack, click = "forBack")
    ImageView imgBack;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tvTitle;

    @ViewInject(id = R.id.tv_right)
    TextView tvRight;
    //考勤记录
    @ViewInject(id = R.id.tv_AttendRecord, click = "forRecord")
    TextView tv_AttendRecord;

    //地图考勤
    @ViewInject(id = R.id.img_mapAttend, click = "forMapAttend")
    ImageView img_mapAttend;

    //时间显示
    @ViewInject(id = R.id.tv_time)
    TextView tv_Time;

    //日期显示
    @ViewInject(id = R.id.tv_date)
    TextView tv_Date;

    private final int LOGIN_SUCESS = 2001; // 登陆成功
    private ImageView forMapAttend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_attendance);
        tvTitle.setText(getResources().getString(R.string.mapAttendance));
        tvRight.setText("");

        forMapAttend = (ImageView) findViewById(R.id.img_mapAttend);
        forMapAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage(LOGIN_SUCESS);
            }
        });
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case LOGIN_SUCESS: // 1001
                startActivity(MapLocationActivity.class);
                break;

            default:
                break;
        }
    }


    public void forBack(View view) {
        this.finish();
    }

    public void forRecord(View view) {
        startActivity(MapAttendRecordActivity.class);
    }

}
