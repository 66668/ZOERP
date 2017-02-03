package com.zhongou.view;

import android.os.Bundle;
import android.widget.ListView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

/**
 * Created by JackSong on 2016/10/25.
 */

public class WorkPlanAcitivity extends BaseActivity {

    @ViewInject(id = R.id.list_workplan, click = "listDetail")
    ListView list_workplan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_workplan);
    }
//    public void listDetail(View view){
//        startActivity(WorkPlanDetailActivity.class);
//    }
}
