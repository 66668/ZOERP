package com.zhongou.view.examination;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
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
import com.zhongou.utils.PageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjy on 2016/12/2.
 */

public class SalaryadjustActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //调薪人
    @ViewInject(id= R.id.et_person)
    EditText et_person;

    //目前薪资
    @ViewInject(id= R.id.salary_now)
    EditText et_salary_now;

    //调后薪资
    @ViewInject(id= R.id.salary_after)
    EditText et_salary_after;

    //原因
    @ViewInject(id= R.id.et_reason)
    EditText et_reason;

    //变量
    private String approvalID = "";
    private String TargetEmployee ;
    private String reason ;
    private String OriSalary;//调前工资
    private String SrcSalary;//调后工资
    private List<String> approvalIDList = new ArrayList<String>();

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_salaryadjust);
        tv_title.setText(getResources().getString(R.string.salaryAdjust));
    }

    public void forCommit(View view) {
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
        TargetEmployee = et_person.getText().toString().trim();
        reason =et_reason.getText().toString();
        OriSalary =et_salary_now.getText().toString();
        SrcSalary =et_salary_after.getText().toString();

        if (TextUtils.isEmpty(TargetEmployee)) {
            PageUtil.DisplayToast("员工不能为空");
            return;
        }
        if (TextUtils.isEmpty(OriSalary) || TextUtils.isEmpty(SrcSalary)) {
            PageUtil.DisplayToast("薪资不能为空");
            return;
        }
        if (TextUtils.isEmpty(reason)) {
            PageUtil.DisplayToast("薪资说明不能为空");
            return;
        }

        Loading.run(SalaryadjustActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    js.put("TargetEmployee", TargetEmployee);
                    js.put("OriSalary", OriSalary);
                    js.put("SrcSalary", SrcSalary);
                    js.put("Reason", reason);
                    js.put("ApprovalIDList", approvalID);//

                    UserHelper.changeSalary(SalaryadjustActivity.this, js);
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
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }

}
