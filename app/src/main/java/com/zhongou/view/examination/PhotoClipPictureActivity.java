package com.zhongou.view.examination;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.ImageLoadingConfig;
import com.zhongou.widget.ClipViews;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

;

/**
 * 照片截取页面
 */
public class PhotoClipPictureActivity extends BaseActivity implements OnTouchListener {
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();//
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	// 图片部分
	private int userPhotoBgSampleWidth;// 图片的宽
	private int userPhotoBgSampleHeight;// 图片的高
	private String imgPath;// 用于存储图片路径
	private Bitmap bitmapModel;

	private ImageView orgImageView;// 原图显示view
	private ClipViews clipview;// 截取框/ClipView.java
	private LinearLayout userPhotoLoadingBg;// 刚进入此页面时，加载提示

	private Handler handler = new Handler();// 线程处理调用

	private int MOBILE_STATUS_BAR_HEIGHT = 38;// 状态栏高度
	private int TOP_BAR_HEIGHT;// 顶部导航栏高度

	// 外部包：universal-image-loader-1.9.2.jar
	private ImageLoader imgLoader;
	@SuppressWarnings("unused")
	private DisplayImageOptions imgOption;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_photo_clip);// 剪裁图片布局
		//外部包处理图片
		imgLoader = ImageLoader.getInstance();
		imgLoader.init(ImageLoaderConfiguration.createDefault(this));
		imgOption = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);

		DisplayMetrics metrics = getResources().getDisplayMetrics();// 初始化
		userPhotoBgSampleWidth = metrics.widthPixels;// 图片的宽
		userPhotoBgSampleHeight = metrics.heightPixels;// 图片的高
		// 设置导航栏的高度
		TOP_BAR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.common_topbar_height);
		// 设置状态栏高度
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			Field field = clazz.getField("status_bar_height");
			int height = Integer.parseInt(field.get(object).toString());
			MOBILE_STATUS_BAR_HEIGHT = getResources().getDimensionPixelSize(height); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 01图片剪裁并保存到本地
		initViewBtn();

		orgImageView = (ImageView) this.findViewById(R.id.src_pic);
		// 06设置图片裁剪监听
		orgImageView.setOnTouchListener(this);
		
		// 获取图片路径(相册调用)
		Intent intent = getIntent();
		if (intent != null) {
			imgPath = intent.getStringExtra("img_path");//CameraGalleryUtils.onActivityResultAction中的值
		}
		//若没有获得图片路径，指定目录寻找
		if (TextUtils.isEmpty(imgPath)) {// 空=true
			// YUEVISION/tempPics/uploadTemp/unhandled.jpg
			imgPath = MyApplication.getUnhandledUserPhotoPath(PhotoClipPictureActivity.this);
		}

		File fi = new File(imgPath);// 创建
		if (!fi.exists()) {
			Toast.makeText(getApplicationContext(), "请选择系统相册图片", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}     
		// 刚进入此页面时，加载提示
		userPhotoLoadingBg = (LinearLayout) findViewById(R.id.user_photo_loading_bg);
		// 05保存缓存到本地
		new saveToLocalThread().start();

	}

	/* 06 OnTouchListener的实现方法 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			// 設置初始點位置
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		view.setImageMatrix(matrix);
		return true; // indicate event was handled
	}

	/**
	 * Determine the space between the first two fingers
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float)Math.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	// 01oncreate方法调用：图片裁剪界面标题设置，图片剪裁并保存
	public void initViewBtn() {

		ImageView back = (ImageView) findViewById(R.id.back_btn);// back图片按钮
		ImageButton next = (ImageButton) findViewById(R.id.top_right_btn);// 对号按钮，裁剪完成点击
		next.setVisibility(View.VISIBLE);// 设置可见
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 02调用下边方法：图片剪裁并保存
				generateUploadUserPhotoToLocal();
				// 下一步
				Intent intent = new Intent();
				//返回之前的activity的onActivityResult中处理
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	// 02裁剪图片
	private void generateUploadUserPhotoToLocal() {

		clipview = (ClipViews) this.findViewById(R.id.clipview);
		clipview.setVisibility(View.INVISIBLE);// 截取框不可见
		// 04调用下边方法：获取Activity的截屏
		Bitmap screenShoot = takeScreenShot();
		clipview.setVisibility(View.VISIBLE);
		int width = clipview.getWidth();
		int height = clipview.getHeight();

		// yStart 相对于整个屏幕高度
		int yStart = (height - width) / 2 + (MOBILE_STATUS_BAR_HEIGHT + TOP_BAR_HEIGHT);// 状态栏高度，导航栏高度

		Bitmap finalBitmap = Bitmap.createBitmap(screenShoot, 1, yStart + 1, width - 1, width - 1);

		int compressDegree = 85;// 压缩比例
		// 03调用下边方法：图片保存
		// 图片路径：YUEVISION/tempPics/uploadTemp/handled.jpg
		saveBitmapToLocalFile(MyApplication.getHandledUserPhotoPath(PhotoClipPictureActivity.this), finalBitmap,
				compressDegree, false);

		if (finalBitmap != null && !finalBitmap.isRecycled()) {
			finalBitmap.recycle();
			finalBitmap = null;
		}

	}

	// 03将bitmap保存路径
	public void saveBitmapToLocalFile(String filePath, Bitmap sourceBitmap, int compressDegree,
			boolean isSourceBitmapRecycle) {

		FileOutputStream stream = null;

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}

		try {
			stream = new FileOutputStream(file);
			if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
				sourceBitmap.compress(CompressFormat.JPEG, compressDegree, stream);// 大小压缩
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}

				if (isSourceBitmapRecycle && sourceBitmap != null && !sourceBitmap.isRecycled()) {
					sourceBitmap.recycle();
					sourceBitmap = null;
				}
			} catch (IOException e2) {
			}
		}

	}

	// 04获取Activity的截屏
	private Bitmap takeScreenShot() {
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		return view.getDrawingCache();
	}

	// 05 oncreate方法调用：保存缓存到本地
	private class saveToLocalThread extends Thread {

		@Override
		public void run() {

			ImageSize targetSize = new ImageSize(userPhotoBgSampleWidth, userPhotoBgSampleHeight); // 最后图片适应这个大小

			bitmapModel = imgLoader.loadImageSync("file://" + imgPath, targetSize,
					ImageLoadingConfig.generateDisplayImageOptionsNoCatchDisc());

			handler.post(new Runnable() {
				@Override
				public void run() {

					if (bitmapModel != null && !bitmapModel.isRecycled()) {
						orgImageView.setImageBitmap(bitmapModel);// 将bitmap展示到imageView上

						// Get the image's rect
						RectF drawableRect = new RectF(0, 0, bitmapModel.getWidth(), bitmapModel.getHeight());

						// Get the image view's rect
						RectF viewRect = new RectF(0, 0, orgImageView.getWidth(), orgImageView.getHeight());

						// draw the image in the view
						matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);

						if (bitmapModel.getWidth() > bitmapModel.getHeight()) {
							// 处理高
							float nowImgHeight = ((float) orgImageView.getWidth() / bitmapModel.getWidth())
									* bitmapModel.getHeight();
							// 比例
							float sx = (float) (orgImageView.getWidth() / nowImgHeight);
							matrix.postScale(sx, sx, orgImageView.getWidth() / 2, orgImageView.getHeight() / 2);
						}

						orgImageView.setImageMatrix(matrix);
						orgImageView.invalidate();// 刷新
						userPhotoLoadingBg.setVisibility(View.GONE);// 截取框消失
					} else {
						Toast.makeText(getApplicationContext(), "图片处理失败", Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			});

		}
	}
}