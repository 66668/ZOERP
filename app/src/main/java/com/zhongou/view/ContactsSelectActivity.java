package com.zhongou.view;

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

import com.zhongou.R;
import com.zhongou.adapter.ContactsSelectAdapter;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.CharacterParser;
import com.zhongou.common.MyException;
import com.zhongou.common.PinyinComparator;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.utils.ConfigUtil;
import com.zhongou.utils.PageUtil;
import com.zhongou.widget.SideBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 处理审批-所有申请时-添加联系人，获取审批通讯录界面
 * <p>
 * 功能：
 * 第一次通讯录从服务端获取，数据保存到sp中，以后都走sp获取数据，退出后数据清空
 * <p>
 * Created by sjy on 2017/2/7.
 */

public class ContactsSelectActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forSure")
    TextView tv_right;

    //listView
    @ViewInject(id = R.id.country_lvcountry)
    ListView contactsListView;

    //变量
    private ContactsSelectAdapter adapter;//审批人通讯录适配

    private SideBar sideBar;
    private CharacterParser characterParser;// 汉字转换成拼音的类
    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类


    public static List<ContactsEmployeeModel> selectlist;//checkBox选中数据集合
    private static List<ContactsEmployeeModel> listContactApprover;//审批人通讯录 集合

    //常量
    public static final int POST_SUCCESEE = 15;
    public static final int POST_FAILED = 16;
    public static final int CHASE_DATA = 17;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_contacts);
        tv_title.setText(getResources().getString(R.string.examination_requester));
        tv_right.setText(getResources().getString(R.string.examination_requester_sure));
        //布局详细操作（可添加多个方法）
        initViews();
        initListener();

        getContactApprover();
    }

    /**
     *
     */
    private void initViews() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
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

        //checkbox绑定列表监听
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

    public void getContactApprover() {
        //先判断sp中是否有值
        ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
        List<ContactsEmployeeModel> list = config.getContactApproverData();
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
                    List<ContactsEmployeeModel> list = UserHelper.getContactsSelectCo(MyApplication.getInstance());
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
            case POST_SUCCESEE://服务端获取数据
                List<ContactsEmployeeModel> listHandler = (List<ContactsEmployeeModel>) msg.obj;
                listContactApprover = filledData(listHandler);//为数据添加首字母
                //数据保存
                ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
                config.setContactApproverData(listContactApprover);
                // 根据a-z进行排序源数据
                Collections.sort(listContactApprover, pinyinComparator);

                adapter = new ContactsSelectAdapter(ContactsSelectActivity.this, listContactApprover);
                contactsListView.setAdapter(adapter);
                break;
            case POST_FAILED://
                PageUtil.DisplayToast((String) msg.obj);
                break;
            case CHASE_DATA://缓存的审批人通讯录

                List<ContactsEmployeeModel> listData = (List<ContactsEmployeeModel>) msg.obj;

                listContactApprover = filledData(listData);//为数据添加首字母
                // 根据a-z进行排序源数据
                Collections.sort(listContactApprover, pinyinComparator);

                adapter = new ContactsSelectAdapter(ContactsSelectActivity.this, listContactApprover);
                contactsListView.setAdapter(adapter);

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
     * 根据搜索框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactsEmployeeModel> filterDateList = new ArrayList<ContactsEmployeeModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = listContactApprover;
        } else {
            filterDateList.clear();
            for (ContactsEmployeeModel sortModel : listContactApprover) {
                String name = sortModel.getsEmployeeName();

                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


    /**
     * 确定
     *
     * @param view
     */
    public void forSure(View view) {
        selectlist = new ArrayList<ContactsEmployeeModel>();
        //遍历，查找为ture的该条记录，提取信息
        for (int i = 0; i < listContactApprover.size(); i++) {
            if (ContactsSelectAdapter.getIsSelectedMap().get(i) == true) {
                selectlist.add(listContactApprover.get(i));
                Log.d("SJY", "选中的checkbox位置=" + i + "checkbox选中数据长度=" + selectlist.size());
            }
        }

        Intent data = new Intent();//只是回传数据就不用写跳转对象

        if(selectlist.size()>0){//选中数据长度
            data.putExtra("data", (Serializable) selectlist);//数据放到data里面去 注意传空的异常处理
        }
        setResult(0, data);//返回data，2为result，data为intent对象
        finish();//页面销毁

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
