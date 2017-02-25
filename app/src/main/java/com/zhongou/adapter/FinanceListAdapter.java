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
import com.zhongou.model.applicationdetailmodel.FinancialAllModel;


/**
 * 财务记录 自定义上拉下拉listView 的适配
 *
 * @author
 */

public class FinanceListAdapter extends BaseListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;


    public class WidgetHolder {
        public TextView tv_finaceTitle;
        public TextView tvTime;
        public TextView tvType;
    }

    public FinanceListAdapter(Context context) {
        super(context);
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);
    }

    @Override
    protected View inflateConvertView() {
        //一条记录的布局
        View view = inflater.inflate(R.layout.item_finance, null);
        //该布局上的控件
        WidgetHolder holder = new WidgetHolder();
        holder.tv_finaceTitle = (TextView) view.findViewById(R.id.tv_finace_title);
        holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
        holder.tvType = (TextView) view.findViewById(R.id.tv_type);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例

        FinancialAllModel model = (FinancialAllModel) entityList.get(position);
        //获取一条信息

        holder.tvTime.setText(model.getCreateTime());
        holder.tvType.setText(model.getType());
        holder.tv_finaceTitle.setText(model.getCreateTime());


    }


    public void destroy() {
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
    }

}
