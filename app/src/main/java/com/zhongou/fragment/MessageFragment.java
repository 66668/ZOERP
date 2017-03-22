package com.zhongou.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.zhongou.model.ConferenceMSGModel;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.model.NoticeListModel;
import com.zhongou.model.NotificationListModel;
import com.zhongou.model.ScheduleModel;
import com.zhongou.view.ConferenceListActivity;
import com.zhongou.view.NoticeListActivity;
import com.zhongou.view.NotificationListActivity;
import com.zhongou.view.ScheduleMainActivity;
import com.zhongou.view.examination.ZOAplicationListActivity;
import com.zhongou.view.examination.ZOApprovelListActivity;
import com.zhongou.view.examination.ZOCopyListActivity;
import com.zhongou.widget.CircleTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * BottomBar 消息
 * Created by JackSong on 2016/10/9.
 */

public class MessageFragment extends BaseFragment {
    private static final String TAG = "MessageFragment";

    //变量
    SQLiteScheduledb dao = null;


    //内容
    private TextView msg_content;
    private TextView notice_content;
    private TextView undo_content;
    private TextView schedule_content;
    private TextView confernce_content;
    private TextView copy_content;
    private TextView done_content;

    //时间
    private TextView msg_time;
    private TextView notice_time;
    private TextView undo_time;
    private TextView schedule_time;
    private TextView confernce_time;
    private TextView copy_time;
    private TextView done_time;

    //个数
    private CircleTextView msg_number;
    private CircleTextView notice_number;
    private CircleTextView undo_number;
    private CircleTextView schedule_number;
    private CircleTextView confernce_number;
    private CircleTextView copy_number;
    private CircleTextView done_number;

    //布局
    private RelativeLayout layout_notification;
    private RelativeLayout layout_notice;
    private RelativeLayout layout_undo;
    private RelativeLayout layout_schedule;
    private RelativeLayout layout_confernce;
    private RelativeLayout layout_done;
    private RelativeLayout layout_copy;

    //常量

    //日程
    private static final int GET_SCHEDULE_DATA = -39;
    private static final int NONE_SCHEDULE_DATA = -38;

    //公告
    private static final int GET_NOTICE_DATA = -37;//
    private static final int NONE_NOTICE_DATA = -36;//

    //未办事项
    private static final int GET_UNDO_DATA = -35;//
    private static final int NONE_UNDO_DATA = -34;//

    //通知
    private static final int GET_NOTIFICATION_DATA = -33;//
    private static final int NONE_NOTIFICATION_DATA = -32;//

    //会议
    private static final int GET_CONFERENCE_DATA = -31;//
    private static final int NONE_CONFERENCE_DATA = -30;//

    //已受理
    private static final int GET_DONE_DATA = -29;//
    private static final int NONE_DONE_DATA = -28;//

    //抄送给我
    private static final int GET_COPY_DATA = -27;//
    private static final int NONE_COPY_DATA = -26;//


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
        //消息
        msg_content = (TextView) view.findViewById(R.id.tv_msgContains);
        notice_content = (TextView) view.findViewById(R.id.tv_noticeContains);
        undo_content = (TextView) view.findViewById(R.id.tv_undoContains);
        schedule_content = (TextView) view.findViewById(R.id.tv_ScheduleContains);
        confernce_content = (TextView) view.findViewById(R.id.tv_confernceContains);
        copy_content = (TextView) view.findViewById(R.id.tv_copyContains);
        done_content = (TextView) view.findViewById(R.id.tv_doneContains);

        //时间
        msg_time = (TextView) view.findViewById(R.id.tv_msgTime);
        notice_time = (TextView) view.findViewById(R.id.tv_noticeTime);
        undo_time = (TextView) view.findViewById(R.id.tv_undoTime);
        schedule_time = (TextView) view.findViewById(R.id.tv_scheduleTime);
        confernce_time = (TextView) view.findViewById(R.id.tv_confernceTime);
        copy_time = (TextView) view.findViewById(R.id.tv_copyTime);
        done_time = (TextView) view.findViewById(R.id.tv_doneTime);

        //数量提醒
        msg_number = (CircleTextView) view.findViewById(R.id.msg_number);
        notice_number = (CircleTextView) view.findViewById(R.id.notice_number);
        undo_number = (CircleTextView) view.findViewById(R.id.undo_number);
        schedule_number = (CircleTextView) view.findViewById(R.id.schedule_number);
        confernce_number = (CircleTextView) view.findViewById(R.id.confernce_number);
        copy_number = (CircleTextView) view.findViewById(R.id.copy_number);
        done_number = (CircleTextView) view.findViewById(R.id.done_number);

