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
import com.zhongou.model.FinanceModel;


/**
 * 财务记录 适配
 *
 * @author
 */

public class FinanceLoadMoreListAdapter extends BaseLoadMoreListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;


    public class WidgetHolder {
        public TextView tv_storeName;
        public TextView tvTime;
        public TextView tvType;
    }

    public FinanceLoadMoreListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
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
        holder.tv_storeName = (TextView) view.findViewById(R.id.tv_storeName);
        holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
        holder.tvType = (TextView) view.findViewById(R.id.tv_type);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例

        FinanceModel model = (FinanceModel) entityList.get(position);
        //获取一条信息

        holder.tvTime.setText(model.getCopyTime());
        holder.tvType.setText(model.getApplicationType());
        holder.tv_storeName.setText(model.getApplicationTitle());


    }


    public void destroy() {
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
    }

}
