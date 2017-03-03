package com.zhongou.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseFragment;
import com.zhongou.common.MyException;
import com.zhongou.db.sqlite.SQLiteScheduledb;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.model.ScheduleModel;
import com.zhongou.view.NoticeListActivity;
import com.zhongou.view.NotificationListActivity;
import com.zhongou.view.ScheduleMainActivity;
import com.zhongou.view.examination.ZOApprovelListActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * BottomBar 消息
 * Created by JackSong on 2016/10/9.
 */

public class MessageFragment extends BaseFragment {
    private static final String TAG = "MessageFragment";

    //变量
    //    private ScheduleDAO dao = null;
    SQLiteScheduledb dao = null;


    //控件
    private TextView msg_content;
    private TextView notice_content;
    private TextView undo_content;
    private TextView schedule_content;//日程安排提示

    private TextView msg_time;
    private TextView notice_time;
    private TextView undo_time;
    private TextView schedule_time;

    private RelativeLayout layout_notification;
    private RelativeLayout layout_notice;
    private RelativeLayout layout_undo;
    private RelativeLayout layout_schedule;

    private static final int GET_SCHEDULE_DATA = -38;
    private static final int GET_NEW_DATA = -37;//
    private static final int GET_REFRESH_DATA = -36;//
    private static final int GET_UNDO_DATA = -35;//
    private static final int NONE_NUDO_DATA = -34;//

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

    //重新进入页面后，刷新数据
    @Override
    public void onResume() {
        super.onResume();
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
    private void initListener() {
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

        //未审批
        layout_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZOApprovelListActivity.class);
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
        //日程表
        Loading.noDialogRun(getActivity(), new Runnable() {
            @Override
            public void run() {

                //        dao = new ScheduleDAO(this);
                dao = new SQLiteScheduledb(getActivity(), UserHelper.getCurrentUser().getEmployeeID() + ".db");


                ArrayList<ScheduleModel> listSchedule = dao.getAllSchedule();
                int scheduleSize = -1;
                if (listSchedule != null) {
                    scheduleSize = listSchedule.size();
                } else {
                    scheduleSize = 0;
                }
                handler.sendMessage(handler.obtainMessage(GET_SCHEDULE_DATA, scheduleSize));

            }
        });

        Loading.noDialogRun(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    List<MyApprovalModel> visitorModelList = UserHelper.getApprovalSearchResults(
                            getActivity(),
                            "",//iMaxTime
                            "");

                    if (visitorModelList == null || visitorModelList.size() <= 0) {
                        handler.sendMessage(handler.obtainMessage(NONE_NUDO_DATA, "没有未审批申请"));
                    }
                    handler.sendMessage(handler.obtainMessage(GET_UNDO_DATA, visitorModelList));
                } catch (MyException e) {
                    handler.sendMessage(handler.obtainMessage(NONE_NUDO_DATA, "没有未审批申请"));
                }
            }
        });
    }

    /**
     * handler sendMessage的处理
     */
    @SuppressLint("HandlerLeak") // 确保类内部的handler不含有对外部类的隐式引用
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 调用下边的方法处理信息
            switch (msg.what) {
                case GET_SCHEDULE_DATA:
                    //您有x条日程
                    int scheduleSize = (int) msg.obj;
                    if (scheduleSize > 0 && scheduleSize <= 10) {
                        schedule_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        schedule_content.setText("您有 " + scheduleSize + " 条日程要处理");
                    } else if (scheduleSize > 10) {
                        schedule_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        schedule_content.setText("您有 10+ 条日程要处理");
                    } else {
                        schedule_content.setText("没有日程安排");
                        schedule_content.setTextColor(getActivity().getResources().getColor(R.color.textHintColor));
                    }
                    break;

                case GET_UNDO_DATA:
                    List<MyApprovalModel> visitorModelList = (List<MyApprovalModel>) msg.obj;
                    int size = splitDate(visitorModelList);
                    if (size > 0 && size <= 10) {
                        undo_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        undo_content.setText("您有 " + size + " 条未审批申请");
                    } else if (size > 10) {
                        undo_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        undo_content.setText("您有 10+ 条未审批申请");
                    }

                    break;
                case NONE_NUDO_DATA:
                    undo_content.setText((String) msg.obj);
                    undo_content.setTextColor(getActivity().getResources().getColor(R.color.textHintColor));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //获取ApprovalStatus = 0的list
    private int splitDate(List<MyApprovalModel> list) {
        List<MyApprovalModel> undoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getApprovalStatus().contains("0")) {
                undoList.add(list.get(i));
            }
        }
        int size = undoList.size();
        return size;

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
