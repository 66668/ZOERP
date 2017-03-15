package com.zhongou.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhongou.helper.UserHelper;
import com.zhongou.view.LoginActivity;
import com.zhongou.view.NoticeListActivity;
import com.zhongou.view.NotificationListActivity;
import com.zhongou.view.examination.ZOAplicationListActivity;
import com.zhongou.view.examination.ZOApprovelListActivity;
import com.zhongou.view.examination.ZOCopyListActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getExtras();
        String myContent = printBundle(bundle);

        //页面跳转
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            transferTo(context, myContent);
        }
    }


    // 获取自己需要的数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_ALERT)) {
                sb.append(bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void transferTo(Context context, String content) {

        if (TextUtils.isEmpty(UserHelper.getCurrentUser().getStoreID()) || UserHelper.getCurrentUser().getStoreID() == null) {
            //在程序退出状态下获取通知，一些值无法获取，需要重新登录
            Intent intent = new Intent();
            intent.setClass(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            return;
        }

        if (content.contains("新的申请需要审批")) {
            Intent intent = new Intent();
            intent.setClass(context, ZOApprovelListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (content.contains("收到一条抄送申请")) {
            Intent intent = new Intent();
            intent.setClass(context, ZOCopyListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (content.contains("新的通知")) {
            Intent intent = new Intent();
            intent.setClass(context, NotificationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (content.contains("新的公告")) {
            Intent intent = new Intent();
            intent.setClass(context, NoticeListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (content.contains("申请审批已完成")) {
            Intent intent = new Intent();
            intent.setClass(context, ZOAplicationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
