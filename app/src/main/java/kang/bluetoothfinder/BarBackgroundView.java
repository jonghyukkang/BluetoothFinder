package kang.bluetoothfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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

        // Graph Line x, y축 그리기

        mPaintLine = new Paint();
        mPaintLine.setColor(Color.BLACK);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(3);

        canvas.drawLine(80, 150, 80, 800, mPaintLine);
        canvas.drawLine(80, 800, 700, 800, mPaintLine);

        // Graph Line 수치선 그리기

        mPaintLineSmall = new Paint();
        mPaintLineSmall.setColor(Color.LTGRAY);
        mPaintLineSmall.setStyle(Paint.Style.STROKE);
        mPaintLineSmall.setStrokeWidth(1);
        canvas.drawLine(80, 180, 700, 180, mPaintLineSmall);
        canvas.drawLine(80, 280, 700, 280, mPaintLineSmall);
        canvas.drawLine(80, 380, 700, 380, mPaintLineSmall);
        canvas.drawLine(80, 480, 700, 480, mPaintLineSmall);
        canvas.drawLine(80, 580, 700, 580, mPaintLineSmall);
        canvas.drawLine(80, 680, 700, 680, mPaintLineSmall);

        // Graph y축 수치값 그리기

        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(30);

        String[] str1 = new String[]{"-30", "-40", "-50", "-60", "-70", "-80", "-90"};
        canvas.drawText(str1[0], 25, 190, mPaintText);
        canvas.drawText(str1[1], 25, 290, mPaintText);
        canvas.drawText(str1[2], 25, 390, mPaintText);
        canvas.drawText(str1[3], 25, 490, mPaintText);
        canvas.drawText(str1[4], 25, 590, mPaintText);
        canvas.drawText(str1[5], 25, 690, mPaintText);
        canvas.drawText(str1[6], 25, 790, mPaintText);

    }
}
