package com.mindspree.days.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FitImageView extends ImageView
{
   
    public FitImageView(Context context) {
        super(context);
        setup();
    }
   
    public FitImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }
   
    public FitImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }
   
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if( getDrawable() != null)
        {
	        int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
	        setMeasuredDimension(width, height);
        }else {
        	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
   
    private void setup() {
        setScaleType(ScaleType.CENTER_CROP);
    }

}
