package com.zhongou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义view,裁剪的正方形方框
 * 
 * @author JackSong
 *
 */
public class ClipViews extends View{
	
	public ClipViews(Context context){
		super(context);
	}

	public ClipViews(Context context, AttributeSet attrs){
		super(context, attrs);
	}


	public ClipViews(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		/*这里就是绘制矩形区域*/
		int width = this.getWidth();
		int height = this.getHeight();
		
		Paint paint = new Paint();
		paint.setColor(Color.BLACK); //0xaa000000
		paint.setAlpha(99);
		paint.setAntiAlias(true);
		 
		Paint linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setAntiAlias(true); 
		
		int yStart = (height - width)/2; //start和end都是相对于当前视图的坐标点
		int yEnd = yStart + width;
		
		//画遮挡阴影
		//top
		canvas.drawRect(0, 0, width, yStart, paint);
		
		//bottom
		canvas.drawRect(0, yEnd, width, height, paint);
		
		//画线
		//top
		canvas.drawLine(0, yStart, width, yStart, linePaint);
		//left
		canvas.drawLine(0, yStart, 1, yEnd, linePaint); 
		//right
		canvas.drawLine(width-1, yStart, width, yEnd, linePaint);
		//bottom
		canvas.drawLine(0, yEnd, width, yEnd, linePaint);
		
	}
	
}