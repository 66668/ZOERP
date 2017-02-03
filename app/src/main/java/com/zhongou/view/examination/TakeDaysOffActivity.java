package com.zhongou.view.examination;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
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
 * 调休
 * Created by sjy on 2016/12/2.
 */

public class TakeDaysOffActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //调休开始时间
    @ViewInject(id = R.id.layout_startTime, click = "startTime")
    LinearLayout layout_startTime;
    @ViewInject(id = R.id.tv_timeStart)
    TextView tv_timeStart;


    //调休结束时间
    @ViewInject(id = R.id.layout_end, click = "endTime")
    LinearLayout layout_end;
    @ViewInject(id = R.id.tv_timeEnd)
    TextView tv_timeEnd;

    //原工作开始时间
    @ViewInject(id = R.id.layout_startTimebefore, click = "startTimeBefore")
    LinearLayout layout_startTimebefore;
    @ViewInject(id = R.id.tv_timeStartbefore)
    TextView tv_timeStartbefore;


    //原工作调休结束时间
    @ViewInject(id = R.id.layout_endTimebefore, click = "endTimeBefore")
    LinearLayout layout_endTimebefore;
    @ViewInject(id = R.id.tv_timeEndbefore)
    TextView tv_timeEndBefore;

    //原因
    @ViewInject(id = R.id.et_reason)
    EditText et_reason;


    private String StartOffDate;
    private String EndOffDate;
    private String StartTakeDate;
    private String EndTakeDate;
    private String reason;
    private String remark = "";
    private String approvalID = "";
    private List<String> approvalIDList = new ArrayList<String>();

    //常量
    public static final int POST_SUCCESS = 19;
    public static final int POST_FAILED = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_takedaysoff);
        tv_title.setText(getResources().getString(R.string.workRest));

    }

    /**
     * 提交
     */
    public void forCommit(View view) {
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
        reason  = et_reason.getText().toString();
        if (TextUtils.isEmpty(StartOffDate) || TextUtils.isEmpty(EndOffDate)
                || TextUtils.isEmpty(StartTakeDate) ||TextUtils.isEmpty(EndTakeDate)) {
            PageUtil.DisplayToast("时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(reason)) {
            PageUtil.DisplayToast("调休原因不能为空");
            return;
        }
        Loading.run(TakeDaysOffActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    js.put("StartTakeDate", StartTakeDate);
                    js.put("EndTakeDate", EndTakeDate);
                    js.put("StartOffDate", StartOffDate);
                    js.put("EndOffDate", EndOffDate);
                    js.put("Reason", reason);
                    js.put("Remark", remark);
                    js.put("ApprovalIDList", approvalID);

                    UserHelper.takeDaysOffPost(TakeDaysOffActivity.this, js);
                    sendMessage(POST_SUCCESS);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());

                }catch (JSONException e) {
                    e.printStackTrace();
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
     * 调休开始时间
     *
     * @param view
     */
    public void startTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(TakeDaysOffActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        StartOffDate = time;
                        tv_timeStart.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("开始时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 调休结束时间
     *
     * @param view
     */
    public void endTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(TakeDaysOffActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        EndOffDate = time;
                        tv_timeEnd.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("结束时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 原工作开始时间
     *
     * @param view
     */
    public void startTimeBefore(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(TakeDaysOffActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        StartTakeDate = time;
                        tv_timeStartbefore.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("开始时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 原工作结束时间
     *
     * @param view
     */
    public void endTimeBefore(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(TakeDaysOffActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        EndTakeDate = time;
                        tv_timeEndBefore.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("结束时间");
        endDateChooseDialog.showDateChooseDialog();
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
