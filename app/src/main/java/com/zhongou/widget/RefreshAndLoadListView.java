package com.zhongou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhongou.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.zhongou.R.id.progressBar;

/**
 * 自定义listView控件，上拉下拉刷新功能(headView，footerView),区别于RefreshListView
 */
public class RefreshAndLoadListView extends ListView implements AbsListView.OnScrollListener {
    // 定义header的四种状态和当前状
    private int state;//当前的状态
    private final int NONE = 0;
    private final int PULL = 1;
    private final int RELEASE = 2;
    private final int REFRESHING = 3;
    // 区分PULL和RELEASE的距离的大小
    private static final int SPACE = 20;
    private OnScrollListener onScrollListener;

    public View headerView;//
    public View footerView;//

    private int headerHeight;//顶部布局文件的高度
    private int headerInitialHeight;//刷新过程中 固定的制定高度 freshing调用
    private int footerHeight;//顶部布局文件的高度

    private int firstVisibleItem;//当前第一个可见的item索引
    private int lastVisisibleItem;//最后一个可见的item索引

    int scrollState;//listView当前滚动状态

    private boolean isFresh = false;//标记是否在刷新，当前是在listView最顶端按下的
    private boolean isLoading = false;//标记是否在加载，当前是在listView最底部按下的
    private int startY;//按下时的y值

    IReflashListener iReflashListener;//刷新接口
    ILoadMoreListener iLoadMoreListener;//加载接口


    //添加下拉动画效果 箭头特效
    private RotateAnimation rotateAnimationUP;
    //添加松开动画效果 progressbar
    private RotateAnimation rotateAnimationDOWN;
    private LayoutInflater inflater;//管理 布局

    //footerView控件
    private TextView tv_loadMore;
    private ImageView loadMoreArrow;
    private ProgressBar loadMoreProgressBar;

    //headView控件
    private TextView tv_fresh;
    private TextView tv_lastTime;
    private ImageView freshArrow;
    private ProgressBar freshProgressBar;


    public RefreshAndLoadListView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshAndLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshAndLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 下拉刷新接口
     */
    public interface IReflashListener {
        public void onRefresh();
    }

    //下拉刷新监听
    public void setIRefreshListener(IReflashListener iReflashListener) {//IReflashListener
        this.iReflashListener = iReflashListener;
    }

    /**
     * 上拉加载接口
     */
    public interface ILoadMoreListener {
        public void onLoadMore();
    }

    //上拉监听
    public void setILoadMoreListener(ILoadMoreListener iLoadMoreListener) {
        this.iLoadMoreListener = iLoadMoreListener;
    }


