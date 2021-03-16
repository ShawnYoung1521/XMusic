package com.tw.music.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.tw.music.R;

/**
 * Created by will on 2018/7/13.
 */

public class MyDashboardView extends View {

	// 白色圆弧画笔
	private Paint whiteArcPaint;

	// 蓝色圆弧画笔
	private Paint blueArcPaint;

	// 称重数据画笔
	private Paint weightDataPaint;

	// 刻度数据画笔
	private Paint scaleDataPaint;

	// 圆弧矩形范围
	private RectF oval;

	private float currentData = 0;

	// 圆弧经过的角度范围
	private float sweepAngle = 240;

	public  float mCurrentProgress = -1;
	// 圆弧最小值
	public float minAngle = 140;
	// 圆弧最大值
	private float maxAngle = 250;
	
	//总共有多少刻度
	private int allItem = 14;
	private float itemAngle = (maxAngle)/allItem;
	//总共有多少刻度
	
	private int mCenterWidth = 275;
	private float mCenterHight = 275;
	private OnAngleChangeListener mOnAngleChangeListener;

	private int width;

	private int height;

	private Paint pointerPaint;

	public interface OnAngleChangeListener{
	    void onStartTrackingTouch(MyDashboardView pergrass, int position, boolean fromUser);
        void onStopTrackingTouch(MyDashboardView pergrass, int position, boolean fromUser);
	}
	public void setOnAngleChangeListener(OnAngleChangeListener l){
	    mOnAngleChangeListener = l;
	}
	void onStartTrackingTouch() {
        if (mOnAngleChangeListener != null) {
            mOnAngleChangeListener.onStartTrackingTouch(this,(int) (getCurrentAngle()/itemAngle),fromUser);
        }
    }

    void onStopTrackingTouch() {
        if (mOnAngleChangeListener != null) {
           
			mOnAngleChangeListener.onStopTrackingTouch(this,(int) (getCurrentAngle()/itemAngle),fromUser);
        }
    }

