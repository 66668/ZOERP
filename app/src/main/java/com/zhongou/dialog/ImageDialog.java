package com.zhongou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhongou.R;
import com.zhongou.common.ImageLoadingConfig;

/**
 * 弹窗查看大图（请假和报销使用）
 */
public class ImageDialog extends Dialog {
    private Context context;
    private int icon = 0;
    private Callback callback = null;// 本类中的接口
    private boolean cancelable = true;
    private String imgPath;
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;

    // 构造赋值01
    public ImageDialog(Context context, String imgpath) {
        super(context, R.style.LoadingDialog);// 加载自定义资源位置
        this.context = context;
        this.imgPath = imgpath;

        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);

        init();
    }


    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    //init
    void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_imgdetail, null);
        setContentView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_img);

        imgLoader.displayImage(imgPath, imageView, imgOptions);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }

    //back键修改
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && cancelable) {
            if (callback != null) {
                callback.update();//调用本类的接口方法
            }
            this.dismiss();
        }
        return true;
    }

    // 接口
    public interface Callback {
        // 接口方法
        public void update();
    }
}
