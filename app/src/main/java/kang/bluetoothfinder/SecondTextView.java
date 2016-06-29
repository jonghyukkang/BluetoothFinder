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
public class SecondTextView extends View {

    private Paint mPaintText;
    private String mAverageValue;

    public SecondTextView(Context context, int average){
        super(context);
        init();
        this.mAverageValue = String.valueOf(average);
    }

    public SecondTextView(Context context, AttributeSet attr){
        super(context, attr);
        init();
    }

    public void init(){
        mPaintText = new Paint();
        mPaintText.setColor(Color.argb(255, 0, 150, 80));
        mPaintText.setTextSize(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mAverageValue, 310, 478, mPaintText);
    }

}
