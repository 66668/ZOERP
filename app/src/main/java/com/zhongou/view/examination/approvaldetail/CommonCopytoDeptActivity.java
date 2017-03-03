package com.zhongou.view.examination.approvaldetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.ContactsCopyToDeptAdapter;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.db.sqlite.SQLiteCoContactdb;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ContactsDeptModel;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.model.ContactsSonCOModel;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.utils.PageUtil;

import java.util.List;

/**
 * 抄送 子公司-部门 通讯录
 * Created by sjy on 2017/1/17.
 */

public class CommonCopytoDeptActivity extends BaseActivity {

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
    private MyApprovalModel myApprovalModel;//跳转对象
    private String sStoreName;

    private static List<ContactsDeptModel> listDeptData;//部门集合

    private ContactsCopyToDeptAdapter adapter;//通讯录排序适配
    private SQLiteCoContactdb myDb; //sql数据库雷

    //常量
    public static final int POST_SUCCESEE = 15;
    public static final int POST_FAILED = 16;
    public static final int CHASE_DATA = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addACT(this);//多界面管理

        setContentView(R.layout.act_apps_examination_myapproval_common_contacts2);
        tv_title.setText(getResources().getString(R.string.examination_copyto));

        //获取调转对象

        Bundle bundle = this.getIntent().getExtras();

        String sStoreID = (String) bundle.get("sStoreID");
        sStoreName = (String) bundle.get("sStoreID");
        myApprovalModel = (MyApprovalModel) bundle.getSerializable("myApprovalModel");

        Log.d("SJY", "--" + myApprovalModel.getApprovalID() + "--");
        Log.d("SJY", "sStoreID=" + sStoreID);

        initViews();
        initListener();

        getData(sStoreID);


    }

    /**
     *
     */
    private void initViews() {
        myDb = new SQLiteCoContactdb(this);
    }

    /**
     * 控件监听
     */
    private void initListener() {

        //
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //因headerView，需要修改position,易错地方，注意postion修改就可以
                int headerViewsCount = contactsListView.getHeaderViewsCount();
                int newPosition = position - headerViewsCount;


                Intent intent = new Intent(CommonCopytoDeptActivity.this, CommonCopytoEplActivity.class);
                intent.putExtra("sDeptID", listDeptData.get(newPosition).getsDeptID());//position
                intent.putExtra("myApprovalModel", myApprovalModel);

                startActivity(intent);
            }
        });

    }

    public void getData(String StoreID) {
        //先判断sql中是否有值
        List<ContactsEmployeeModel> list = myDb.getEmpContactList(SQLiteCoContactdb.EMPLOYEEFLAG);
        Log.d("SJY", "list=" + list.size());
        if (list.size() > 0 && list != null) {
            Log.d("SJY", "走sp缓存");
            sendMessage(CHASE_DATA, list);

        } else if (list == null || list.size() <= 0) {
            Log.d("SJY", "走服务端数据");
            //获取服务端数据
            getDataFromURL(StoreID);

        }
    }

    public void getDataFromURL(final String sStoreID) {
        //
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    //获取部门集合
                    List<ContactsDeptModel> list = UserHelper.getContractsDeptOfSonCO(CommonCopytoDeptActivity.this, sStoreID);

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

                listDeptData = (List<ContactsDeptModel>) msg.obj;

                //                savetoCoContactSQL(listDeptData);// 部门集合保存到sql

                //设置首字母 adapter使用
                setFirstLetter(listDeptData);
                adapter = new ContactsCopyToDeptAdapter(this, listDeptData);

                //数据展示
                contactsListView.setAdapter(adapter);
                break;

            case CHASE_DATA://缓存数据处理

                //                //由于有缓存数据，从这里获取子公司集合
                //                //                listDeptData = myDb.getSonCOList(SQLiteCoContactdb.SONCOFLAG);
                //
                //                List<ContactsEmployeeModel> listData = (List<ContactsEmployeeModel>) msg.obj;
                //                listContactApprover = filledData(listData);//为数据添加首字母
                //
                //                // 根据a-z进行排序源数据
                //                Collections.sort(listContactApprover, pinyinComparator);
                //
                //                //listView添加headView
                //                addListHeadView(listDeptData);
                //
                //                //界面展示
                //                adapter = new ContactsCopyToCoAdapter(CommonCopytoDeptActivity.this, listContactApprover);
                //                contactsListView.setAdapter(adapter);
                break;
            case POST_FAILED://
                PageUtil.DisplayToast((String) msg.obj);
                break;

            default:
                break;
        }
    }

    private void setFirstLetter(List<ContactsDeptModel> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setFirstLetter(sStoreName + "-部门");
        }

    }

    /**
     * 部门集合保存到sql
     */
    private void savetoCoContactSQL(List<ContactsSonCOModel> list) {
        Log.d("SJY", "savetoCoContactSQL部门 数据存储");
        //        myDb.addSonCoList(list, SQLiteCoContactdb.SONCOFLAG);
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
