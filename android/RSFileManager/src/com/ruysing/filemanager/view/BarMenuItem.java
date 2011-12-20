package com.ruysing.filemanager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.ruysing.filemanager.R;

public class BarMenuItem extends View {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private String mText = null;
	private int mTextColor;
	private int mTextSize;
	private int mAscent;
	private float dx = 0;
	private float dy = 0;
	private int mDrawableId;
	private int mRadius;
	private boolean mCenter_width;
	private boolean mCenter_height;
	
	public BarMenuItem(Context context) {
		this(context,null);
	}
	public BarMenuItem(Context context,AttributeSet attrs){
		super(context,attrs);
		init();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BarMenuItem);
		CharSequence cs = a.getString(R.styleable.BarMenuItem_text);
		if(cs != null){
			setText(cs.toString());
		}
		int textSize = a.getDimensionPixelOffset(R.styleable.BarMenuItem_text_size, 0);
		if(textSize != 0){
			setTextSize(textSize);
		}
		int textColor = a.getColor(R.styleable.BarMenuItem_text_color, 0);
		if(textColor != 0){
			setTextColor(textColor);
		}
		int drawable = a.getResourceId(R.styleable.BarMenuItem_drawable, -1);
		if(drawable != -1){
			mDrawableId = drawable;
		}
		int x = a.getDimensionPixelOffset(R.styleable.BarMenuItem_dx, -1);
		if(x != -1){
			dx = x;
		}
		int y = a.getDimensionPixelOffset(R.styleable.BarMenuItem_dy, -1);
		if(y != -1){
			dy = y;
		}
		int radius = a.getDimensionPixelOffset(R.styleable.BarMenuItem_radius, -1);
		if(radius != -1){
			mRadius = radius;
		}
		mCenter_width = a.getBoolean(R.styleable.BarMenuItem_center_width, true);
		mCenter_height = a.getBoolean(R.styleable.BarMenuItem_center_height, true);
	}	
	private void init(){
		mTextColor = Color.WHITE;
		mTextSize = 12;
		mRadius = 0;
		mCenter_height = true;
		mCenter_width = true;
		mPaint.setTextAlign(Paint.Align.CENTER);
		mDrawableId = -1;
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setStrokeWidth(1);
		mBorderPaint.setColor(Color.RED);
	}
	private void setText(String text){
		mText = text;
		requestLayout();
		invalidate();
	}
	private void setTextSize(int textSize){
		mTextSize = textSize;
		mPaint.setTextSize(textSize);
		requestLayout();
		invalidate();
	}
	private void setTextColor(int color){
		mTextColor = color;
		mPaint.setColor(color);
	}
	protected void onDraw(Canvas canvas) {
//		Rect r = new Rect(0,0,getWidth(),getHeight());
//		canvas.drawRect(r, mBorderPaint);
		mPaint.setShadowLayer(mRadius, dx, dy, mTextColor);		
		//ÀLÑuBitmap
		if(mDrawableId != -1){
			Bitmap b = BitmapFactory.decodeResource(getResources(), mDrawableId);
			canvas.drawBitmap(b, getPaddingLeft(), getPaddingTop(), mPaint);
		}
		int tx = getPaddingLeft();
		int ty = getPaddingTop() - mAscent;
		if(mCenter_width){
			tx = (getWidth() + getPaddingLeft() + getPaddingRight()) / 2;
		}
		if(mCenter_height){
			ty = (getHeight() + getPaddingTop() + getPaddingBottom()) / 2;
		}
		canvas.drawText(mText, tx, ty , mPaint);
		super.onDraw(canvas);
	}
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}
	private int measureWidth(int w){
		int result = 0;
		int specMode = MeasureSpec.getMode(w);
		int specSize = MeasureSpec.getSize(w);
		if(specMode == MeasureSpec.EXACTLY){
			result = specSize;
		}else{
			result = getPaddingLeft() + getPaddingRight() + (int)mPaint.measureText(mText);
			if(specMode == MeasureSpec.AT_MOST){
				result = Math.min(result, specSize);
			}
		}
		return result;
	}
	private int measureHeight(int h){
		int result = 0;
		int specMode = MeasureSpec.getMode(h);
		int specSize = MeasureSpec.getSize(h);
		mAscent = (int)mPaint.ascent();
		if(specMode == MeasureSpec.EXACTLY){
			result = specSize;
		}else{
			result = (int)mPaint.descent() - mAscent + getPaddingBottom() + getPaddingTop();
			if(specMode == MeasureSpec.AT_MOST){
				result = Math.min(result, specSize);
			}
		}
		return result;
	}
}
