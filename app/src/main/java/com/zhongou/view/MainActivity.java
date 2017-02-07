package com.zhongou.view;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.base.BaseFragment;
import com.zhongou.fragment.AppsFragment;
import com.zhongou.fragment.ContactsFragment;
import com.zhongou.fragment.MessageFragment;
import com.zhongou.helper.UserHelper;
import com.zhongou.receiver.GetuiReceiver;
import com.zhongou.utils.ConfigUtil;
import com.zhongou.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private NavigationView mNavigationView;
    //    private FrameLayout mHomeContent;
    private ViewPager viewPaper;
    private RadioGroup mRadioGroup;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private RadioButton mMessageRb;
    private RadioButton mAppsRb;
    private RadioButton mContractsRb;

    private MessageFragment messageFragment;
    private AppsFragment appsFragment;
    private ContactsFragment contactsFragment;
    private List<BaseFragment> listFragment;
    private int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        //判断自动登录
        if (!MyApplication.getInstance().isLogin() && (MyApplication.getInstance().getClientID() == null)) {
            startActivity(LoginActivity.class);
            this.finish();
        }

        initMyView();
        initViewPaperAndFragment();
        initListener();
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

    //    private void initListener() {
    //        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    //            @Override
    //            public void onCheckedChanged(RadioGroup group, int checkedId) {
    //                int index = 0;
    //                switch (checkedId) {
    //                    case R.id.btn_message:
    //                        index = 0;
    //                        break;
    //                    case R.id.btn_app:
    //                        index = 1;
    //                        break;
    //                    case R.id.btn_contract:
    //                        index = 2;
    //                        break;
    //                    default:
    //                        break;
    //                }
    //                //通过fragments这个adapter还有index来替换帧布局中的内容
    //                Fragment fragment = (Fragment) fragmentStatePagerAdapter.instantiateItem(mHomeContent, index);
    //                //一开始将帧布局中 的内容设置为第一个
    //                fragmentStatePagerAdapter.setPrimaryItem(mHomeContent, 0, fragment);
    //                fragmentStatePagerAdapter.finishUpdate(mHomeContent);
    //            }
    //        });
    //    }

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
        tvName.setText(UserHelper.getCurrentUser().getName());

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

    //    //用adapter来管理几个Fragment界面的变化。注意，我这里用的Fragment都是v4包里面的
    //    FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
    //        @Override
    //        public Fragment getItem(int position) {
    //            Fragment fragment = null;
    //            switch (position) {
    //                case 0:
    //                    fragment = new MessageFragment();
    //                    break;
    //                case 1://tab第二页
    //                    fragment = new AppsFragment();
    //                    break;
    //                case 2://第三页
    //                    fragment = new ContractsFragment();
    //                    break;
    //                default:
    //                    new MessageFragment();//fragment = ?
    //                    break;
    //            }
    //            return fragment;
    //        }
    //
    //        @Override
    //        public int getCount() {
    //            return 0;
    //        }
    //
    //    };

    //    //第一次启动时，我们让第1页处于选中状态。
    //    @Override
    //    protected void onStart() {
    //        super.onStart();
    //        mRadioGroup.check(R.id.btn_message);
    //    }

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
                    case R.id.nav_menu_feedback:
                        startActivity(FeedbackActivity.class);
                        break;
                    case R.id.nav_menu_quit:
                        //                        Intent intent = new Intent();
                        //                        intent.setAction(EXIT_APP_ACTION);
                        //                        sendBroadcast(intent);//发送退出的广播

                        //个推关闭
                        PushManager.getInstance().turnOffPush(MainActivity.this);
                        PushManager.getInstance().stopService(MainActivity.this.getApplicationContext());
                        GetuiReceiver.payloadData.delete(0, GetuiReceiver.payloadData.length());

                        //存储自动登录修改
                        ConfigUtil configUtil = new ConfigUtil(MainActivity.this);
                        configUtil.setAutoLogin(false);

                        //修改自动登录的判断
                        MyApplication.getInstance().setIsLogin(false);
                        MyApplication.getInstance().setClientID(null);
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
}
