package com.zhongou.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseLoadMoreListAdapter;
import com.zhongou.common.ImageLoadingConfig;
import com.zhongou.model.VehicleReturnModel;


/**
 * 交车记录 适配
 *
 * @author
 */

public class VehicleReturnLoadMoreListAdapter extends BaseLoadMoreListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;


    public class WidgetHolder {
        public TextView tvTitle;
        public TextView tvTime;
        public TextView tvType;
        public TextView tv_status;
    }

    public VehicleReturnLoadMoreListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);
    }

    @Override
    protected View inflateConvertView() {
        //一条记录的布局
        View view = inflater.inflate(R.layout.item_vehicle_return, null);
        //该布局上的控件
        WidgetHolder holder = new WidgetHolder();
        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
        holder.tvType = (TextView) view.findViewById(R.id.tv_type);
        holder.tv_status = (TextView) view.findViewById(R.id.tv_status);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例
        //获取一条信息
        //
        VehicleReturnModel model = (VehicleReturnModel) entityList.get(position);
        holder.tvTime.setText(model.getCopyTime());
        holder.tvType.setText(model.getApplicationType());
        holder.tvTitle.setText(model.getApplicationTitle());
        if (model.getIsBack().equals("1")) {
            holder.tv_status.setTextColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.textHintColor));
            holder.tv_status.setText(MyApplication.getInstance().getResources().getString(R.string.vehicleRe_complete));
        } else if (model.getIsBack().equals("0")) {

            holder.tv_status.setTextColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.red));
            holder.tv_status.setText(MyApplication.getInstance().getResources().getString(R.string.vehicleRe_uncomplete));
        } else {
            holder.tv_status.setTextColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.red));
            holder.tv_status.setText("无法判断");
        }
    }

    public void destroy() {
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
    }

}
