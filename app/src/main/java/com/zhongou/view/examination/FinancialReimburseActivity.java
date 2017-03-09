package com.zhongou.view.examination;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.utils.CameraGalleryUtils;
import com.zhongou.utils.ImageUtils;
import com.zhongou.utils.PageUtil;
import com.zhongou.view.ContactsSelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 报销申请
 * Created by sjy on 2016/12/2.
 */

public class FinancialReimburseActivity extends BaseActivity implements CameraGalleryUtils.ChoosePicCallBack{
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
    @ViewInject(id = R.id.et_remark)
    EditText et_remark;

    //添加审批人
    @ViewInject(id = R.id.AddApprover, click = "forAddApprover")
    RelativeLayout AddApprover;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    //添加图片
    @ViewInject(id = R.id.addPicture, click = "ForAddPicture")
    RelativeLayout addPicture;

    //图片1
    @ViewInject(id = R.id.layout_img_01, click = "showDetailImg")
    RelativeLayout layout_img_01;
    @ViewInject(id = R.id.img_01)
    ImageView img_01;

    //图片2
    @ViewInject(id = R.id.layout_img_02, click = "showDetailImg")
    RelativeLayout layout_img_02;
    @ViewInject(id = R.id.img_02)
    ImageView img_02;

    //图片3
    @ViewInject(id = R.id.layout_img_03, click = "showDetailImg")
    RelativeLayout layout_img_03;
    @ViewInject(id = R.id.img_03)
    ImageView img_03;

    //还款时间
    //    @ViewInject(id = R.id.et_Reason)
    //    EditText et_Reason;

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;
    public static final int PIC_SHOW = 23;



    //变量
    private CameraGalleryUtils cameraGalleryUtils;// 头像上传工具
    private String picPath;
    private File filePicPath;
    private List<Bitmap> listPic;
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
        setContentView(R.layout.act_apps_examination_financial_reimburse);
        tv_title.setText(getResources().getString(R.string.financial_reimburse));
        cameraGalleryUtils = new CameraGalleryUtils(this, this);
        listPic = new ArrayList<>();
        initLinstener();//输入监听

    }


    /**
     * 提交申请
     *
     * @param view
     */

    public void forCommit(View view) {
        useage1 = et_useage_one.getText().toString();
        useage2 = et_useageTwo.getText().toString();
        useage3 = et_useageThree.getText().toString();

        fee1 = et_FeeOne.getText().toString();
        fee2 = et_FeeTwo.getText().toString();
        fee3 = et_FeeThree.getText().toString();

        total = et_totle.getText().toString();
        remark = et_remark.getText().toString();

        if (TextUtils.isEmpty(total)) {
            PageUtil.DisplayToast("总额为空");

            return;
        }
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


        Loading.run(FinancialReimburseActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    //
                    JSONObject js = new JSONObject();
                    js.put("Type", getResources().getString(R.string.financial_reimburse_apl));//
                    js.put("Total", total);//
                    js.put("Remark", remark);//
                    js.put("ApprovalIDList", approvalID);//
                    js.put("Useageone", useage1);//
                    js.put("Feeone", fee1);//

                    if (!TextUtils.isEmpty(fee2)) {
                        js.put("Useagetwo", useage2);//
                        js.put("Feetwo", fee2);//
                    }

                    if (!TextUtils.isEmpty(fee3)) {
                        js.put("Useagethree", useage3);//
                        js.put("Feethree", fee3);//
                    }

                    UserHelper.LRApplicationPost(FinancialReimburseActivity.this, js,filePicPath);
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
            case PIC_SHOW://添加图片后，展示
                List<Bitmap> listpic = (List<Bitmap>) msg.obj;

                if (listpic.size() == 3) {
                    img_01.setVisibility(View.VISIBLE);
                    img_02.setVisibility(View.VISIBLE);
                    img_03.setVisibility(View.VISIBLE);

                    img_01.setImageBitmap(listpic.get(0));
                    img_02.setImageBitmap(listpic.get(1));
                    img_03.setImageBitmap(listpic.get(2));
                }
                if (listpic.size() == 2) {
                    img_01.setVisibility(View.VISIBLE);
                    img_02.setVisibility(View.VISIBLE);
                    img_03.setVisibility(View.INVISIBLE);

                    img_01.setImageBitmap(listpic.get(0));

                    img_02.setImageBitmap(listpic.get(1));

                }

                if (listpic.size() == 1) {
                    img_01.setVisibility(View.VISIBLE);
                    img_02.setVisibility(View.INVISIBLE);
                    img_03.setVisibility(View.INVISIBLE);

                    img_01.setImageBitmap(listpic.get(0));
                }
                break;
        }
    }

    private void clear() {
        et_FeeOne.setText("");
        et_FeeTwo.setText("");
        et_FeeThree.setText("");
        et_useage_one.setText("");
        et_useageTwo.setText("");
        et_useageThree.setText("");
        et_totle.setText("");
        et_remark.setText("");
        tv_Requester.setText("");

        img_01.setImageBitmap(null);
        img_02.setImageBitmap(null);
        img_03.setImageBitmap(null);
    }

    /**
     * 添加审批人
     *
     * @param view
     */
    public void forAddApprover(View view) {
        myStartForResult(ContactsSelectActivity.class, 0);
    }

    /**
     * 添加图片
     */
    public void ForAddPicture(View view) {
        if (listPic.size() >= 1) {
            PageUtil.DisplayToast("最多添加1张图片");
            return;
        }
        cameraGalleryUtils.showChoosePhotoDialog(CameraGalleryUtils.IMG_TYPE_CAMERA);
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

        //相册
        cameraGalleryUtils.onActivityResultAction(requestCode, resultCode, data);
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

    /**
     * back
     *
     * @param view
     */

    public void forBack(View view) {
        this.finish();
    }

    @Override
    public void updateAvatarSuccess(int updateType, String picpath, String avatarBase64) {
        picPath = picpath;

        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
        Uri uri = ImageUtils.savePicture(this, bitmap);
        filePicPath = new File(ImageUtils.getImageAbsolutePath(this, uri));

        listPic.add(bitmap);
        sendMessage(PIC_SHOW, listPic);
    }

    @Override
    public void updateAvatarFailed(int updateType) {

    }

    @Override
    public void cancel() {

    }
}
