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
import com.zhongou.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 招聘
 * Created by sjy on 2016/12/2.
 */

public class RecruitmentActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //职位名称
    @ViewInject(id = R.id.et_positionRequest)
    EditText et_positionRequest;

    //个数
    @ViewInject(id = R.id.et_requestNumber)
    EditText et_numberOfPeople;
    //时间
    @ViewInject(id = R.id.layout_time, click = "chooseTime")
    LinearLayout layout_time;

    @ViewInject(id = R.id.tv_timeIn)
    TextView sp_timeIn;

    //Responsibility
    @ViewInject(id = R.id.et_responsibility)
    EditText et_responsibility;

    //变量
    private String position;//职位名称
    private String numberOfPeople;
    private String responsibility;
    private String ExpectedEntryDate;//时间
    private String remark = "";//备注
    private String approvalID = "";
    private List<String> approvalIDList = new ArrayList<String>();

    //常量
    public static final int POST_SUCCESS = 11;
    public static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_recruitment);
        tv_title.setText(getResources().getString(R.string.forjobs));

    }

    /**
     * 选择入职时间
     *
     * @param view
     */
    public void chooseTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(RecruitmentActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        ExpectedEntryDate = time;
                        sp_timeIn.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("入职时间");
        endDateChooseDialog.showDateChooseDialog();
    }


    /**
     * 提交
     */
    public void forCommit(View view) {
        Log.d("SJY", "Utils.getCurrentTime()=" + Utils.getCurrentTime());
        position = et_positionRequest.getText().toString().trim();
        numberOfPeople = et_numberOfPeople.getText().toString().trim();
        responsibility = et_responsibility.getText().toString().trim();
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
        if (TextUtils.isEmpty(position)) {
            PageUtil.DisplayToast("招聘职位不能为空");
            return;
        }
        if (TextUtils.isEmpty(numberOfPeople)) {
            PageUtil.DisplayToast("招聘人数不能为空");
            return;
        }
        if (TextUtils.isEmpty(responsibility)) {
            PageUtil.DisplayToast("职位职责不能为空");
            return;
        }

        Loading.run(RecruitmentActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    js.put("Position", position);//职位名称
                    js.put("NumberOfPeople", numberOfPeople);//人数
                    js.put("Responsibility", responsibility);//职责
                    js.put("ExpectedEntryDate", ExpectedEntryDate);
                    js.put("Remark", remark);
                    js.put("ApprovalIDList", approvalID);//审批人

                    UserHelper.recruitmentPost(RecruitmentActivity.this, js);
                    sendMessage(POST_SUCCESS);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());

                } catch (JSONException e) {
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
     * back
     */
    public void forBack(View v) {
        this.finish();
    }
}