    /**
     * 初始化界面，添加顶部布局到listView
     *
     * @param context
     */
    private void initView(Context context) {
        state = NONE;
        firstVisibleItem = 0;
        lastVisisibleItem = 0;

        inflater = LayoutInflater.from(context);
        initHeaderView();
        initFooterView();

        // 01 添加下拉动画效果 箭头特效
        rotateAnimationUP = new RotateAnimation(0,
                180,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimationUP.setDuration(500);
        rotateAnimationUP.setFillAfter(true);//动画结束后，停留在结束的位置

        // 02 添加松开动画效果 progressbar
        rotateAnimationDOWN = new RotateAnimation(180,
                0,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimationUP.setDuration(500);
        rotateAnimationUP.setFillAfter(true);//动画结束后，停留在结束的位置

        super.setOnScrollListener(this);//设置滚动监听
    }

    private void initFooterView() {
        //添加footerView
        footerView = inflater.inflate(R.layout.myrefreshlist_footerview, null);
        tv_loadMore = (TextView) footerView.findViewById(R.id.tv_loadmore);
        loadMoreArrow = (ImageView) footerView.findViewById(R.id.arrow);
        loadMoreProgressBar = (ProgressBar) footerView.findViewById(progressBar);

        measureView(footerView);

        footerHeight = footerView.getMeasuredHeight();
        hidefooterView(-footerHeight);

        this.addFooterView(footerView);//添加footerView
    }

    private void initHeaderView() {
        //添加headView
        headerView = inflater.inflate(R.layout.myrefreshlist_headerview, null);

        tv_fresh = (TextView) headerView.findViewById(R.id.tv_content);
        freshProgressBar = (ProgressBar) headerView.findViewById(progressBar);
        freshArrow = (ImageView) headerView.findViewById(R.id.arrow);
        tv_lastTime = (TextView) headerView.findViewById(R.id.tv_lastTime);
        tv_lastTime.setText("上次刷新时间：" + getCurrentTime());

        measureView(headerView);//
        headerHeight = headerView.getMeasuredHeight();//插入的headView控件高度
        hideHeaderView(-headerHeight);//

        headerInitialHeight = headerView.getPaddingTop();//freshing 固定高度
        this.addHeaderView(headerView);//添加headerView

    }

    /**
     * 通知父布局 占用 高
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidth = ViewGroup.getChildMeasureSpec(0, 0, params.width);//定义子布局宽度（左边距，内边距，宽度）
        int childHeight;
        int tempHeight = params.height;

        if (tempHeight > 0) {
            childHeight = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);//高度不空，填充布局
        } else {
            childHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);//高度是空，不填充
        }

        //这里的childWidth和childHeight并不是一个准备的值，
        // 而且指定一个规格或者标准让系统帮我们测量View的宽高，
        // 当我们指定childWidth和childHeight分别为0的时候，
        // 系统将不采用这个规格去测量，而是根据实际情况去测量
        child.measure(childWidth, childHeight);
    }

    /**
     * 隐藏 headerView
     */
    private void hideHeaderView(int topPadding) {

                headerView.setPadding(headerView.getPaddingLeft(),
                        topPadding,
                        headerView.getPaddingRight(),
                        headerView.getPaddingBottom());
//        headerView.setPadding(0,
//                topPadding,
//                0,
//                0);
        headerView.invalidate();
    }


    /**
     * 隐藏 footerView
     */
    private void hidefooterView(int topPadding) {

                footerView.setPadding(footerView.getPaddingLeft(),
                        topPadding,
                        footerView.getPaddingRight(),
                        footerView.getPaddingBottom());

//        footerView.setPadding(0,
        //                topPadding,
        //                0,
        //                0);

        footerView.invalidate();
    }


    /**
     * OnScrollListener两个重写方法
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        this.firstVisibleItem = firstVisibleItem;
        this.lastVisisibleItem = firstVisibleItem + visibleItemCount - 1;

        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;//获取滚动状态
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(view, scrollState);
        }

    }

    /**
     * 触摸手势监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isFresh = true;
                    startY = (int) ev.getY();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                onMoveEvent(ev);
                break;

            case MotionEvent.ACTION_UP:

                if (state == RELEASE) {
                    if (isFresh) {
                        state = REFRESHING;
                        onHeaderRefreshByState();
                        if (iReflashListener != null) {
                            Log.d("SJY", "onRefresh");
                            iReflashListener.onRefresh();//刷新接口
                        }
                    }
                    if (isLoading) {
                        state = REFRESHING;
                        onFooterLoadMoreByState();
                        if (iLoadMoreListener != null) {
                            Log.d("SJY", "loadMore");
                            iLoadMoreListener.onLoadMore();//加载接口
                        }
                    }

                } else if (state == PULL) {
                    if (isFresh) {
                        state = NONE;
                        onHeaderRefreshByState();
                        resetHeader();
                        isFresh = false;
                    }
                    if (isLoading) {
                        state = NONE;
                        onFooterLoadMoreByState();
                        resetFooter();
                        isLoading = false;
                    }
                } else if (state == NONE) {
                    isLoading = false;
                    isFresh = false;

                } else {

                }


                //                if (state == PULL) {
                //                    state = NONE;
                //                    isFresh = false;
                //
                //
                //                    onHeaderRefreshByState();
                //                } else if (state == RELEASE) {
                //
                //
                //                    state = REFRESHING;
                //                    if (iReflashListener != null) {
                //                        Log.d("SJY", "onfresh");
                //                        iReflashListener.onRefresh(); //调用刷新接口
                //                    }
                //                    onHeaderRefreshByState();
                //                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * MotionEvent.ACTION_MOVE 手势详细
     *
     * @param ev
     */

    private void onMoveEvent(MotionEvent ev) {

        if (resetAnim == null || !resetAnim.run) {
            if (state != REFRESHING) {
                Adapter adapter = getAdapter();
                if (adapter == null) {
                    headerEvent(ev);
                } else {
                    final int count = adapter.getCount();
                    if (count <= 0) {
                        headerEvent(ev);
                    } else {
                        int tempY = (int) ev.getRawY();
                        int moveY = tempY - startY;//移动距离
                        if (firstVisibleItem == 0 && lastVisisibleItem == count - 1) {
                            if (isFresh) {
                                headerEvent(ev);
                            } else if (isLoading) {
                                footerEvent(ev);
                            } else {
                                if (moveY > 0) {
                                    headerEvent(ev);
                                } else if (moveY < 0) {
                                    footerEvent(ev);
                                } else {

                                }
                            }
                        } else if (firstVisibleItem == 0 && moveY > 0) {
                            headerEvent(ev);
                        } else if (lastVisisibleItem == count - 1 && moveY < 0) {
                            footerEvent(ev);
                        } else {

                        }
                    }
                }

            }
        }

    }

    /**
     * 下拉手势
     *
     * @param ev
     */
    private void headerEvent(MotionEvent ev) {
        isFresh = true;
        int tempY = (int) ev.getRawY();
        int moveY = tempY - startY;
        moveY /= 2;
        startY = tempY;
        if (moveY > 0) {
            int newPadding = headerView.getPaddingTop() + moveY;
            newPadding = Math.min(newPadding, headerHeight / 2);
            headerView.setPadding(0, newPadding, 0, 0);
            if (state != REFRESHING) {
                if (newPadding > 0) {
                    state = RELEASE;
                    onHeaderRefreshByState();
                } else {
                    state = PULL;
                    onHeaderRefreshByState();
                }
            }
        } else {
            if (state == RELEASE || state == PULL) {
                int newPadding = (int) (headerView.getPaddingTop() + moveY);
                newPadding = Math.max(newPadding, -1 * headerHeight);
                headerView.setPadding(0, newPadding, 0, 0);
                if (newPadding <= -headerHeight) {
                    state = NONE;
                    onHeaderRefreshByState();
                    isFresh = false;
                } else if (newPadding <= 0) {
                    state = PULL;
                    onHeaderRefreshByState();
                } else {

                }
            }
        }

        //        if (!isFresh) {
        //            return;
        //        }
        //        int tempY = (int) ev.getY();//获取当前y
        //        int moveY = tempY - startY;//移动距离
        //        int topPadding = moveY - headerHeight;//不断监听距离变化,间距
        //        switch (state) {
        //            case NONE:
        //                if (moveY > 0) {
        //                    state = PULL;
        //                    onHeaderRefreshByState();
        //                }
        //                break;
        //
        //            case PULL:
        //
        //                //下拉头布局
        //                hideHeaderView(topPadding);
        //                if (moveY > headerHeight + SPACE
        //                        && scrollState == SCROLL_STATE_TOUCH_SCROLL) {//正在滚动状态
        //                    state = RELEASE;
        //                    onHeaderRefreshByState();
        //                }
        //                break;
        //
        //            case RELEASE:
        //
        //                //下拉头布局
        //                hideHeaderView(topPadding);
        //                if (moveY < headerHeight + SPACE && moveY > 0) {
        //                    state = PULL;
        //                    onHeaderRefreshByState();
        //                } else if (moveY <= 0) {
        //                    isFresh = false;
        //                    state = NONE;
        //                    onHeaderRefreshByState();
        //                }
        //                break;
        //
        //            case REFRESHING:
        //                break;
        //        }

    }

    /**
     * 上拉手势
     *
     * @param ev
     */
    private void footerEvent(MotionEvent ev) {
        isLoading = true;
        int tempY = (int) ev.getRawY();
        int moveY = tempY - startY;
        moveY /= 2;
        startY = tempY;
        if (moveY < 0) {
            int newPadding = (int) (footerView.getPaddingTop() - moveY);
            if (newPadding > 0) {
                newPadding = 0;
            }
            footerView.setPadding(0, newPadding, 0, 0);
            if (state != REFRESHING) {
                if (newPadding < 0) {
                    state = PULL;
                    onFooterLoadMoreByState();
                } else {
                    state = RELEASE;
                    onFooterLoadMoreByState();
                }
            }
        } else {
            int newPadding = footerView.getPaddingTop() - moveY;
            newPadding = Math.min(newPadding, footerHeight);
            footerView.setPadding(0, newPadding, 0, 0);
            if (newPadding <= -footerHeight) {
                state = NONE;
                onFooterLoadMoreByState();
                isLoading = false;
            } else if (newPadding < 0) {
                state = PULL;
                onFooterLoadMoreByState();
            }
        }
    }

    /**
     * 根据当前状态 改变界面显示 调整header
     */
    private void onHeaderRefreshByState() {
        switch (state) {
            case NONE://隐藏状态

                //下拉头布局
                hideHeaderView(-headerHeight);
                tv_fresh.setText("下拉可以刷新");//can fresh
                freshProgressBar.setVisibility(View.GONE);
                freshArrow.clearAnimation();//移除动画
                break;

            case PULL://下拉状态

                //onMove中已设置完头布局

                tv_fresh.setVisibility(View.VISIBLE);
                tv_lastTime.setVisibility(View.VISIBLE);
                freshArrow.setVisibility(View.VISIBLE);
                freshProgressBar.setVisibility(View.GONE);

                tv_fresh.setText("下拉可以刷新");//can fresh
                if (state == RELEASE) {
                    freshArrow.setAnimation(rotateAnimationDOWN); //图片方向向下
                } else {
                    freshArrow.clearAnimation();//移除动画
                }
                break;

            case RELEASE://松开刷新状态

                //onMove中已设置完头布局

                freshArrow.setVisibility(View.VISIBLE);
                tv_fresh.setVisibility(View.VISIBLE);
                tv_lastTime.setVisibility(View.VISIBLE);
                freshProgressBar.setVisibility(View.GONE);

                tv_fresh.setText("松开可以刷新");//release to fresh
                freshArrow.clearAnimation();
                freshArrow.setAnimation(rotateAnimationUP);//图片方向向上
                break;

            case REFRESHING://正在刷新状态

                //下拉头布局
                hideHeaderView(headerInitialHeight);

                freshArrow.setVisibility(View.GONE);
                freshArrow.clearAnimation();//移除动画
                freshProgressBar.setVisibility(View.VISIBLE);
                tv_fresh.setVisibility(View.VISIBLE);
                tv_fresh.setText("正在刷新...");//freshing
                tv_lastTime.setVisibility(View.INVISIBLE);

                break;
            default:
                break;
        }
    }

    /**
     * 根据当前状态 改变界面显示 调整footer
     */
    private void onFooterLoadMoreByState() {
        switch (state) {
            case NONE:
                loadMoreProgressBar.setVisibility(View.INVISIBLE);
                tv_loadMore.setText("下拉可以加载！");
                break;
            case PULL:
                loadMoreProgressBar.setVisibility(View.INVISIBLE);
                tv_loadMore.setText("下拉可以加载！");
                break;
            case RELEASE:
                loadMoreProgressBar.setVisibility(View.INVISIBLE);
                tv_loadMore.setText("松开可以刷新");
                break;
            case REFRESHING:
                loadMoreProgressBar.setVisibility(View.VISIBLE);
                tv_loadMore.setText("正在加载...");
                break;
            default:
                break;
        }
    }

    /**
     * 上拉 下拉 完成后的回调
     */

    public void loadAndFreshComplete() {
        if (isLoading) {
            onLoadMoreComplete();
        }
        if (isFresh) {
            onRefreshComplete();
        }
    }

    /**
     * 上拉加载 完成回调
     */
    private void onLoadMoreComplete() {
        isLoading = false;
        state = NONE;
        resetFooter();
        onFooterLoadMoreByState();
    }

    /**
     * 下拉刷新 完成回调
     *
     * @param lastTime
     */
    private void onRefreshComplete(String lastTime) {
        //可以将刷新后的时间显示到headerView上
        tv_lastTime.setText(lastTime);

        state = NONE;
        isFresh = false;
        resetHeader();
        onHeaderRefreshByState();
    }

    private void onRefreshComplete() {
        String currentTime = getCurrentTime();
        onRefreshComplete(currentTime);
    }

    private String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    private String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    //
    private void resetHeader() {
        resetAnim = new ResetAnimation(headerView, headerHeight, headerView.getPaddingTop());
        resetAnim.start();
    }

    private void resetFooter() {
        resetAnim = new ResetAnimation(footerView, footerHeight, footerView.getPaddingTop());
        resetAnim.start();
    }

    private ResetAnimation resetAnim;

    //?
    static class ResetAnimation extends Thread {

        static final int DURATION = 600;

        static final int INTERVAL = 5;

        View view;
        int orignalHeight;
        int paddingTop;

        boolean run = false;

        ResetAnimation(View view, int orignalHeight, int paddingTop) {
            this.view = view;
            this.orignalHeight = orignalHeight;
            this.paddingTop = paddingTop;
        }

        public void run() {
            run = true;
            int total = orignalHeight * 2 + paddingTop;
            int timeTotal = DURATION / INTERVAL;
            int piece = total / timeTotal;
            int time = 0;
            final View view = this.view;
            final int paddingTop = this.paddingTop;
            do {
                final int nextPaddingTop = paddingTop - time * piece;
                view.post(new Runnable() {
                    public void run() {
                        view.setPadding(0, nextPaddingTop, 0, 0);
                        view.postInvalidate();
                    }
                });
                try {
                    sleep(INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time++;
            } while (time < timeTotal);
            run = false;
        }
    }

}
