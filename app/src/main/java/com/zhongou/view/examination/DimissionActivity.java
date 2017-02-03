package com.zhongou.view.examination;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
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
 * 离职
 * Created by sjy on 2016/12/2.
 */

public class DimissionActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //提交
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //开始时间
    @ViewInject(id = R.id.layout_timestart, click = "startTime")
    LinearLayout layout_timestart;
    @ViewInject(id = R.id.tv_jobsLeaveTimeIn)
    TextView tv_jobsLeaveTimeIn;

    //结束时间
    @ViewInject(id = R.id.layout_timeend, click = "endTime")
    LinearLayout layout_timeend;
    //原因
    @ViewInject(id = R.id.et_reason)
    TextView et_reason;

    //离职类型
    @ViewInject(id = R.id.layout_dismissionType, click = "dismissionType")
    LinearLayout layout_dismissionType;
    @ViewInject(id = R.id.tv_dismissiontype)
    TextView tv_dismissiontype;

    @ViewInject(id = R.id.tv_jobsLeavetimeOut)
    TextView tv_jobsLeavetimeOut;

    private String EntryDate;//入职时间
    private String DimissionDate;//离职时间
    private String content;//原因
    private String approvalID = "";
    private String remark = "";
    private String dimissionID = "";//离职类型
    private List<String> approvalIDList = new ArrayList<String>();

    //常量
    public static final int POST_SUCCESS = 15;
    public static final int POST_FAILED = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_dismission);
        tv_title.setText(getResources().getString(R.string.jobsForLeave));

    }

    /**
     * 提交
     *
     * @param v
     */
    public void forCommit(View v) {
        content = et_reason.getText().toString();
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
        if (TextUtils.isEmpty(dimissionID)) {
            PageUtil.DisplayToast("离职类型不能为空");
            return;
        }
        if (TextUtils.isEmpty(DimissionDate)) {
            PageUtil.DisplayToast("离职时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            PageUtil.DisplayToast("离职说明不能为空");
            return;
        }

        Loading.run(DimissionActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    js.put("DimissionID", dimissionID);//离职类型
                    js.put("Content", content);//原因
                    js.put("EntryDate", EntryDate);
                    js.put("DimissionDate", DimissionDate);
                    js.put("Remak", remark);
                    js.put("ApprovalIDList", approvalID);

                    UserHelper.dimissionPost(DimissionActivity.this, js);

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
     * 离职类型
     */
    public void dismissionType(View view){
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setTitle(getResources().getString(R.string.jobsleavetype));
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        final String[] data = getResources().getStringArray(R.array.spDimissionID);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dimissionID = data[which];
                tv_dismissiontype.setText(dimissionID.trim());
                dialog.dismiss();
            }
        });
        buidler.show();
    }
    /**
     * 入职时间
     *
     * @param v
     */

    public void startTime(View v) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(DimissionActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        EntryDate = time;
                        tv_jobsLeaveTimeIn.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("入职时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 离职时间
     *
     * @param v
     */
    public void endTime(View v) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(DimissionActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        DimissionDate = time;
                        tv_jobsLeavetimeOut.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("离职时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     *
     */
    public void forBack(View view) {
        this.finish();
    }
}
