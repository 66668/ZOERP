package com.zhongou.view.examination;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.DateChooseWheelViewDialog;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.utils.PageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sjy on 2016/12/2.
 * 请假
 */

public class LeaveActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;


    //请假原因
    @ViewInject(id = R.id.et_reason)
    TextView et_reason;

    //开始时间
    @ViewInject(id = R.id.layout_startTime, click = "startTime")
    LinearLayout layout_startTime;
    @ViewInject(id = R.id.tv_timeStart)
    TextView tv_timeStart;


    //结束时间
    @ViewInject(id = R.id.layout_end, click = "endTime")
    LinearLayout layout_end;
    @ViewInject(id = R.id.tv_timeEnd)
    TextView tv_timeEnd;

    //标题
    @ViewInject(id = R.id.et_ApplicationTitle)
    EditText et_ApplicationTitle;


    //变量
    private String startDate;
    private String endDates;
    private String content;
    private String remark = "";
    private String applicationTitle = "";//标题
    private String approvalID = "";
    private List<String> approvalIDList = new ArrayList<String>();


    //常量
    public static final int POST_SUCCESS = 15;
    public static final int POST_FAILED = 16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_forleave);
        tv_title.setText(getResources().getString(R.string.leave));
    }

    public void forCommit(View view) {
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
        content = et_reason.getText().toString();
        applicationTitle = et_ApplicationTitle.getText().toString().trim();
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";

        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDates)) {
            PageUtil.DisplayToast("请假时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            PageUtil.DisplayToast("请假原因不能为空");
            return;
        }
        Loading.run(LeaveActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    js.put("ApplicationTitle", applicationTitle);
                    js.put("Content", content);
                    js.put("StartDate", startDate);
                    js.put("EndDate", endDates);
                    js.put("Remark", remark);
                    js.put("ApprovalIDList", approvalID);

                    UserHelper.leavePost(LeaveActivity.this, js);
                    sendMessage(POST_SUCCESS);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());

                } catch (JSONException e){
                    Log.d("SJY", e.getMessage());
                }
            }
        });

    }
    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                PageUtil.DisplayToast("成功提交！");
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }


    /**
     * 开始时间
     *
     * @param view
     */
    public void startTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(LeaveActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        startDate = time;
                        tv_timeStart.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("开始时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 结束时间
     *
     * @param view
     */
    public void endTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(LeaveActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        endDates = time;
                        tv_timeEnd.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("结束时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * back
     * @param view
     */
    public void forBack(View view){
        this.finish();
    }

}
