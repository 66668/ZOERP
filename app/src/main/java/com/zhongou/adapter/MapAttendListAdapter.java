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
import com.zhongou.model.MapAttendModel;

import java.util.Random;


/**
 * 地图考勤 适配
 *
 * @author
 */

public class MapAttendListAdapter extends BaseListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;

    public class WidgetHolder {
        TextView tv_local;
        TextView tv_time;
    }

    public MapAttendListAdapter(Context context) {
        super(context);
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);
    }

    @Override
    protected View inflateConvertView() {
        //一条记录的布局
        View view = inflater.inflate(R.layout.item_mapattendrecord, null);
        //该布局上的控件
        WidgetHolder holder = new WidgetHolder();
        holder.tv_local = (TextView) view.findViewById(R.id.tv_local);
        holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void initViewData(final int position, View convertView) {
        WidgetHolder holder = (WidgetHolder) convertView.getTag();//获取控件管理实例
        //获取一条信息
        //?java.lang.ClassCastException: java.util.ArrayList cannot be cast to com.yvision.model.VisitorBModel
        MapAttendModel model = (MapAttendModel) entityList.get(position);

        holder.tv_local.setText(model.getAddress());
        holder.tv_time.setText(model.getAttendCapTime());

    }

    //设置一条记录的随机颜色
    private int randomColor(){
        int [] colorArray = new int[]{R.color.pink,R.color.lightgreen,R.color.gray,R.color.yellow,R.color.common_color,R.color.aquamarine,R.color.brown};
        return colorArray[new Random().nextInt(6)];
    }

    public void destroy() {
        imgLoader.clearMemoryCache();
        imgLoader.destroy();
    }

}
