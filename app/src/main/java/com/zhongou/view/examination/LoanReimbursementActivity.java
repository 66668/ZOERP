package com.zhongou.view.examination;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
 */

public class LoanReimbursementActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //申请类型
    @ViewInject(id = R.id.layout_loanreimType, click = "loanreimType")
    LinearLayout layout_loanreimType;
    @ViewInject(id = R.id.tv_loanreimType)
    TextView tv_loanreimType;

    //申请类型
    @ViewInject(id = R.id.layout_useType, click = "useType")
    LinearLayout layout_useType;
    @ViewInject(id = R.id.tv_useType)
    TextView tv_useType;

    //方式
    @ViewInject(id = R.id.layout_wayType, click = "wayType")
    LinearLayout layout_wayType;
    @ViewInject(id = R.id.tv_wayType)
    TextView tv_wayType;

    //金额
    @ViewInject(id = R.id.et_Fee)
    EditText et_Fee;

    //说明
    @ViewInject(id = R.id.et_Reason)
    EditText et_Reason;


    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    //变量
    private String approvalID = "";
    private String Type = "";
    private String Useage = "";
    private String way = "";
    private String fee = "";
    private String reason = "";
    private List<String> approvalIDList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_loanreimbursement);
        tv_title.setText(getResources().getString(R.string.loanReimburse));

    }

    public void forCommit(View view) {
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
        fee = et_Fee.getText().toString().trim();
        reason = et_Reason.getText().toString();
        Log.d("SJY", Type + "\n" + way + "\n" + Useage);
        if (TextUtils.isEmpty(Type)) {
            PageUtil.DisplayToast("申请类型不能为空");
            return;
        }
        if (TextUtils.isEmpty(Useage)) {
            PageUtil.DisplayToast("用途类型不能为空");
            return;
        }

        if (TextUtils.isEmpty(way)) {
            PageUtil.DisplayToast("方式不能为空");
            return;
        }
        if (TextUtils.isEmpty(fee)) {
            PageUtil.DisplayToast("金额不能为空");
            return;
        }


        Loading.run(LoanReimbursementActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    js.put("Type", Type);
                    js.put("Way", way);
                    js.put("Fee", fee);
                    js.put("Useage", Useage);
                    js.put("Reason", reason);
                    js.put("ApprovalIDList", approvalID);//

                    UserHelper.LRApplicationPost(LoanReimbursementActivity.this, js);
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
     * 报销类型
     */
    public void loanreimType(View view) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setTitle(getResources().getString(R.string.loanReimburseType));
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        final String[] data = getResources().getStringArray(R.array.spLoanreimType);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Type = data[which];
                tv_loanreimType.setText(Type.trim());
                dialog.dismiss();
            }
        });
        buidler.show();
    }

    /**
     * 用途类型
     */
    public void useType(View view) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setTitle(getResources().getString(R.string.loanReimburseUseType));
        final String[] data = getResources().getStringArray(R.array.spLoanreimUseageType);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Useage = data[which];
                tv_useType.setText(Useage.trim());
                dialog.dismiss();
            }
        });
        buidler.show();
    }

    /**
     * 方式
     */
    public void wayType(View view) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setTitle(getResources().getString(R.string.loanReimburseUsestyle));
        final String[] data = getResources().getStringArray(R.array.spLoanreimWay);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                way = data[which];
                tv_wayType.setText(way.trim());
                dialog.dismiss();
            }
        });
        buidler.show();
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
