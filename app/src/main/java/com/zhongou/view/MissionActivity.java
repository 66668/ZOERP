package com.zhongou.view;

import android.os.Bundle;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

/**
 * Created by JackSong on 2016/10/25.
 */

public class MissionActivity extends BaseActivity {
    @ViewInject(id = R.id.tv_right)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_mission);
        tv_title.setText("任务");
    }

}
