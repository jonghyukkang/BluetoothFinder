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
public class FirstBarView extends View {
    private Paint mPaint1;
    private int mRssinum;

    public FirstBarView(Context context, int num){
        super(context);
        init();
        this.mRssinum = num;
    }

    public FirstBarView(Context context, AttributeSet attr){
        super(context, attr);
        init();
    }

    public void init(){
        mPaint1 = new Paint();
        mPaint1.setColor(Color.RED);
        mPaint1.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(150, mRssinum, 200, 800, mPaint1);
    }
}