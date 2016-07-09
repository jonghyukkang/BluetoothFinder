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
    private int id;

    public FirstBarView(Context context, int num, int id){
        super(context);
        this.mRssinum = num;
        this.id = id;
    }

    public FirstBarView(Context context, AttributeSet attr){
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch(id){
            case 1 :
                invalidate();
                mPaint1 = new Paint();
                mPaint1.setColor(Const.COLOR_ONE);
                mPaint1.setStyle(Paint.Style.FILL);
                canvas.drawRect(150, mRssinum, 200, 800, mPaint1);
                break;

            case 2 :
                invalidate();
                mPaint1 = new Paint();
                mPaint1.setColor(Const.COLOR_TWO);
                mPaint1.setStyle(Paint.Style.FILL);
                canvas.drawRect(300, mRssinum, 350, 800, mPaint1);
                break;

            case 3 :
                invalidate();
                mPaint1 = new Paint();
                mPaint1.setColor(Const.COLOR_THREE);
                mPaint1.setStyle(Paint.Style.FILL);
                canvas.drawRect(450, mRssinum, 500, 800, mPaint1);

        }
    }


}