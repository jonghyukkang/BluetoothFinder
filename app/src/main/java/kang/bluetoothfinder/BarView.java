package kang.bluetoothfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kangjonghyuk on 2016. 6. 24..
 */
public class BarView extends View {
    private Paint mPaint;
    private int mRssinum;
    private float mPos_left, mPos_right, mPos_bottom;
    private int mBarColor;

    public BarView(Context context) {
        super(context);
    }

    public BarView(Context context, int num, float left, float right, float bottom, int color) {
        super(context);
        this.mRssinum = num;
        this.mPos_left = left;
        this.mPos_right = right;
        this.mPos_bottom = bottom;
        this.mBarColor = color;
        init();
    }

    public void init(){
        mPaint = new Paint();
        mPaint.setTextSize(60);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBarColor);
    }

    public BarView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int PrevRssinum = (mRssinum + 260) / 20;

        if (mRssinum < 1600) {
            canvas.drawRect(mPos_left, mRssinum, mPos_right, mPos_bottom, mPaint);
            canvas.drawText("-" + String.valueOf(PrevRssinum), mPos_left, mRssinum - 30, mPaint);
        } else {
            canvas.drawText("-" + String.valueOf(PrevRssinum), mPos_left, 900, mPaint);
        }

    }
}
