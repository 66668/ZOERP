package com.zhongou.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseFragment;
import com.zhongou.view.FinancialListAcitivity;
import com.zhongou.view.NoticeListActivity;
import com.zhongou.view.NotificationListActivity;
import com.zhongou.view.ScheduleMainActivity;



/**
 * BottomBar 消息
 * Created by JackSong on 2016/10/9.
 */

public class MessageFragment extends BaseFragment {
    private static final String TAG = "MessageFragment";

    private TextView msg_content;
    private TextView notice_content;
    private TextView undo_content;
    private TextView schedule_content;

    private TextView msg_time;
    private TextView notice_time;
    private TextView undo_time;
    private TextView schedule_time;

    private RelativeLayout layout_notification;
    private RelativeLayout layout_notice;
    private RelativeLayout layout_undo;
    private RelativeLayout layout_schedule;


    //单例模式
    public static MessageFragment newInstance() {
        MessageFragment messageFragment = new MessageFragment();
        return messageFragment;
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_message, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //布局详细操作（可添加多个方法）
        initViews(view);
        initListener();
        getData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean hasMultiFragment() {
        return false;
    }

    @Override
    protected String setFragmentName() {
        return null;
    }

    /**
     * 数据详细操作
     *
     * @param view
     */
    private void initViews(View view) {
        msg_content = (TextView) view.findViewById(R.id.tv_msgContains);
        notice_content = (TextView) view.findViewById(R.id.tv_noticeContains);
        undo_content = (TextView) view.findViewById(R.id.tv_undoContains);
        schedule_content = (TextView) view.findViewById(R.id.tv_ScheduleContains);

        msg_time = (TextView) view.findViewById(R.id.tv_msgTime);
        notice_time = (TextView) view.findViewById(R.id.tv_noticeTime);
        undo_time = (TextView) view.findViewById(R.id.tv_undoTime);
        schedule_time = (TextView) view.findViewById(R.id.tv_scheduleTime);

        layout_notification = (RelativeLayout) view.findViewById(R.id.layout_notification);
        layout_notice = (RelativeLayout) view.findViewById(R.id.layout_notice);
        layout_undo = (RelativeLayout) view.findViewById(R.id.layout_undo);
        layout_schedule = (RelativeLayout) view.findViewById(R.id.layout_schedule);
    }

    /**
     * 界面跳转
     */
    private void initListener(){
        //通知
        layout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationListActivity.class);
                startActivity(intent);
            }
        });

        //公告
        layout_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoticeListActivity.class);
                startActivity(intent);
            }
        });

        //
        layout_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FinancialListAcitivity.class);
                startActivity(intent);
            }
        });

        //日程
        layout_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleMainActivity.class);
                startActivity(intent);
            }
        });

    }
    /**
     * 访问服务端，获取数据
     */
    private void getData() {

    }

    //重写setMenuVisibility方法，不然会出现叠层的现象
    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }
}
