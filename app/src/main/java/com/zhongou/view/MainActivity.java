package com.zhongou.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.base.BaseFragment;
import com.zhongou.db.sqlite.SQLiteCopytoContactdb;
import com.zhongou.fragment.AppsFragment;
import com.zhongou.fragment.ContactsFragment;
import com.zhongou.fragment.MessageFragment;
import com.zhongou.helper.UserHelper;
import com.zhongou.utils.ConfigUtil;
import com.zhongou.utils.JPushUtil;
import com.zhongou.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class MainActivity extends BaseActivity {

    private NavigationView mNavigationView;
    private ViewPager viewPaper;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private RadioGroup mRadioGroup;
    private RadioButton mMessageRb;
    private RadioButton mAppsRb;
    private RadioButton mContractsRb;

    private MessageFragment messageFragment;//消息
    private AppsFragment appsFragment;//应用
    private ContactsFragment contactsFragment;//联系人

    private List<BaseFragment> listFragment;
    private int currentFragment;
    public static boolean isForeground = false;//推送 判断

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        initJpush();
        initMyView();
        initViewPaperAndFragment();
        initListener();
    }

    //极光配置
    private void initJpush(){
        JPushInterface.init(getApplicationContext());
        registerMessageReceiver();  // used for receive msg
        //推送设置别名
        setAlias(UserHelper.getCurrentUser().getWorlkId());
    }




    /**
     * 控件初始化
     */


    protected void initMyView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_home);

        //低于5.0版本的无法实现该功能
        if (Build.VERSION.SDK_INT >= 21) {
            // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
            ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
            mActionBarDrawerToggle.syncState();
            mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        }

        //底部控件实例化
        viewPaper = (ViewPager) findViewById(R.id.viewpager); //tab上方的区域
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);  //底部的3个tab
        mMessageRb = (RadioButton) findViewById(R.id.btn_message);
        mAppsRb = (RadioButton) findViewById(R.id.btn_app);
        mContractsRb = (RadioButton) findViewById(R.id.btn_contract);

        //侧滑菜单实例化
        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        mNavigationView = (NavigationView) findViewById(R.id.id_navigationview);
        mNavigationView.inflateHeaderView(R.layout.navigaitonview_header);
        View headerView = mNavigationView.getHeaderView(0);

        //添加图片，可替换成图片缓存方式
        CircleImageView circleImageView = (CircleImageView) headerView.findViewById(R.id.circleImg);
        circleImageView.setImageResource(R.mipmap.info_photo);
        //添加登陆人姓名
        TextView tvName = (TextView) headerView.findViewById(R.id.tv_login_name);

        if (!(UserHelper.getCurrentUser() == null) && !(UserHelper.getCurrentUser().getName() == null)) {
            tvName.setText(UserHelper.getCurrentUser().getName());
        }


        //添加菜单内容
        mNavigationView.inflateMenu(R.menu.menu_nav);
        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavgationViewMenuItemSelected(mNavigationView);

    }

    private void initViewPaperAndFragment() {

        messageFragment = MessageFragment.newInstance();
        appsFragment = AppsFragment.newInstance();
        contactsFragment = ContactsFragment.newInstance();
        listFragment = new ArrayList<>();
        listFragment.add(messageFragment);
        listFragment.add(appsFragment);
        listFragment.add(contactsFragment);
        viewPaper.setOffscreenPageLimit(3);
        viewPaper.setOnPageChangeListener(onPageChangeListener);

    }

    /**
     * 设置监听
     */
    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.btn_message:
                        currentFragment = 0;
                        break;

                    case R.id.btn_app:
                        currentFragment = 1;
                        break;

                    case R.id.btn_contract:
                        currentFragment = 2;
                        break;

                }


                viewPaper.setCurrentItem(currentFragment, false);

            }
        });

        viewPaper.setAdapter(new FragmentPagerAdapter(
                getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return listFragment.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return listFragment.get(arg0);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                super.destroyItem(container, position, object);
            }

        });
    }


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mRadioGroup.check(R.id.btn_message);
                    break;
                case 1:
                    mRadioGroup.check(R.id.btn_app);
                    break;
                case 2:
                    mRadioGroup.check(R.id.btn_contract);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav
     */
    private void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_info://个人资料
                        startActivity(PersonalInfoActivity.class);
                        break;
                    case R.id.nav_menu_psd://修改密码
                        startActivity(ChangePassWordActivity.class);
                        break;
                    case R.id.nav_menu_notify:

                        break;
                    //                    case R.id.nav_menu_feedback:
                    //                        startActivity(FeedbackActivity.class);//反馈意见
                    //                        break;
                    case R.id.nav_menu_quit://程序退出
                        //                        Intent intent = new Intent();
                        //                        intent.setAction(EXIT_APP_ACTION);
                        //                        sendBroadcast(intent);//发送退出的广播

                        //推送 关闭
                        JPushInterface.stopPush(getApplicationContext());
                        unregisterReceiver(mMessageReceiver);//注销广播

                        //数据清除
                        ConfigUtil config = new ConfigUtil(MainActivity.this);
                        config.setAutoLogin(false);//存储自动登录修改
                        config.setContactApproverData(null);//清空审批人通讯录数据，以防登录人更换后数据每更换导致出错
                        new SQLiteCopytoContactdb(MyApplication.getInstance()).clearDb();//清除抄送通讯录

                        //修改自动登录的判断
                        MyApplication.getInstance().setIsLogin(false);
                        UserHelper.setmCurrentUser(null);

                          /*
                         *该按钮只是关闭了activity,自动登录的参数，service等未处理，注意修改
                         */
                        MyApplication.getInstance().exit();

                        break;
                }

                menuItem.setChecked(true);
                // Menu item点击后选中，并关闭Drawerlayout
                //drawerlayoutHome.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!JPushUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                Log.d("JPush", "rid=" + JPushInterface.getRegistrationID(MainActivity.this) + "\n--showMsg" + showMsg);
            }
        }
    }
    /**
     * jpush 绑定别名
     */
    private void setAlias(String workid) {
        JPushInterface.setAliasAndTags(getApplicationContext(), workid, null, new TagAliasCallback() {

            @Override
            public void gotResult(int code, String s, Set<String> set) {
                String logs;
                switch (code) {
                    case 0:
                        Log.i("JPush", "Set tag and alias success极光推送别名设置成功");
                        break;
                    case 6002:
                        Log.i("JPush", "极光推送别名设置失败，Code = 6002");
                        break;
                    default:
                        Log.e("JPush", "极光推送设置失败，Code = " + code);
                        break;
                }
            }
        });
    }
}
