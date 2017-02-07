package com.zhongou.view.examination;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * 借阅
 * Created by sjy on 2016/12/2.
 */

public class BorrowActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //说明
    @ViewInject(id = R.id.et_reason)
    EditText et_reason;

    //类型
    @ViewInject(id = R.id.layout_borrowType, click = "borrowType")
    LinearLayout layout_borrowType;
    @ViewInject(id = R.id.tv_borrowType)
    TextView tv_borrowType;

    //借阅名称
    @ViewInject(id = R.id.et_BorrowThings)
    EditText et_BorrowThings;

    //开始时间
    @ViewInject(id = R.id.layout_startTime, click = "startTime")
    LinearLayout layout_startTime;
    @ViewInject(id = R.id.tv_timeStart)
    TextView tv_timeStart;


    //结束时间
    @ViewInject(id = R.id.layout_endTime, click = "endTime")
    LinearLayout layout_endTime;
    @ViewInject(id = R.id.tv_timeEnd)
    TextView tv_timeEnd;

    //添加审批人
    @ViewInject(id = R.id.AddApprover, click = "forAddApprover")
    RelativeLayout AddApprover;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;


    //变量
    public String BorrowThings;
    public String BorrowType;
    public String startDate;
    public String endDates;
    public String reason;
    public String remark = "null";
    public String applicationTitle = "";
    private String approvalID = "";
    private List<String> approvalIDList = new ArrayList<String>();
    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_borrow);
        tv_title.setText(getResources().getString(R.string.borrows));

        //spinner调用
        //        sp_borrowType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //            @Override
        //            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //                BorrowType = getResources().getStringArray(R.array.spBorrowType)[position];
        //            }
        //
        //            @Override
        //            public void onNothingSelected(AdapterView<?> parent) {
        //            }
        //        });
    }

    public void forCommit(View view) {
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
        BorrowThings = et_BorrowThings.getText().toString();
        reason = et_reason.getText().toString();
        if (TextUtils.isEmpty(BorrowThings)) {
            PageUtil.DisplayToast("借阅名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(BorrowType)) {
            PageUtil.DisplayToast("借阅类型不能为空");
            return;
        }
        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDates)) {
            PageUtil.DisplayToast("时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(reason)) {
            PageUtil.DisplayToast("借阅说明不能为空");
            return;
        }
        Loading.run(BorrowActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    js.put("BorrowThings", BorrowThings);
                    js.put("BorrowType", BorrowType);
                    js.put("StartTime", startDate);
                    js.put("FinishTime", endDates);
                    js.put("Remark", remark);
                    js.put("Reason", reason);
                    js.put("ApprovalIDList", approvalID);

                    UserHelper.borrowPost(BorrowActivity.this, js);
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
     * 类型
     *
     * @param
     */
    public void borrowType(View view) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(BorrowActivity.this);
        buidler.setTitle(getResources().getString(R.string.borrowsType));
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        final String[] data = getResources().getStringArray(R.array.spBorrowType);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BorrowType = data[which];
                tv_borrowType.setText(BorrowType.trim());
                dialog.dismiss();
            }
        });
        buidler.show();
    }

    /**
     * 开始时间
     *
     * @param view
     */
    public void startTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(BorrowActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        startDate = time;
                        tv_timeStart.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("开始时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 结束时间
     *
     * @param view
     */
    public void endTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(BorrowActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        endDates = time;
                        tv_timeEnd.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("结束时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 添加审批人
     *
     * @param view
     */
    public void forAddApprover(View view) {
//        myStartForResult(ContactsActivity.class,);
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
