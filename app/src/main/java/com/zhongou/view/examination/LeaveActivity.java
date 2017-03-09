package com.zhongou.view.examination;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.utils.CameraGalleryUtils;
import com.zhongou.utils.ImageUtils;
import com.zhongou.utils.PageUtil;
import com.zhongou.view.ContactsSelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 请假
 * Created by sjy on 2016/12/2.
 */

public class LeaveActivity extends BaseActivity implements CameraGalleryUtils.ChoosePicCallBack {
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
    EditText et_reason;

    //备注
    @ViewInject(id = R.id.et_remark)
    EditText et_remark;

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

    //添加图片
    @ViewInject(id = R.id.addPicture, click = "ForAddPicture")
    RelativeLayout addPicture;

    //添加审批人
    @ViewInject(id = R.id.AddApprover, click = "forAddApprover")
    RelativeLayout AddApprover;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

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


    //变量
    private String startDate;
    private String endDates;
    private String reason;
    private String remark = "";
    private String applicationTitle = "";//标题
    private String approvalID = "";
    private CameraGalleryUtils cameraGalleryUtils;// 头像上传工具
    private String picPath;
    private File filePicPath;
    private List<Bitmap> listPic;

    //常量
    public static final int POST_SUCCESS = 15;
    public static final int POST_FAILED = 16;
    public static final int PIC_SHOW = 17;//图片展示


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_forleave);

        initMyView();
    }

    private void initMyView() {
        tv_title.setText(getResources().getString(R.string.leave));
        cameraGalleryUtils = new CameraGalleryUtils(this, this);
        listPic = new ArrayList<>();
    }

    public void forCommit(View view) {
        reason = et_reason.getText().toString();
        remark = et_remark.getText().toString();
        applicationTitle = et_ApplicationTitle.getText().toString().trim();

        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDates)) {
            PageUtil.DisplayToast("请假时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(reason)) {
            PageUtil.DisplayToast("请假原因不能为空");
            return;
        }
        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为空");
            return;
        }
        Loading.run(LeaveActivity.this, new Runnable() {
            @Override
            public void run() {
                try {


                    JSONObject js = new JSONObject();
                    js.put("ApplicationTitle", applicationTitle);
                    js.put("Content", reason);
                    js.put("StartDate", startDate);
                    js.put("EndDate", endDates);
                    js.put("Remark", remark);
                    js.put("Reason", remark);
                    js.put("ApprovalIDList", approvalID);

                    UserHelper.leavePost(LeaveActivity.this, js, filePicPath);
                    sendMessage(POST_SUCCESS);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());

                } catch (JSONException e) {
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
        et_reason.setText("");
        et_remark.setText("");
        tv_timeStart.setText("");
        tv_timeEnd.setText("");
        et_ApplicationTitle.setText("");
        tv_Requester.setText("");
        startDate = null;
        endDates = null;
        approvalID = null;
        listPic.clear();//清空数据展示
        img_01.setImageBitmap(null);
        img_02.setImageBitmap(null);
        img_03.setImageBitmap(null);
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
        if (requestCode == 0 && resultCode == 0) {
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
