package com.zhongou.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhongou.R;
import com.zhongou.base.BaseListAdapter;
import com.zhongou.common.ImageLoadingConfig;
import com.zhongou.model.MyApplicationModel;


/**
 * 公告适配
 *
 * @author
 */

public class NotificationListAdapter extends BaseListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;


    public class WidgetHolder {
        public TextView tvTitle;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvType;
        public TextView tvComment;
    }

    public NotificationListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);
    }

    @Override
    protected View inflateConvertView() {
        //一条记录的布局
        View view = inflater.inflate(R.layout.item_apl_apvl, null);
        //该布局上的控件
        WidgetHolder holder = new WidgetHolder();
        holder.tvName = (TextView) view.findViewById(R.id.tv_name);
        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
        holder.tvType = (TextView) view.findViewById(R.id.tv_type);
        holder.tvComment = (TextView) view.findViewById(R.id.tv_Comment);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例
        //获取一条信息
        //?java.lang.ClassCastException: java.util.ArrayList cannot be cast to com.yvision.model.VisitorBModel
        MyApplicationModel model = (MyApplicationModel) entityList.get(position);
        holder.tvName.setText(model.getEmployeeName());
        holder.tvTime.setText(model.getCreateTime());
        holder.tvComment.setText(model.getComment());
        holder.tvType.setText(model.getApplicationType());
        holder.tvTitle.setText(model.getApplicationTitle());
    }



    public void destroy() {
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
    }

}
