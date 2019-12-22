package com.codebreak.bank;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PinDotView extends View {
    private  final int COUNT;
    private  int dotCount;
    private float dotWidth;
    private float strokeWidth;
    private float dotSize;
    private float dotGap;
    private Paint paint;
    private final float width, height;
    private int fillCount;

    public PinDotView(Context context) {
        this(context,null);
    }

    public PinDotView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PinDotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float density =context.getResources().getDisplayMetrics().density;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PinDotView);
        COUNT = attributes.getInteger(R.styleable.PinDotView_dotCount,4);
        width = density*24*(2*COUNT-1);
        height = density*24;
        strokeWidth = density*2;
        y = Math.round(height/2);
        paint = new Paint();
        fillCount=0;
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);


    }

    int x = 0;
    int y ;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int count = canvas.save();
        canvas.translate(height/2,height/2);
        for(int i=1;i<=COUNT;i++)
        {
            if(i<=fillCount) {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
            else
                paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(0,0, (height/2-strokeWidth/2),paint);
            canvas.translate(height*2,0);
        }
        canvas.restoreToCount(count);
    }

    public void setFillCount(int fillCount)
    {

        this.fillCount = fillCount;
        invalidate();
    }

    public void unFillAll()
    {
        this.fillCount=0;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(Math.round(width),Math.round(height));
    }
}
