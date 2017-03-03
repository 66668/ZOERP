package com.zhongou.view.examination.approvaldetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.zhongou.R;
import com.zhongou.adapter.ContactsSelectAdapter;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.CharacterParser;
import com.zhongou.common.MyException;
import com.zhongou.common.PinyinComparator;
import com.zhongou.db.sqlite.SQLiteCopytoContactdb;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ApprovalSModel;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 抄送 部门-联系人通讯录
 * <p>
 * listView绑定checkBox
 * <p>
 * Created by sjy on 2017/1/17.
 */

public class CommonCopytoEplActivity extends BaseActivity {

    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCopyto")
    TextView tv_right;

    //listView
    @ViewInject(id = R.id.country_lvcountry)
    ListView contactsListView;

    //变量
    private String sDeptID;//
    private MyApprovalModel myApprovalModel;//跳转对象
    private ApprovalSModel approvalSModel;//提交对象
    private String sApprovalemployeeinfos;//转发人ApprovalEmployeeID

    private SideBar sideBar;
    private CharacterParser characterParser;// 汉字转换成拼音的类
    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private static List<ContactsEmployeeModel> listData;//审批人通讯录 集合
    public static List<ContactsEmployeeModel> selectlist;//checkBox选中数据集合

    private ContactsSelectAdapter adapter;//通讯录排序适配
    private SQLiteCopytoContactdb dao; //sql数据库雷

    //常量
    public static final int POST_SUCCESEE = 15;
    public static final int POST_FAILED = 16;
    public static final int CHASE_DATA = 17;
    public static final int POSTDATA_SUCCESS = 18;//数据转交
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addACT(this);//多界面管理

        setContentView(R.layout.act_apps_examination_myapproval_common_contacts2);
        tv_title.setText(getResources().getString(R.string.examination_copyto));

        //获取调转对象

        Intent intent = getIntent();
        sDeptID = intent.getStringExtra("sDeptID");
        myApprovalModel = (MyApprovalModel) intent.getSerializableExtra("myApprovalModel");
        Log.d("SJY", "--" + myApprovalModel.getApprovalID() + "--");
        initViews();
        initListener();

