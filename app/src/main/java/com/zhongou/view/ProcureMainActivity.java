package com.zhongou.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.base.BaseFragment;
import com.zhongou.fragment.ProcurementListFragment;
import com.zhongou.fragment.ReceiveListFragment;
import com.zhongou.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 采购领用
 * Created by sjy on 2016/12/13.
 */

public class ProcureMainActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    private RadioGroup mRadioGroup;
    private RadioButton mProcurement;
    private RadioButton mReceive;

    private ReceiveListFragment receiveListFragment;//领用
    private ProcurementListFragment procurementListFragment;//采购

    private List<BaseFragment> listFragment;
    private int currentFragment;
    private ViewPager viewPaper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_procurement);
        tv_title.setText(getResources().getString(R.string.app_procueRecord));
        tv_right.setText("");

        initMyView();
        initListener();
    }

    private void initMyView() {
        //底部控件实例化
        viewPaper = (ViewPager) findViewById(R.id.procureListViewpager);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mProcurement = (RadioButton) findViewById(R.id.btn_Procurement);
        mReceive = (RadioButton) findViewById(R.id.btn_receive);

        procurementListFragment = ProcurementListFragment.newInstance();
        receiveListFragment = ReceiveListFragment.newInstance();
        listFragment = new ArrayList<>();
        listFragment.add(procurementListFragment);
        listFragment.add(receiveListFragment);
        viewPaper.setOffscreenPageLimit(2);
        viewPaper.setOnPageChangeListener(onPageChangeListener);
    }


    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.btn_Procurement:
                        currentFragment = 0;
                        break;

                    case R.id.btn_receive:
                        currentFragment = 1;
                        break;
                }

                //通过fragments这个adapter还有index来替换帧布局中的内容
                //                Fragment fragment = (Fragment) fragmentStatePagerAdapter.instantiateItem(mHomeContent, index);
                //                //一开始将帧布局中 的内容设置为第一个
                //                fragmentStatePagerAdapter.setPrimaryItem(mHomeContent, 0, fragment);
                //                fragmentStatePagerAdapter.finishUpdate(mHomeContent);

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
                    mRadioGroup.check(R.id.btn_Procurement);
                    break;
                case 1:
                    mRadioGroup.check(R.id.btn_receive);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }
}
