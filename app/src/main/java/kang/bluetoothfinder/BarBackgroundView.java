package kang.bluetoothfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by kangjonghyuk on 2016. 6. 16..
 */
public class BarBackgroundView extends View {

    Paint mPaintLine, mPaintLineSmall, mPaintText;

    public BarBackgroundView(Context context){
        super(context);
    }

    public BarBackgroundView(Context context, AttributeSet attrs){
        this(context, attrs,0);
    }

    public BarBackgroundView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getMeasuredWidth();
        float height = getMeasuredHeight();

        // Graph Line x, y축 그리기

        mPaintLine = new Paint();
        mPaintLine.setColor(Color.BLACK);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(3);

        canvas.drawLine(width/9, height/6, width/9, height-74, mPaintLine);
        canvas.drawLine(width/9, height-74, width-20, height-74, mPaintLine);

        // Graph Line 수치선 그리기

        mPaintLineSmall = new Paint();
        mPaintLineSmall.setColor(Color.LTGRAY);
        mPaintLineSmall.setStyle(Paint.Style.STROKE);
        mPaintLineSmall.setStrokeWidth(1);
        canvas.drawLine(width/9, (float)(height/4.9), width-20, (float)(height/4.9), mPaintLineSmall);
        canvas.drawLine(width/9, (float)(height/3.1), width-20, (float)(height/3.1), mPaintLineSmall);
        canvas.drawLine(width/9, (float)(height/2.3), width-20, (float)(height/2.3), mPaintLineSmall);
        canvas.drawLine(width/9, (float)(height/1.8), width-20, (float)(height/1.8), mPaintLineSmall);
        canvas.drawLine(width/9, (float)(height/1.5), width-20, (float)(height/1.5), mPaintLineSmall);
        canvas.drawLine(width/9, (float)(height/1.28), width-20, (float)(height/1.28), mPaintLineSmall);
        canvas.drawLine(width/9, (float)(height/1.11), width-20, (float)(height/1.11), mPaintLineSmall);

        // Graph y축 수치값 그리기

        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(30);

        String[] str1 = new String[]{"-30", "-40", "-50", "-60", "-70", "-80", "-90"};
        canvas.drawText(str1[0], (width/29), (float)(height/4.7), mPaintText);
        canvas.drawText(str1[1], (width/29), (float)(height/3.0), mPaintText);
        canvas.drawText(str1[2], (width/29), (float)(height/2.25), mPaintText);
        canvas.drawText(str1[3], (width/29), (float)(height/1.77), mPaintText);
        canvas.drawText(str1[4], (width/29), (float)(height/1.48), mPaintText);
        canvas.drawText(str1[5], (width/29), (float)(height/1.27), mPaintText);
        canvas.drawText(str1[6], (width/29), (float)(height/1.1), mPaintText);

    }
}
