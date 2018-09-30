package com.disnodeteam.dogecv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.corningrobotics.enderbots.endercv.R;
import org.opencv.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 9/15/2018.
 */

public class DrawViewSource extends View {

    private Paint mPaint;
    private Resources resources;
    private Bitmap bitmap;

    public DrawViewSource(Context context) {
        // TODO Auto-generated constructor stub
        super(context);
        init(context, null, 0);
    }

    public DrawViewSource(Context context, AttributeSet attrs) {
        // TODO Auto-generated constructor stub
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DrawViewSource(Context context, AttributeSet attrs, int defStyleAttr) {
        // TODO Auto-generated constructor stub
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        // TODO Auto-generated method stub
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        resources = context.getResources();
        bitmap = BitmapFactory
                .decodeResource(resources, R.drawable.feedback_shape_orange);


        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @SuppressLint("NewApi")
    public DrawViewSource(Context context, AttributeSet attrs,
                          int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    public void onFrame(Bitmap map){
        bitmap = map;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);


        // commented out bc this can add distortion to the image
        // maximize size of the bitmap to remove black borders in portrait orientation
        //mCacheBitmap = Bitmap.createScaledBitmap(mCacheBitmap, canvas.getHeight(), canvas.getWidth(), true);

        int deviceOrientation = getContext().getResources().getConfiguration().orientation;


        canvas.drawBitmap(bitmap, 0, 0,mPaint);

    }
}