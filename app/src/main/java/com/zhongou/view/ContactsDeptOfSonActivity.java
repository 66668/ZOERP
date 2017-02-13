package com.zhongou.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.zhongou.model.ContactsDeptModel;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.widget.SearchEditText;
import com.zhongou.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 子公司-部门通讯录
 * Created by sjy on 2017/1/14.
 */

public class ContactsDeptOfSonActivity extends BaseActivity {
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

    private static List<ContactsDeptModel> listDeptData;//部门集合

    private static List<ContactsEmployeeModel> ListEmployeeData;//最终解析数据list

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
        setContentView(R.layout.act_contacts_2);
        tv_title.setText(getResources().getString(R.string.txt_contract));
        tv_right.setText("");

        Intent intent = getIntent();
        String sStoreID = intent.getStringExtra("sStoreID");
        Log.d("SJY", "sStoreID=" + sStoreID);

        initMyView();
        initListener();

        getData(sStoreID);
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
                //因headerView，需要修改position,易错地方，注意postion修改就可以
                int headerViewsCount = contactsListView.getHeaderViewsCount();
                int newPosition = position - headerViewsCount;

                //页面跳转
                if (newPosition < 0) {

                    int listDataPostion = newPosition + listDeptData.size();//listDeptData修正后的位置

                    Intent intent = new Intent(ContactsDeptOfSonActivity.this, ContactsEplOfDeptActivity.class);
                    intent.putExtra("sDeptID", listDeptData.get(listDataPostion).getsDeptID());//position
                    startActivity(intent);
                } else {
                    Intent intent2 = new Intent(ContactsDeptOfSonActivity.this, ContactsEplDetailActivity.class);
                    intent2.putExtra("ContactsEmployeModel", ListEmployeeData.get(newPosition));
                    startActivity(intent2);
                }
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
    private void getData(final String sStoreID) {
        final Message msg = new Message();

        Loading.noDialogRun(ContactsDeptOfSonActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    List<ContactsDeptModel> list = UserHelper.getContractsDeptOfSonCO(ContactsDeptOfSonActivity.this, sStoreID);
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
                case POST_SONCO_SUCCESS://服务端数据处理

                    listDeptData = (List<ContactsDeptModel>) msg.obj;

                    List<ContactsEmployeeModel> listEmpl = new ArrayList<ContactsEmployeeModel>();
                    for (int i = 0; i < listDeptData.size(); i++) {
                        listEmpl.addAll(listDeptData.get(i).getObj());
                    }

                    //为数据添加首字母
                    ListEmployeeData = filledData(listEmpl);
                    // 根据a-z进行排序源数据
                    Collections.sort(ListEmployeeData, pinyinComparator);
                    adapter = new ContactsSortAdapter(ContactsDeptOfSonActivity.this, ListEmployeeData);

                    //为listView添加动态headerView
                    addHeadView(listDeptData);

                    contactsListView.setAdapter(adapter);

                    break;
                case POST_FAILED:// 1001
                    PageUtil.DisplayToast((String) msg.obj);
                    break;
            }
        }
    };

    private void addHeadView(List<ContactsDeptModel> listData) {

        LayoutInflater inflator = (LayoutInflater) ContactsDeptOfSonActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int j = 0; j < listData.size(); j++) {
            //实例化控件
            LinearLayout headView = (LinearLayout) inflator.inflate(R.layout.item_contacts, null);
            //消除hederView中 公司
            if (j > 0) {
                TextView tv_letter = (TextView) headView.findViewById(R.id.tv_letter);
                tv_letter.setVisibility(View.GONE);
            }
            //展示界面
            TextView tv_Letter = (TextView) headView.findViewById(R.id.tv_letter);
            tv_Letter.setText(getResources().getString(R.string.examination_copyto_dept));

            TextView tv_SonOfCo = (TextView) headView.findViewById(R.id.tv_name);
            tv_SonOfCo.setText(listData.get(j).getsDeptName());

            contactsListView.addHeaderView(headView);
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
