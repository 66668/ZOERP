package com.zhongou.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.ContactsSortAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.CharacterParser;
import com.zhongou.common.MyException;
import com.zhongou.common.PinyinComparator;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.widget.SearchEditText;
import com.zhongou.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 部门所有员工通讯录
 * Created by sjy on 2017/1/14.
 */

public class ContactsEplOfDeptActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    private ListView contactsListView;
    private SideBar sideBar;
    private ContactsSortAdapter adapter;
    private SearchEditText searchEditText;

    private static List<ContactsEmployeeModel> ListEmployeeData;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    //常量
    public static final int POST_SONCO_SUCCESS = 15;
    public static final int POST_FAILED = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contacts_dept);

        tv_title.setText(getResources().getString(R.string.txt_contract));
        tv_right.setText("");

        Intent intent = getIntent();
        String sDeptID = intent.getStringExtra("sDeptID");

        initMyView();

        initListener();

        getData(sDeptID);
    }

    /**
     *
     */
    private void initMyView() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        contactsListView = (ListView) findViewById(R.id.country_lvcountry);
        searchEditText = (SearchEditText) findViewById(R.id.filter_edit);
    }

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

        //列表监听
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent2 = new Intent(ContactsEplOfDeptActivity.this, ContactsEplDetailActivity.class);
                intent2.putExtra("ContactsEmployeModel", ListEmployeeData.get(position));
                startActivity(intent2);

            }
        });

        //搜索监听
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //获取服务端 通讯录数据
    private void getData(final String sDeptID) {
        final Message msg = new Message();

        Loading.noDialogRun(ContactsEplOfDeptActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    List<ContactsEmployeeModel> list = UserHelper.getContractsEmployeeOfDept(ContactsEplOfDeptActivity.this, sDeptID);
                    msg.obj = list;
                    msg.what = POST_SONCO_SUCCESS;// 1001
                    handler.sendMessage(msg);
                } catch (MyException e) {
                    msg.obj = e.getMessage();
                    msg.what = POST_FAILED;// 1001
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POST_SONCO_SUCCESS:// 1001
                    //数据处理
                    ListEmployeeData = (List<ContactsEmployeeModel>) msg.obj;
                    //为数据添加首字母
                    filledData(ListEmployeeData);
                    // 根据a-z进行排序源数据
                    Collections.sort(ListEmployeeData, pinyinComparator);
                    adapter = new ContactsSortAdapter(ContactsEplOfDeptActivity.this, ListEmployeeData);
                    contactsListView.setAdapter(adapter);

                    break;
                case POST_FAILED:// 1001
                    PageUtil.DisplayToast((String) msg.obj);
                    break;
            }
        }
    };

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
            filterDateList = ListEmployeeData;
        } else {
            filterDateList.clear();
            for (ContactsEmployeeModel sortModel : ListEmployeeData) {
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
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }
}