	@SuppressLint("NewApi")
	public MyDashboardView(Context context, AttributeSet attrs) {
		super(context, attrs);

		whiteArcPaint = new Paint();
		whiteArcPaint.setAntiAlias(true);
		whiteArcPaint.setColor(Color.WHITE);
		whiteArcPaint.setStyle(Paint.Style.STROKE);

		blueArcPaint = new Paint();
		blueArcPaint.setAntiAlias(true);
		blueArcPaint.setColor(Color.parseColor("#3a84f4"));
		blueArcPaint.setStyle(Paint.Style.STROKE);
		blueArcPaint.setStrokeWidth(13);
		blueArcPaint.setShadowLayer((float) 10, (float) 10, (float) 10,
				Color.parseColor("#99000000"));

		weightDataPaint = new Paint();
		weightDataPaint.setColor(Color.WHITE);
		weightDataPaint.setTextSize(37);
		weightDataPaint.setStyle(Paint.Style.STROKE);

		scaleDataPaint = new Paint();
		scaleDataPaint.setColor(Color.WHITE);
		scaleDataPaint.setTextSize(26);
		
		pointerPaint = new Paint();
		pointerPaint.setAntiAlias(true);
		pointerPaint.setColor(Color.WHITE);
		pointerPaint.setStyle(Paint.Style.STROKE);
		setLayerType(LAYER_TYPE_SOFTWARE, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		mCenterWidth = width / 2;
		mCenterHight = height / 2;

		oval = new RectF(width * (float) 0.24, width * (float) 0.25, width
				* (float) 0.77, width * (float) 0.78);
		setMeasuredDimension(width, width);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		// 控件宽、高
		width = height = Math.min(h, w);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		drawBlueArc(canvas);
		drawArrow(canvas);

	}

	private void drawBlueArc(Canvas canvas) {
		canvas.save();
		canvas.drawArc(oval, minAngle, mCurrentProgress, false, blueArcPaint);
	}

	private void drawArrow(Canvas canvas) {
		canvas.save();
		oldBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.xuanniu_point);
//		int width = oldBitmap.getWidth();
//		int height = oldBitmap.getHeight();
//		int newWidth = (int) (getWidth() * 0.8);
//		float scaleWidth = ((float) newWidth) / width;
//		float scaleHeight = ((float) newWidth) / height;
//		Matrix matrix = new Matrix();
//		// TODO 下面这两个顺序不能变
//		matrix.setRotate((mCurrentProgress+75 ),
//				oldBitmap.getWidth() / 2, oldBitmap.getHeight() / 2);
//		matrix.postScale(scaleWidth, scaleHeight);
//		// 将按钮移到中心位置
//		matrix.postTranslate((this.width - width) / 2, (this.height - height) / 2);
//		Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, width, height,
//				matrix, true);
//		canvas.drawBitmap(newBitmap, getWidth() / 2 - newBitmap.getWidth() / 2,
//				getHeight() / 2 - newBitmap.getHeight() / 2, whiteArcPaint);
//		oldBitmap.recycle();
//		newBitmap.recycle();
		
//		// 按钮宽高
		int buttonWidth = oldBitmap.getWidth();
		int buttonHeight = oldBitmap.getHeight();

		// 绘制按钮阴影

		Matrix matrix = new Matrix();
		// 设置按钮位置
		matrix.setTranslate(buttonWidth / 2, buttonHeight / 2);
		// 设置旋转角度
		matrix.preRotate(mCurrentProgress );
		// 按钮位置还原，此时按钮位置在左上角
		matrix.preTranslate(-buttonWidth / 2, -buttonHeight / 2);
		// 将按钮移到中心位置
		matrix.postTranslate((width - buttonWidth) / 2, (height - buttonHeight) / 2);

		// 设置抗锯齿
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG));
		canvas.drawBitmap(oldBitmap, matrix, pointerPaint);
		oldBitmap.recycle();
	}

	/*
	 * 增加set方法，用于在java代码中调用
	 */
	private synchronized void setProgress(float progress) {
		if (progress < 90) {
			progress = progress + 360;
		}
		if (progress>50&&progress<135) {
			return;
		}
		progress = progress - minAngle;
		if (progress > maxAngle) {
			progress = maxAngle;
		}
		if (progress < 0) {
			progress = 0;
		}
		mCurrentProgress = progress;
//		currentData  = progress;
		// mSeekBarDegree = (progress * 360 / mSeekBarMax);
		// setThumbPosition(Math.toRadians(mSeekBarDegree));
//		Log.d("gss", "mCurrentProgress==" + mCurrentProgress);
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		fromUser = true;
		float eventX = event.getX();
		float eventY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			seekTo(eventX, eventY, false);
			break;

		case MotionEvent.ACTION_MOVE:
			seekTo(eventX, eventY, false);
			onStartTrackingTouch();
			break;

		case MotionEvent.ACTION_UP:
			fromUser=false;
			seekTo(eventX, eventY, true);
			onStopTrackingTouch();
			break;
		}
		return true;
	}

	private void seekTo(float eventX, float eventY, boolean isUp) {

		double radian = Math
				.atan2(eventY - mCenterHight, eventX - mCenterWidth);
		/*
		 * 由于atan2返回的值为[-pi,pi] 因此需要将弧度值转换一下，使得区间为[0,2*pi]
		 */
//		Log.i("gss", "radian==" + radian);
		if (radian < 0) {
			radian = radian + 2 * Math.PI;
		}
//		Log.i("gss", "radian222==" + radian);
//		int currentData = ;
		// mCurrentProgress = (int) (maxAngle * currentData / 360);
		
		setProgress((float) Math.round(Math.toDegrees(radian)));

		// invalidate();

	}

	private boolean fromUser;

	private Bitmap oldBitmap;
	/**
	 * 根据刻度显示
	 * @param item
	 */
	public void setDashboardChange(int item) {
		float changeValue=item*((float)maxAngle/allItem);
		changeValue=changeValue>maxAngle?maxAngle:changeValue;
		changeValue=changeValue<0?0:changeValue;
		ValueAnimator animator = ValueAnimator.ofFloat(mCurrentProgress, changeValue);
		animator.setDuration(300);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mCurrentProgress =(Float) animation.getAnimatedValue();
				invalidate();
			}
		});
		animator.start();
	}
	private float getCurrentAngle(){
       return mCurrentProgress;
	}
}
