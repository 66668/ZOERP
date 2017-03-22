package com.zhongou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhongou.R;
import com.zhongou.base.BaseLoadMoreListAdapter;
import com.zhongou.common.ImageLoadingConfig;
import com.zhongou.model.ConferenceMSGModel;


/**
 * 会议列表 适配
 *
 * @author
 */

public class ConferenceLoadMoreListAdapter extends BaseLoadMoreListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;


    public class WidgetHolder {
        public TextView tvTime;
        public TextView tvType;
        public TextView tvContent;
        public TextView tv_state;
    }

    public ConferenceLoadMoreListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);
    }

    @Override
    protected View inflateConvertView() {
        //一条记录的布局
        View view = inflater.inflate(R.layout.item_app_notification_notice_common, null);
        //该布局上的控件
        WidgetHolder holder = new WidgetHolder();
        holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
        holder.tvType = (TextView) view.findViewById(R.id.tv_type);
        holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
        holder.tv_state = (TextView) view.findViewById(R.id.tv_state);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例
        //获取一条信息
        ConferenceMSGModel model = (ConferenceMSGModel) entityList.get(position);
        holder.tvTime.setText(model.getPublishTime());
        holder.tvType.setText(model.getTitle());
        holder.tvContent.setText(model.getAbstract());
        if(model.getIsRead().contains("1")){
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.green));
            holder.tv_state.setText("已读");
        }else{
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.red));
            holder.tv_state.setText("未读");
        }

    }


    public void destroy() {
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
    }

}
