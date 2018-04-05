package com.example.handin3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by flero on 4/2/18.
 */

public class Ball extends View implements SensorEventListener {
    private float mRadius;
    private RectF mBoundraryBox;
    private Paint mPaint;

    private Drawable mBall;

    private long mLastTime;
    private float mLastTimeHit;

    private PointF mAcceleration;
    private PointF mVelocity;
    private PointF mPosition;
    private Vibrator mVibrator;

    public Ball(Context context){
        super(context);

        // setup the view
        init();
    }

    public Ball(Context context, AttributeSet attrs) {
        super(context, attrs);

        // setup the view
        init();
    }

    private void init() {

        // Force the background to software rendering because otherwise the Blur
        // filter won't work.
        if (!this.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        Context context = getContext();
        DisplayMetrics screenInfo = context.getResources().getDisplayMetrics();

        // setup initial member value
        mRadius = 100.0f;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFFFFFF00);
        mPaint.setStyle(Paint.Style.FILL);

        mPosition = new PointF((screenInfo.widthPixels / 2) - mRadius,(screenInfo.heightPixels / 2) - mRadius);
        mVelocity = new PointF(0.0f,0.0f);
        mAcceleration = new PointF(0.0f, 0.0f);

        mBoundraryBox = new RectF(
                mPosition.x,
                mPosition.y,
                mPosition.x + (2 * mRadius),
                mPosition.y + (2 * mRadius)
        );

        Resources res = getResources();
        mBall = res.getDrawable(R.drawable.ball, null);

        mLastTime = System.currentTimeMillis();

        mVibrator = getContext().getSystemService(Vibrator.class);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        // Set ball diameter to roughly 5% of width
        mRadius = w / 40.0f;
        Log.i("Ball.java","radius: " + mRadius);

        mBoundraryBox.set(
                mPosition.x,
                mPosition.y,
                mPosition.x + (2 * mRadius),
                mPosition.y + (2 * mRadius)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(mBoundraryBox, mPaint);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        try{
            float dt = ((System.currentTimeMillis() - mLastTime) )/1000;
            if (dt == 0.0f){
                dt = 0.1f;
            }
            mLastTime = System.currentTimeMillis();
            //x and y are flipped due to landscape mode
            updateAcceleration(sensorEvent.values[1], sensorEvent.values[0], sensorEvent.values[2]);
            updateVelocity(dt);
            updatePosition(dt);
        }finally {
            invalidate();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void updateAcceleration( float x, float y, float z) {
        mAcceleration.set(x, y);
    }
    private void updateVelocity(float dt) {

        mVelocity.offset(dt * mAcceleration.x, dt * mAcceleration.y);

    }

    private void updatePosition(float dt){

        final RectF tmpBallBoundraries = new RectF(mBoundraryBox);
        final RectF viewBoundraries = new RectF(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        tmpBallBoundraries.offset(dt * mVelocity.x, dt * mVelocity.y);

        mLastTimeHit = mLastTimeHit + dt;

        if (viewBoundraries.contains(tmpBallBoundraries)) {
            mPosition.offset(dt * mVelocity.x, dt * mVelocity.y);
            mBoundraryBox.set(tmpBallBoundraries);
        }
        else{
            mVelocity = new PointF(0,0);
            if (mLastTimeHit > 20){
                try {

                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
                    r.play();

                    if (!this.isInEditMode() && Build.VERSION.SDK_INT >= 26) {
                        mVibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE));
                    }else{
                        mVibrator.vibrate(100);
                    }
                    mLastTimeHit = 0;
                }catch (Exception e){
                    Log.w("Handin3", "Can not play sound");
                }
            }
        }
    }
}