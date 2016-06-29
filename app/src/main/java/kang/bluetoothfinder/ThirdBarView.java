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

public class ThirdBarView extends View {
    private Paint mPaint3;
    private int mRssinum;

    public ThirdBarView(Context context, int num){
        super(context);
        init();
        this.mRssinum = num;
    }

    public ThirdBarView(Context context, AttributeSet attr){
        super(context, attr);
        init();
    }

    public void init(){
        mPaint3 = new Paint();
        mPaint3.setColor(Color.argb(255, 0, 0, 255));
        mPaint3.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(450, mRssinum, 500, 800, mPaint3);
    }
}