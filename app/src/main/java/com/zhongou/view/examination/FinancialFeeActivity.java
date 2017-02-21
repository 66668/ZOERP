package com.zhongou.view.examination;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.view.ContactsSelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 费用申请
 * <p>
 * Created by sjy on 2016/12/2.
 */

public class FinancialFeeActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;


    //金额1
    @ViewInject(id = R.id.et_FeeOne)
    EditText et_FeeOne;

    //用途1
    @ViewInject(id = R.id.et_useage_one)
    EditText et_useage_one;

    //金额2
    @ViewInject(id = R.id.et_FeeTwo)
    EditText et_FeeTwo;

    //用途2
    @ViewInject(id = R.id.et_useageTwo)
    EditText et_useageTwo;

    //金额3
    @ViewInject(id = R.id.et_FeeThree)
    EditText et_FeeThree;

    //用途3
    @ViewInject(id = R.id.et_useageThree)
    EditText et_useageThree;

    //合计
    @ViewInject(id = R.id.et_totle)
    TextView et_totle;

    //备注
    @ViewInject(id = R.id.et_Reason)
    EditText et_remark;

    //添加审批人
    @ViewInject(id = R.id.AddApprover, click = "forAddApprover")
    RelativeLayout AddApprover;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;


    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    //变量
    private String approvalID = "";
    private String useage1 = "";//用途
    private String useage2 = "";
    private String useage3 = "";

    private String fee1 = "";//费用
    private String fee2 = "";
    private String fee3 = "";

    private String total = "";
    private String remark = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_financial_fee);
        tv_title.setText(getResources().getString(R.string.financial_fee));
        initLinstener();//输入监听
    }

    public void forCommit(View view) {

        useage1 = et_useage_one.getText().toString();
        useage2 = et_useageTwo.getText().toString();
        useage3 = et_useageThree.getText().toString();

        fee1 = et_FeeOne.getText().toString();
        fee2 = et_FeeTwo.getText().toString();
        fee3 = et_FeeThree.getText().toString();

        total = et_totle.getText().toString();
        remark = et_remark.getText().toString();

        if (TextUtils.isEmpty(fee1)) {
            PageUtil.DisplayToast(getResources().getString(R.string.financial_reimburse_feeNull));

            return;
        }

        if (TextUtils.isEmpty(useage1)) {
            PageUtil.DisplayToast(getResources().getString(R.string.financial_reimburse_useageNull));
            return;
        }

        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast(getResources().getString(R.string.financial_reimburse_RequesterNull));
            return;
        }


        Loading.run(FinancialFeeActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    js.put("Type", getResources().getString(R.string.financial_fee_apl));//
                    js.put("Total", total);//
                    js.put("Remark", remark);//
                    js.put("ApprovalIDList", approvalID);//
                    js.put("Useageone", useage1);//
                    js.put("Feeone", fee1);//

                    if(!TextUtils.isEmpty(fee2)){
                        js.put("Useagetwo", useage2);//
                        js.put("Feetwo", fee2);//
                    }

                    if (!TextUtils.isEmpty(fee3)) {
                        js.put("Useagethree", useage3);//
                        js.put("Feethree", fee3);//
                    }

                    UserHelper.LRApplicationPost(FinancialFeeActivity.this, js);
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
                PageUtil.DisplayToast(getResources().getString(R.string.approval_success));
                clear();
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }

    private void clear(){
        et_FeeOne.setText("");
        et_FeeTwo.setText("");
        et_FeeThree.setText("");
        et_useage_one.setText("");
        et_useageTwo.setText("");
        et_useageThree.setText("");
        et_totle.setText("");
        et_remark.setText("");
        tv_Requester.setText("");
    }
    /**
     * 添加审批人
     *
     * @param view
     */
    public void forAddApprover(View view) {
        myStartForResult(ContactsSelectActivity.class, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0)//通过请求码(去SActivity)和回传码（回传数据到第一个页面）判断回传的页面
        {
            //判断返回值是否为空
            List<ContactsEmployeeModel> list = new ArrayList<>();
            if (data != null && (List<ContactsEmployeeModel>) data.getSerializableExtra("data") != null) {
                list = (List<ContactsEmployeeModel>) data.getSerializableExtra("data");
            } else {

            }

            StringBuilder name = new StringBuilder();
            StringBuilder employeeId = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                name.append(list.get(i).getsEmployeeName() + "  ");
                employeeId.append(list.get(i).getsEmployeeID() + ",");
            }
            //            approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
            approvalID = getApprovalID(employeeId.toString());
            Log.d("SJY", "approvalID=" + approvalID);
            tv_Requester.setText(name);
        }

    }

    private void initLinstener() {

        et_FeeOne.addTextChangedListener(new TextWatcher() {
            private String fee1;
            private String fee2;
            private String fee3;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("SJY", "ET_beforeTextChanged1");
                fee2 = et_FeeOne.getText().toString();
                fee3 = et_FeeTwo.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("SJY", "ET_onTextChanged1");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("SJY", "ET_afterTextChanged1");
                fee1 = et_FeeThree.getText().toString().trim();
                setTotal(fee1, fee2, fee3);

            }
        });

        et_FeeTwo.addTextChangedListener(new TextWatcher() {
            private String fee1;
            private String fee2;
            private String fee3;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("SJY", "ET_beforeTextChanged2");
                fee1 = et_FeeOne.getText().toString();
                fee3 = et_FeeThree.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("SJY", "ET_onTextChanged2");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("SJY", "ET_afterTextChanged3");
                fee2 = et_FeeTwo.getText().toString().trim();
                setTotal(fee1, fee2, fee3);
            }
        });

        et_FeeThree.addTextChangedListener(new TextWatcher() {
            private String fee1;
            private String fee2;
            private String fee3;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("SJY", "ET_beforeTextChanged3");
                fee1 = et_FeeOne.getText().toString();
                fee2 = et_FeeTwo.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("SJY", "ET_onTextChanged3");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("SJY", "ET_afterTextChanged3");
                fee3 = et_FeeThree.getText().toString().trim();
                setTotal(fee1, fee2, fee3);

            }
        });

    }

    //    // 判断输入的数字和小数点 xml android:inputType="numberDecimal"
    //    private Boolean isDigit(String str) {
    //        if (!"".equals(str)) {
    //            char num[] = str.toCharArray();//把字符串转换为字符数组
    //            for (int i = 0; i < num.length; i++) {
    //                if (Character.isDigit(num[i]) || Character.isDefined('.')) {
    //                    continue;
    //                } else {
    //                    return false;
    //                }
    //            }
    //        }
    //        return true;
    //    }

    /**
     * 赋值
     *
     * @param fee1
     * @param fee2
     * @param fee3
     */
    private void setTotal(String fee1, String fee2, String fee3) {
        Log.d("SJY", "结果------");
        BigDecimal b1 = null;
        BigDecimal b2 = null;
        BigDecimal b3 = null;
        String result = null;

        //
        if (!TextUtils.isEmpty(fee1) && !fee1.equals(".")) {
            b1 = new BigDecimal(fee1);
        } else {
            b1 = new BigDecimal("0");
        }

        //
        if (!TextUtils.isEmpty(fee2) && !fee2.equals(".")) {
            b2 = new BigDecimal(fee2);
        } else {
            b2 = new BigDecimal("0");
        }

        //
        if (!TextUtils.isEmpty(fee3) && !fee1.equals(".")) {
            b3 = new BigDecimal(fee3);
        } else {
            b3 = new BigDecimal("0");
        }
        result = new DecimalFormat("0.0").format(b1.add(b2).add(b3));

        // 结果
        et_totle.setText(result);
    }

    /*
     *处理字符串，去除末尾逗号
     */
    private String getApprovalID(String str) {
        if (str.length() > 1) {
            return str.substring(0, str.length() - 1);
        } else {
            return "";
        }
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