        layout_notification = (RelativeLayout) view.findViewById(R.id.layout_notification);
        layout_notice = (RelativeLayout) view.findViewById(R.id.layout_notice);
        layout_undo = (RelativeLayout) view.findViewById(R.id.layout_undo);
        layout_schedule = (RelativeLayout) view.findViewById(R.id.layout_schedule);
        layout_confernce = (RelativeLayout) view.findViewById(R.id.layout_confernce);
        layout_copy = (RelativeLayout) view.findViewById(R.id.layout_copy);
        layout_done = (RelativeLayout) view.findViewById(R.id.layout_done);
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

        //会议
        layout_confernce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConferenceListActivity.class);
                startActivity(intent);
            }
        });


        //已受理
        layout_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZOAplicationListActivity.class);
                startActivity(intent);
            }
        });

        //抄送给我
        layout_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZOCopyListActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 访问服务端，获取数据
     */
    private void getData() {

        Loading.noDialogRun(getActivity(), new Runnable() {
            @Override
            public void run() {
                //日程表
                dao = new SQLiteScheduledb(getActivity(), UserHelper.getCurrentUser().getEmployeeID() + ".db");
                ArrayList<ScheduleModel> listSchedule = dao.getAllSchedule();
                int scheduleSize = -1;
                if (listSchedule != null) {
                    scheduleSize = listSchedule.size();
                    if (scheduleSize <= 0) {
                        handler.sendMessage(handler.obtainMessage(NONE_SCHEDULE_DATA));
                    } else {
                        handler.sendMessage(handler.obtainMessage(GET_SCHEDULE_DATA, scheduleSize));
                    }
                } else {
                    handler.sendMessage(handler.obtainMessage(NONE_SCHEDULE_DATA));
                }


                //公告未读
                try {
                    List<NoticeListModel> visitorModelList = UserHelper.GetAppNoticeList(
                            getActivity(),
                            "",//iMaxTime
                            "");
                    if (visitorModelList == null) {
                        handler.sendMessage(handler.obtainMessage(NONE_NOTICE_DATA, "没有最新公告"));
                    } else {
                        if (visitorModelList.size() <= 0) {
                            handler.sendMessage(handler.obtainMessage(NONE_NOTICE_DATA, "没有最新公告"));
                        } else {
                            handler.sendMessage(handler.obtainMessage(GET_NOTICE_DATA, visitorModelList));
                        }
                    }
                } catch (MyException e) {
                    Log.d("SJY", "公告异常= " + e.getMessage());
                    handler.sendMessage(handler.obtainMessage(NONE_NOTICE_DATA, "没有最新公告"));
                }

                //通知未读
                try {
                    List<NotificationListModel> visitorModelList = UserHelper.GetAppNotificationList(
                            getActivity(),
                            "",//iMaxTime
                            "");
                    if (visitorModelList == null) {
                        handler.sendMessage(handler.obtainMessage(NONE_NOTIFICATION_DATA, "没有最新通知"));
                    } else {
                        if (visitorModelList.size() <= 0) {
                            handler.sendMessage(handler.obtainMessage(NONE_NOTIFICATION_DATA, "没有最新通知"));
                        } else {
                            handler.sendMessage(handler.obtainMessage(GET_NOTIFICATION_DATA, visitorModelList));
                        }
                    }
                } catch (MyException e) {
                    Log.d("SJY", "通知异常= " + e.getMessage());
                    handler.sendMessage(handler.obtainMessage(NONE_NOTIFICATION_DATA, "没有最新通知"));
                }

                //未办事项
                try {
                    List<MyApprovalModel> visitorModelList = UserHelper.getApprovalSearchResults(
                            getActivity(),
                            "",//iMaxTime
                            "");

                    if (visitorModelList == null) {
                        handler.sendMessage(handler.obtainMessage(NONE_UNDO_DATA, "没有未审批申请"));
                    } else {
                        if (visitorModelList.size() <= 0) {
                            handler.sendMessage(handler.obtainMessage(NONE_UNDO_DATA, "没有未审批申请"));
                        } else {
                            handler.sendMessage(handler.obtainMessage(GET_UNDO_DATA, visitorModelList));
                        }
                    }
                } catch (MyException e) {
                    handler.sendMessage(handler.obtainMessage(NONE_UNDO_DATA, "没有未审批申请"));
                }


                //会议
                try {
                    List<ConferenceMSGModel> conferenceModelList = UserHelper.GetAppConferenceList(
                            getActivity(),
                            "",//iMaxTime
                            "");

                    if (conferenceModelList == null) {
                        handler.sendMessage(handler.obtainMessage(NONE_CONFERENCE_DATA, "没有最新会议"));
                    } else {
                        if (conferenceModelList.size() <= 0) {
                            handler.sendMessage(handler.obtainMessage(NONE_CONFERENCE_DATA, "没有最新会议"));
                        } else {
                            handler.sendMessage(handler.obtainMessage(GET_CONFERENCE_DATA, conferenceModelList));
                        }
                    }
                } catch (MyException e) {
                    handler.sendMessage(handler.obtainMessage(NONE_CONFERENCE_DATA, "没有最新会议"));
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
                case GET_NOTICE_DATA://公告
                    List<NoticeListModel> noticeList = (List<NoticeListModel>) msg.obj;
                    int noticeSize = splitNoticeDate(noticeList);
                    if (noticeSize == 0) {
                        handler.sendMessage(handler.obtainMessage(NONE_NOTICE_DATA, "没有最新公告"));

                    } else if (noticeSize > 0 && noticeSize <= 10) {

                        //内容
                        notice_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        notice_content.setText("您有 " + noticeSize + " 条公告未阅读");

                        //时间
                        notice_time.setVisibility(View.VISIBLE);
                        notice_time.setText(noticeList.get(0).getPublishTime());
                        //个数
                        notice_number.setVisibility(View.VISIBLE);
                        notice_number.setText("" + noticeSize);

                    } else if (noticeSize > 10) {
                        notice_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        notice_content.setText("您有 10+  条公告未阅读");
                        //时间
                        notice_time.setVisibility(View.VISIBLE);
                        notice_time.setText(noticeList.get(0).getPublishTime());

                        //个数
                        notice_time.setVisibility(View.VISIBLE);
                        notice_number.setText("10+");
                    }

                    break;
                case GET_NOTIFICATION_DATA://通知
                    List<NotificationListModel> notificationList = (List<NotificationListModel>) msg.obj;
                    int notificationSize = splitNotificationDate(notificationList);
                    if (notificationSize == 0) {
                        handler.sendMessage(handler.obtainMessage(NONE_NOTIFICATION_DATA, "没有最新通知"));

                    } else if (notificationSize > 0 && notificationSize <= 10) {

                        //内容
                        msg_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        msg_content.setText("您有 " + notificationSize + " 条通知未阅读");

                        //时间
                        msg_time.setVisibility(View.VISIBLE);
                        msg_time.setText(notificationList.get(0).getPublishTime());

                        //个数
                        msg_number.setVisibility(View.VISIBLE);
                        msg_number.setText("" + notificationSize);

                    } else if (notificationSize > 10) {
                        msg_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        msg_content.setText("您有 10+  条通知未阅读");
                        //时间
                        msg_time.setVisibility(View.VISIBLE);
                        msg_time.setText(notificationList.get(0).getPublishTime());

                        //个数
                        msg_number.setVisibility(View.VISIBLE);
                        msg_number.setText("10+");
                    }

                    break;

                case GET_CONFERENCE_DATA://会议
                    List<ConferenceMSGModel> conferenceModelList = (List<ConferenceMSGModel>) msg.obj;

                    int conferenceSize = splitConferenceDate(conferenceModelList);

                    if (conferenceSize == 0) {
                        handler.sendMessage(handler.obtainMessage(NONE_CONFERENCE_DATA, "没有最新会议"));

                    } else if (conferenceSize > 0 && conferenceSize <= 10) {

                        //内容
                        confernce_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        confernce_content.setText("您有 " + conferenceSize + " 条未读会议");

                        //时间
                        confernce_time.setVisibility(View.VISIBLE);
                        confernce_time.setText("");

                        //个数

                        confernce_number.setVisibility(View.VISIBLE);
                        confernce_number.setText("" + conferenceSize);

                    } else if (conferenceSize > 10) {
                        confernce_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        confernce_content.setText("您有 10+ 条未读会议");
                        //时间
                        confernce_time.setVisibility(View.VISIBLE);
                        confernce_time.setText("");

                        //个数
                        confernce_number.setVisibility(View.VISIBLE);
                        confernce_number.setText("10+");
                    }

                    break;

                case GET_UNDO_DATA://未办事项
                    List<MyApprovalModel> visitorModelList = (List<MyApprovalModel>) msg.obj;
                    int size = splitApprovaDate(visitorModelList);
                    if (size == 0) {
                        handler.sendMessage(handler.obtainMessage(NONE_UNDO_DATA, "没有未办事项"));

                    } else if (size > 0 && size <= 10) {

                        //内容
                        undo_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        undo_content.setText("您有 " + size + " 条未审批申请");

                        //时间
                        undo_time.setVisibility(View.VISIBLE);
                        undo_time.setText("");

                        //个数
                        undo_number.setVisibility(View.VISIBLE);
                        undo_number.setText("" + size);

                    } else if (size > 10) {
                        undo_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        undo_content.setText("您有 10+ 条未审批申请");
                        //时间
                        undo_time.setVisibility(View.VISIBLE);
                        undo_time.setText("");

                        //个数
                        undo_number.setVisibility(View.VISIBLE);
                        undo_number.setText("10+");
                    }

                    break;

                case GET_SCHEDULE_DATA://日程

                    int scheduleSize = (int) msg.obj;
                    if (scheduleSize > 0 && scheduleSize <= 10) {
                        //内容
                        schedule_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        schedule_content.setText("您有 " + scheduleSize + " 条日程要处理");
                        schedule_time.setText("");
                        //个数
                        schedule_number.setText(scheduleSize + "");

                    } else if (scheduleSize > 10) {
                        //内容
                        schedule_content.setTextColor(getActivity().getResources().getColor(R.color.red));
                        schedule_content.setText("您有 10+ 条日程要处理");
                        schedule_time.setText("");
                        //个数
                        schedule_number.setText("10+");
                    }
                    break;

                case NONE_NOTICE_DATA:

                    //内容
                    notice_content.setText((String) msg.obj);
                    notice_content.setTextColor(getActivity().getResources().getColor(R.color.textHintColor));

                    //时间
                    notice_number.setVisibility(View.INVISIBLE);
                    //个数
                    notice_time.setVisibility(View.INVISIBLE);
                    break;
                case NONE_NOTIFICATION_DATA:

                    //内容
                    msg_content.setText((String) msg.obj);
                    msg_content.setTextColor(getActivity().getResources().getColor(R.color.textHintColor));
                    //时间
                    msg_number.setVisibility(View.INVISIBLE);
                    //个数
                    msg_time.setVisibility(View.INVISIBLE);
                    break;

                case NONE_CONFERENCE_DATA:

                    //内容
                    confernce_content.setText((String) msg.obj);
                    confernce_content.setTextColor(getActivity().getResources().getColor(R.color.textHintColor));
                    //时间
                    confernce_number.setVisibility(View.INVISIBLE);
                    //个数
                    confernce_time.setVisibility(View.INVISIBLE);
                    break;

                case NONE_UNDO_DATA:

                    //内容
                    undo_content.setText((String) msg.obj);
                    undo_content.setTextColor(getActivity().getResources().getColor(R.color.textHintColor));

                    //时间
                    undo_number.setVisibility(View.INVISIBLE);

                    //个数
                    undo_time.setText("");
                    undo_time.setVisibility(View.INVISIBLE);
                    break;


                case NONE_SCHEDULE_DATA:

                    //内容
                    schedule_content.setText("没有日程安排");
                    schedule_content.setTextColor(getActivity().getResources().getColor(R.color.textHintColor));

                    //时间
                    schedule_time.setText("");

                    //个数
                    schedule_number.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //获取ApprovalStatus != 0的list
    private int splitApprovaDate(List<MyApprovalModel> list) {
        List<MyApprovalModel> undoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getApprovalStatus().contains("0")) {
                undoList.add(list.get(i));
            }
        }
        int size = undoList.size();
        return size;

    }

    //获取公告! = 0的list
    private int splitNoticeDate(List<NoticeListModel> list) {
        List<NoticeListModel> NoticeList = new ArrayList<NoticeListModel>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsRead().contains("0")) {
                NoticeList.add(list.get(i));
            }
        }
        int size = NoticeList.size();
        return size;

    }

    //获取通知! = 0的list
    private int splitNotificationDate(List<NotificationListModel> list) {
        List<NotificationListModel> NoticeList = new ArrayList<NotificationListModel>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsRead().contains("0")) {
                NoticeList.add(list.get(i));
            }
        }
        int size = NoticeList.size();
        return size;

    }

    //获取会议! = 0的list
    private int splitConferenceDate(List<ConferenceMSGModel> list) {
        List<ConferenceMSGModel> NoticeList = new ArrayList<ConferenceMSGModel>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsRead().contains("0")) {
                NoticeList.add(list.get(i));
            }
        }
        int size = NoticeList.size();
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
