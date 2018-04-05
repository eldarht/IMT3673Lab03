/*package com.example.handin3;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by flero on 4/3/18.
 */
/*
public class Board extends ViewGroup {
    private RectF mBoundrary;
    private RectF mInnerBoundrary;
    private Ball ball;
    private Paint mPaintBoundrary;
    private Paint mPaintInner;

    public Board(Context context) {
        super(context);
        init(context);
    }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Force the background to software rendering because otherwise the Blur
        // filter won't work.
        if (!this.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // setup initial member value
        DisplayMetrics screenInfo = context.getResources().getDisplayMetrics();
        mBoundrary = new RectF(0, 0, screenInfo.widthPixels, screenInfo.heightPixels);
        mInnerBoundrary = new RectF(3, 3, screenInfo.widthPixels-3, screenInfo.heightPixels-3);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFFFFFF00);
        mPaint.setStyle(Paint.Style.FILL);
        mPosition = new PointF(0.0f, 0.0f);

    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
*/