        getData();
    }

    /**
     *
     */
    private void initViews() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dao = new SQLiteCopytoContactdb(this);
    }

    /**
     * 控件监听
     */
    private void initListener() {

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    contactsListView.setSelection(position);
                }

            }
        });

        //listView绑定checkbox监听
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //因headerView，需要修改position,易错地方，注意postion修改就可以
                int headerViewsCount = contactsListView.getHeaderViewsCount();
                int newPosition = position - headerViewsCount;

                //判断view是否相等
                if (view.getTag() instanceof ContactsSelectAdapter.MyViewHolder) {
                    //如果是的话，重用
                    ContactsSelectAdapter.MyViewHolder holder = (ContactsSelectAdapter.MyViewHolder) view.getTag();
                    //自动触发
                    holder.selectCheck.toggle();
                }
            }
        });

    }

    public void getData() {
        //先判断sql中是否有值
        List<ContactsEmployeeModel> list = dao.getEmpList(sDeptID);
        if (list.size() > 0 && list != null) {
            Log.d("SJY", "走sp缓存");
            sendMessage(CHASE_DATA, list);

        } else if (list == null || list.size() <= 0) {
            Log.d("SJY", "走服务端数据");
            //获取服务端数据
            getDataFromURL();

        }
    }

    public void getDataFromURL() {
        //
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    //获取联系人集合
                    List<ContactsEmployeeModel> list = UserHelper.getContractsEmployeeOfDept(CommonCopytoEplActivity.this, sDeptID);

                    sendMessage(POST_SUCCESEE, list);

                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESEE:// 服务端数据处理


                listData = (List<ContactsEmployeeModel>) msg.obj;

                dao.addEmplList(listData, sDeptID);//联系人保存到sql

                //为数据添加首字母
                listData = filledData(listData);
                // 根据a-z进行排序源数据
                Collections.sort(listData, pinyinComparator);
                adapter = new ContactsSelectAdapter(this, listData);

                //数据展示
                contactsListView.setAdapter(adapter);
                break;

            case CHASE_DATA://缓存数据处理

                listData = (List<ContactsEmployeeModel>) msg.obj;

                //为数据添加首字母
                listData = filledData(listData);
                // 根据a-z进行排序源数据
                Collections.sort(listData, pinyinComparator);
                adapter = new ContactsSelectAdapter(this, listData);

                //数据展示
                contactsListView.setAdapter(adapter);
                break;

            case POSTDATA_SUCCESS://提交
                PageUtil.DisplayToast((String) msg.obj);

                //需要消除三个界面
                MyApplication.getInstance().closeACT();

                break;

            case POST_FAILED://
                PageUtil.DisplayToast((String) msg.obj);
                break;

            default:
                break;
        }
    }

    /**
     * 重新修改model,为ListView填充首字母数据
     *
     * @return
     */
    private List<ContactsEmployeeModel> filledData(List<ContactsEmployeeModel> listdata) {
        List<ContactsEmployeeModel> mSortList = new ArrayList<ContactsEmployeeModel>();

        for (int i = 0; i < listdata.size(); i++) {

            //汉字转换成拼音
            String pinyin = characterParser.getSelling(listdata.get(i).getsEmployeeName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                listdata.get(i).setFirstLetter(sortString.toUpperCase());
            } else {
                listdata.get(i).setFirstLetter("#");
            }

            mSortList.add(listdata.get(i));
        }
        return mSortList;

    }


    /**
     * 抄送
     *
     * @param view
     */

    public void forCopyto(View view) {

        selectlist = getSelectList(listData);//获取 抄送人
        sApprovalemployeeinfos = getList2String(selectlist);//获取
        Log.d("SJY", "转发-确定sApprovalemployeeinfos=" + sApprovalemployeeinfos);

        if (TextUtils.isEmpty(sApprovalemployeeinfos)) {
            PageUtil.DisplayToast("抄送人不能为空");
            return;
        }

        //对象赋值处理
        approvalSModel = new ApprovalSModel();
        approvalSModel.setsApplicationid(myApprovalModel.getApprovalID());
        approvalSModel.setsComment(myApprovalModel.getComment());
        approvalSModel.setsApplicationid(myApprovalModel.getApplicationID());
        approvalSModel.setsApplicationtype(myApprovalModel.getApplicationType());
        approvalSModel.setsEmployeeid(myApprovalModel.getEmployeeID());
        approvalSModel.setsStoreid(myApprovalModel.getStoreID());
        approvalSModel.setsApplicationtitle(myApprovalModel.getApplicationTitle());
        approvalSModel.setsApprovalemployeeinfos(sApprovalemployeeinfos);

        Loading.run(this, new Runnable() {
            @Override
            public void run() {

                try {
                    Log.d("SJY", "抄送接口");
                    String message = UserHelper.CopyToMyApproval(CommonCopytoEplActivity.this, approvalSModel);

                    sendMessage(POSTDATA_SUCCESS, message);
                } catch (MyException e) {

                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });
    }


    /**
     * 获取选择的抄送人
     *
     * @param list
     * @return
     */
    private List<ContactsEmployeeModel> getSelectList(List<ContactsEmployeeModel> list) {
        List<ContactsEmployeeModel> checkBoxList = new ArrayList<>();
        //遍历
        for (int i = 0; i < list.size(); i++) {
            if (ContactsSelectAdapter.getIsSelectedMap().get(i) == true) {
                checkBoxList.add(list.get(i));
                Log.d("SJY", "选中的checkbox位置=" + i + "checkbox选中数据长度=" + checkBoxList.size());
            }
        }
        return checkBoxList;
    }

    /**
     * 对参数处理,将字符串变成：xxxxx,xxxxx,xxxxx
     *
     * @param list
     * @return
     */
    private String getList2String(List<ContactsEmployeeModel> list) {
        StringBuilder orgString = new StringBuilder();
        //遍历
        for (int i = 0; i < list.size(); i++) {
            orgString.append(list.get(i).getsEmployeeID() + ",");
        }
        //去除末尾逗号
        return orgString.substring(0, orgString.length() - 1);
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
