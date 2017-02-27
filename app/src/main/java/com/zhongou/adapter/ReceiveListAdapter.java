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
import com.zhongou.model.ReceiveListModel;

/**
 * 应用-采购适配
 */
public class ReceiveListAdapter extends BaseListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;
    private Context context;

    public class WidgetHolder {
        public TextView tv_Title;
        public TextView tvTime;
        public TextView tvState;
    }

    public ReceiveListAdapter(Context context) {
        super(context);
        this.context = context;
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);
    }

    @Override
    protected View inflateConvertView() {
        //一条记录的布局
        View view = inflater.inflate(R.layout.item_procurementlist, null);
        //该布局上的控件
        WidgetHolder holder = new WidgetHolder();
        holder.tv_Title = (TextView) view.findViewById(R.id.tv_procurementTitle);
        holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
        holder.tvState = (TextView) view.findViewById(R.id.tv_state);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例

        ReceiveListModel model = (ReceiveListModel) entityList.get(position);
        //获取一条信息

        holder.tvTime.setText(model.getCreateTime());
        holder.tv_Title.setText(model.getName());

        //审批状态
        if (model.getApprovalStatus().contains("0")) {
            holder.tvState.setText("未审批");
            holder.tvState.setTextColor(context.getResources().getColor(R.color.red));
        } else if (model.getApprovalStatus().contains("1")) {
            holder.tvState.setText("已审批");
            holder.tvState.setTextColor(context.getResources().getColor(R.color.green));
        } else if (model.getApprovalStatus().contains("2")) {
            holder.tvState.setText("审批中...");
            holder.tvState.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.tvState.setText("你猜猜！");
        }

    }


    public void destroy() {
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
    }

}
