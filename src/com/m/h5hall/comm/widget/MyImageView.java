package com.m.h5hall.comm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * 自定义ImageView
 * @author yangsc
 */
public class MyImageView extends ImageView {
    public MyImageView(Context context) {
        super(context);
    }
 
    public MyImageView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
    	try {  
            super.onDraw(canvas);
        } catch (Exception e) {  
        	Log.w("sexyshow", "MyImageView  -> onDraw() Canvas: trying to use a recycled bitmap");  
        }  
    }
}