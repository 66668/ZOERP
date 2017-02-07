package com.zhongou.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhongou.R;
import com.zhongou.base.BaseListAdapter;
import com.zhongou.common.ImageLoadingConfig;
import com.zhongou.application.MyApplication;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.widget.CircleTextView;

import java.util.Random;


/**
 * 我的审批 适配
 *
 * @author
 */

public class MyApprovalListAdapter extends BaseListAdapter {
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;


    public class WidgetHolder {
        public TextView tvTitle;
        public CircleTextView tvName;
        public TextView tvTime;
        public TextView tvType;//类型
        public TextView tvComment;//审批状态
    }

    public MyApprovalListAdapter(Context context, AdapterCallBack callBack) {
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
        holder.tvName = (CircleTextView) view.findViewById(R.id.tv_name);
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
        MyApprovalModel model = (MyApprovalModel) entityList.get(position);
        holder.tvName.setText(model.getEmployeeName());
        holder.tvName.setBackgroundColor(ContextCompat.getColor(MyApplication.getInstance(),randomColor()));
        holder.tvTime.setText(model.getCreateTime());
        holder.tvType.setText(model.getApplicationType());
        holder.tvTitle.setText(model.getApplicationTitle());

        if (model.getApprovalStatus().contains("1")) {
            holder.tvComment.setText(MyApplication.getInstance().getResources().getString(R.string.examination_yes));
            holder.tvComment.setTextColor(ContextCompat.getColor(MyApplication.getInstance(),R.color.common_color));
        } else if (model.getApprovalStatus().contains("0")){
            holder.tvComment.setText(MyApplication.getInstance().getResources().getString(R.string.examination_no));
            holder.tvComment.setTextColor(ContextCompat.getColor(MyApplication.getInstance(),R.color.red));
        }else if(model.getApprovalStatus().contains("2")){
            holder.tvComment.setText(MyApplication.getInstance().getResources().getString(R.string.examination_going));
            holder.tvComment.setTextColor(ContextCompat.getColor(MyApplication.getInstance(),R.color.common_color));
        }else{
            holder.tvComment.setText("无法判断");
        }


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
