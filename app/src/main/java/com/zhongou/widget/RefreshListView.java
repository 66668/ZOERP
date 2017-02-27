package com.zhongou.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhongou.R;

import static com.zhongou.R.id.arrow;


/**
 * 自定义listView控件，下拉刷新功能(headView),以及上拉的加载功能（调用接口形式）
 * 2016-09-05
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    public View headerView;//顶部布局文件
    private int headerHeight;//顶部布局文件的高度
    private int firstVisibleItem;//当前第一个可见的item位置
    int scrollState;//listView当前滚动状态
    boolean isRemark;//标记，当前是在listView最顶端按下的
    int startY;//按下时的y值

    int state;//当前的状态
    IReflashListener iReflashListener;
    final int NONE = 0;
    final int PULL = 1;
    final int RELEASE = 2;
    final int REFLASHING = 3;

    public RefreshListView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化界面，添加顶部布局到listView
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        headerView = inflater.inflate(R.layout.refreshlist_headerview, null);
        measureView(headerView);
        headerHeight = headerView.getMeasuredHeight();
        topPadding(-headerHeight);
        this.addHeaderView(headerView);
        this.setOnScrollListener(this);//设置滚动监听

    }

    /**
     * 通知父布局 占用 高
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);//定义子布局宽度（左边距，内边距，宽度）
        int height;
        int tempHeight = params.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);//高度不空，填充布局
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);//高度是空，不填充
        }
        view.measure(width, height);
    }

    /**
     * 设置headerView上边距
     */
    private void topPadding(int topPadding) {
        headerView.setPadding(headerView.getPaddingLeft(),
                topPadding,
                headerView.getPaddingRight(),
                headerView.getPaddingBottom());
        headerView.invalidate();
    }

    /**
     * OnScrollListener两个重写方法
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;//获取滚动状态
    }

    /**
     * 触摸监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                onMoveEvent(ev);
                break;

            case MotionEvent.ACTION_UP:

                if (state == RELEASE) {
                    state = REFLASHING;
                    iReflashListener.onRefresh();
                    reflashViewByState();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断移动过程操作
     *
     * @param ev
     */

    private void onMoveEvent(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        int tempY = (int) ev.getY();//获取当前y
        int moveY = tempY - startY;//移动距离
        int topPadding = moveY - headerHeight;//不断监听距离变化
        switch (state) {
            case NONE:
                if (moveY > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;

            case PULL:
                topPadding(topPadding);

                if (moveY > headerHeight + 10 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {//正在滚动状态
                    state = RELEASE;
                    reflashViewByState();
                }
                break;

            case RELEASE:
                topPadding(topPadding);
                if (moveY < headerHeight + 30) {
                    state = PULL;
                    reflashViewByState();
                } else if (moveY <= 0) {
                    isRemark = false;
                    state = NONE;
                    reflashViewByState();
                }
                break;

            case REFLASHING:
                break;
        }
    }

    /**
     * 根据当前状态改变界面显示
     */
    private void reflashViewByState() {
        TextView tv_Content = (TextView) headerView.findViewById(R.id.tv_content);
        ImageView arrowImage = (ImageView) headerView.findViewById(arrow);
        ProgressBar progressBar = (ProgressBar) headerView.findViewById(R.id.progressBar);
        //添加下拉动画效果
        RotateAnimation rotateAnimation1 = new RotateAnimation(0,
                180,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation1.setDuration(500);
        rotateAnimation1.setFillAfter(true);

        //添加松开动画效果
        RotateAnimation rotateAnimation2 = new RotateAnimation(180,
                0,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation1.setDuration(500);
        rotateAnimation1.setFillAfter(true);

        switch (state) {
            case NONE:
                arrowImage.clearAnimation();//移除动画
                topPadding(-headerHeight);
                break;

            case PULL:
                arrowImage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tv_Content.setText("下拉可以刷新");
                //图片方向向下
                arrowImage.clearAnimation();//移除动画
                arrowImage.setAnimation(rotateAnimation2);
                break;

            case RELEASE:
                arrowImage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tv_Content.setText("松开可以刷新");
                //图片方向向上
                arrowImage.clearAnimation();
                arrowImage.setAnimation(rotateAnimation1);

                break;

            case REFLASHING:
                topPadding(50);//正在刷新过程有固定高度
                arrowImage.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tv_Content.setText("正在刷新。。。");
                arrowImage.clearAnimation();//移除动画

                break;
            default:
                break;
        }
    }

    /**
     * 刷新完成
     */
    @TargetApi(Build.VERSION_CODES.N)
    public void reflashComplete() {
        state = NONE;
        isRemark = false;
        reflashViewByState();
    }

    /**
     * 上拉加载接口
     */
    public interface IReflashListener {
        public void onRefresh();
    }

    public void setInterFace(IReflashListener iReflashListener) {//IReflashListener
        this.iReflashListener = iReflashListener;
    }
}
