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
public class SecondBarView extends View {
    private Paint mPaint2;
    private int mRssinum;

    public SecondBarView(Context context, int num){
        super(context);
        init();
        this.mRssinum = num;
    }

    public SecondBarView(Context context, AttributeSet attr){
        super(context, attr);
        init();
    }

    public void init(){
        mPaint2 = new Paint();
        mPaint2.setColor(Color.argb(255, 0, 150, 80));
        mPaint2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(300, mRssinum, 350, 800, mPaint2);
    }
}