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
        switch (id) {
            case 0:
                mPaint1.setColor(Const.COLOR_RED);
                mPaint1.setStyle(Paint.Style.FILL);
                if (mRssinum < 798) {
                    canvas.drawRect(150, mRssinum, 200, 800, mPaint1);
                } else {
                    mPaint1.setTextSize(40);
                    int prevRssinum = (mRssinum + 122) / 10;
                    canvas.drawText("-" + String.valueOf(prevRssinum), 160, 478, mPaint1);
                }
                break;

            case 1:
                mPaint1.setColor(Const.COLOR_GREEN);
                mPaint1.setStyle(Paint.Style.FILL);
                if (mRssinum < 798) {
                    canvas.drawRect(300, mRssinum, 350, 800, mPaint1);
                } else {
                    mPaint1.setTextSize(40);
                    int prevRssinum = (mRssinum + 122) / 10;
                    canvas.drawText("-" + String.valueOf(prevRssinum), 310, 478, mPaint1);
                }
                break;

            case 2:
                mPaint1.setColor(Const.COLOR_BLUE);
                mPaint1.setStyle(Paint.Style.FILL);
                if (mRssinum < 798) {
                    canvas.drawRect(450, mRssinum, 500, 800, mPaint1);
                } else {
                    mPaint1.setTextSize(40);
                    int prevRssinum = (mRssinum + 122) / 10;
                    canvas.drawText("-" + String.valueOf(prevRssinum), 450, 478, mPaint1);
                }
                break;
        }
    }


}