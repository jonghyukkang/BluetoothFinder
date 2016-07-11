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
    private Paint mPaint1 = new Paint();
    private int mRssinum;
    private int id;

    public FirstBarView(Context context) {
        super(context);
    }

    public FirstBarView(Context context, int num, int id) {
        super(context);
        this.mRssinum = num;
        this.id = id;
    }

    public FirstBarView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getMeasuredWidth();
        float height = getMeasuredHeight();

        switch (id) {
            case 0:
                mPaint1.setColor(Const.COLOR_RED);
                mPaint1.setStyle(Paint.Style.FILL);
                if (mRssinum < 1600) {
                    canvas.drawRect((float)(width/4.8), mRssinum, (float)(width/3.6), height-74, mPaint1);
                } else {
                    mPaint1.setTextSize(40);
                    int prevRssinum = (mRssinum + 120) / 10;
                    canvas.drawText("-" + String.valueOf(prevRssinum), (float)(width/4.5), (float)(height/1.8), mPaint1);
                }
                break;

            case 1:
                mPaint1.setColor(Const.COLOR_GREEN);
                mPaint1.setStyle(Paint.Style.FILL);
                if (mRssinum < 1600) {
                    canvas.drawRect((float)(width/2.4), mRssinum, (float)(width/2.05), height-74, mPaint1);
                } else {
                    mPaint1.setTextSize(40);
                    int prevRssinum = (mRssinum + 120) / 10;
                    canvas.drawText("-" + String.valueOf(prevRssinum), (float)(width/2.3), (float)(height/1.8), mPaint1);
                }
                break;

            case 2:
                mPaint1.setColor(Const.COLOR_BLUE);
                mPaint1.setStyle(Paint.Style.FILL);
                if (mRssinum < 1600) {
                    canvas.drawRect((float)(width/1.6), mRssinum, (float)(width/1.44), height-74, mPaint1);
                } else {
                    mPaint1.setTextSize(40);
                    int prevRssinum = (mRssinum + 122) / 10;
                    canvas.drawText("-" + String.valueOf(prevRssinum), (float)(width/1.6), (float)(height/1.8), mPaint1);
                }
                break;
        }
    }
}