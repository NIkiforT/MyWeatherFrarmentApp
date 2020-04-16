package nikifor.tatarkin.myweatherfrarmentapp.myview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import nikifor.tatarkin.myweatherfrarmentapp.R;

public class TempView extends View {
    private static final String TAG = "myTeeeeeV";
    private int tempColor = getResources().getColor(R.color.colorText);
    private int levelColor = getResources().getColor(R.color.colorYellow);
    private RectF tempRectangle = new RectF();
    private RectF levelRectangle = new RectF();
    private RectF headRectangle = new RectF();
    private Paint tempPaint;
    private Paint levelPaint;

    private int width = 0;
    private int height = 0;
    private int level;

    private final static int padding = 9;
    private final static int round = 40;
    private final static int headWidth = 100;

    public TempView(Context context) {
        super(context);
        init();
    }


    public TempView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public TempView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();

    }

    public TempView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TempView, 0,0);
        tempColor = typedArray.getColor(R.styleable.TempView_temp_color, getResources().getColor(R.color.colorText));
        levelColor = typedArray.getColor(R.styleable.TempView_level_color, getResources().getColor(R.color.colorYellow));
        level = typedArray.getInteger(R.styleable.TempView_level, 0);
    }

    private void init() {
        tempPaint = new Paint();
        tempPaint.setColor(tempColor);
        tempPaint.setStyle(Paint.Style.FILL);
        levelPaint = new Paint();
        levelPaint.setColor(levelColor);
        levelPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();


        tempRectangle.set(padding,
                padding,
                width - padding - headWidth + 20,
                height - padding);
        headRectangle.set(width - padding - headWidth,
                4 * padding,
                width - padding,
                height - 4 * padding);
        levelRectangle.set(2 * padding,
                4 * padding,
                (int)((width - 2 * padding - headWidth)*((double)level/(double)100)),
                height - 4 * padding);
        Log.d(TAG, "рисуем");
        Log.d(TAG, String.valueOf(level));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(tempRectangle, round, round, tempPaint);
        canvas.drawRoundRect(levelRectangle, round,round, levelPaint);
        canvas.drawRoundRect(headRectangle, round, round, tempPaint);
    }

//    public void setLevel (float temp){
//        try {
//            if (temp <= 0.0f) {
//                level = 0;
//            } else {
//                level = (int) temp;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.d(TAG, "Данные не верны");
//        }
//        Log.d(TAG, String.valueOf(level));
//        invalidate();
//    }
}
