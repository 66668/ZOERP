package com.zhongou.view.examination.approvaldetail;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.ContactsCopyToCoAdapter;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.db.sqlcontact.SQLiteCoContactdb;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.model.ContactsSonCOModel;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.utils.PageUtil;

import java.util.List;


/**
 * 抄送 公司-子公司 通讯录
 * <p>
 * Created by sjy on 2017/1/17.
 */

public class CommonCopytoCoActivity extends BaseActivity {

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


    private static List<ContactsSonCOModel> listSonCoData;//子公司集合

    private ContactsCopyToCoAdapter adapter;//通讯录 适配
    private SQLiteCoContactdb myDb; //sql数据库雷

    //常量
    public static final int POST_SUCCESEE = 15;
    public static final int POST_FAILED = 16;
    public static final int CHASE_DATA = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addACT(this);//多界面管理

        setContentView(R.layout.act_apps_examination_myapproval_common_contacts1);
        tv_title.setText(getResources().getString(R.string.examination_copyto));

        //获取页面跳转对象
        Bundle bundle = this.getIntent().getExtras();
        myApprovalModel = (MyApprovalModel)bundle.getSerializable("MyApprovalModel");

        initViews();
        initListener();

        getData();
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

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //因headerView，需要修改position,易错地方，注意postion修改就可以
                int headerViewsCount = contactsListView.getHeaderViewsCount();
                int newPosition = position - headerViewsCount;

                Bundle bundle = new Bundle();
                bundle.putString("sStoreID",listSonCoData.get(newPosition).getsStoreID());
                bundle.putString("sStoreName",listSonCoData.get(newPosition).getsStoreName());
                bundle.putSerializable("myApprovalModel", myApprovalModel);//必要的参数

                startActivity(CommonCopytoDeptActivity.class,bundle);

            }
        });
    }

    public void getData() {
        //先判断sql中是否有值
        List<ContactsEmployeeModel> list = myDb.getEmpContactList(SQLiteCoContactdb.EMPLOYEEFLAG);
        Log.d("SJY", "list=" + list.size());
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
                    //获取子公司集合
                    List<ContactsSonCOModel> list = UserHelper.getCompanySonOfCO(MyApplication.getInstance());

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
            case POST_SUCCESEE://
                //数据处理
                listSonCoData = (List<ContactsSonCOModel>) msg.obj;

                //设置首字母 adapter使用
                setFirstLetter(listSonCoData);
                adapter = new ContactsCopyToCoAdapter(this, listSonCoData);
                //数据展示
                contactsListView.setAdapter(adapter);
                break;

            case CHASE_DATA://缓存

                break;

            case POST_FAILED://
                PageUtil.DisplayToast((String) msg.obj);
                break;

            default:
                break;
        }
    }

    private void setFirstLetter(List<ContactsSonCOModel> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setFirstLetter("子公司");
        }

    }

    /**
     * 子公司集合保存到sql
     */
    private void savetoCoContactSQL(List<ContactsSonCOModel> list) {
        Log.d("SJY", "savetoCoContactSQL数据存储");
        myDb.addSonCoList(list, SQLiteCoContactdb.SONCOFLAG);
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
