package com.indicateprogressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Author: wangzhongming<br/>
 * Date :  2017/11/8 11:33 </br>
 * Summary: 带指示器的进度条
 */

public class IndicateProgressBar extends View {

    private float x = 10;
    private String progressText = "0%";
    private static final String TAG = IndicateProgressBar.class.getSimpleName();
    private int width;
    private int height;

    private Paint backPaint;
    private Paint progressPaint;
    private Paint indicateTextPaint;
    private Paint indicateBackPaint;
    private int radius = 10; //进度条四个角的角度px
    private int indicatorRadius = 32; //进度指示器四个角的角度px
    private int defaultMargin = 30; //进度指示器默认多一点长度
    private int max = 100;//进度最大值
    private int progress = 0;//进度0-100
    private boolean isCanTouch = true;//进度条是否可以手动拖动
    private int startProgressColor = 0xfff29310;
    private int textColor = 0xffef4f37;
    private int gray = 0xfff5f5f5;

    public IndicateProgressBar(Context context) {
        this(context, null);
    }

    public IndicateProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
//        Android在用画笔的时候有三种Style，分别是
//        Paint.Style.STROKE 只绘制图形轮廓（描边）//空心效果
//        Paint.Style.FILL 只绘制图形内容
//        Paint.Style.FILL_AND_STROKE 既绘制轮廓也绘制内容

        //Paint.ANTI_ALIAS_FLAG 抗锯齿
        //进度条背景画笔
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(gray);
        backPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //进度条进度画笔
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);

        //进度条指示器框画笔
        indicateBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicateBackPaint.setColor(textColor);
        indicateBackPaint.setTextSize(32);

        //进度条指示器文本画笔
        indicateTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicateTextPaint.setColor(textColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        width = getWidth() - (int) (indicateTextPaint.measureText(max + "%") + defaultMargin);
        height = getHeight();
//画背景
        RectF backRectF = new RectF(0, height * 2 / 5, width, height * 3 / 5);
        canvas.drawRoundRect(backRectF, radius, radius, backPaint);

        //画进度
        RectF progressRectF = new RectF(0, height * 2 / 5, width * getScale(), height * 3 / 5);
        Shader shader = new LinearGradient(0, 0, 400, 400, startProgressColor, Color.RED, Shader.TileMode.REPEAT);//渐变
        progressPaint.setShader(shader);
        canvas.drawRoundRect(progressRectF, radius, radius, progressPaint);

        //画指示器边框
        float left = getScale() * width;
        float right = getScale() * width + indicateTextPaint.measureText(max + "%") + defaultMargin;

        if (left <= 0f) {
            left = 0f;
            right = indicateTextPaint.measureText(max + "%") + defaultMargin;
        }
        if (left >= width) {
            left = width;
            right = width + indicateTextPaint.measureText(max + "%") + defaultMargin;
        }
        RectF indicatorRectF = new RectF(left, height / 5, right, height * 4 / 5);
        indicateBackPaint.setColor(textColor);
        canvas.drawRoundRect(indicatorRectF, indicatorRadius, indicatorRadius, indicateBackPaint);
        //画指示器内部为白色
        RectF indicatorContentRectF = new RectF(left + 2, height / 5 + 2, right - 2, height * 4 / 5 - 2);
        indicateBackPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(indicatorContentRectF, indicatorRadius, indicatorRadius, indicateBackPaint);

        //画指示器文本
        float textX = indicatorContentRectF.centerX() - indicateTextPaint.measureText(progressText) / 2;
        float textY = backRectF.centerY() + height / 9;
        canvas.drawText(progressText, textX, textY, indicateTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCanTouch) {//开启可手动拖动
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = event.getX();//基于控件的坐标
                /*float rawX = event.getRawX();//基于屏幕的坐标
                Log.i(TAG, "x==: " + x);
                Log.i(TAG, "rawX==: " + rawX);
                Log.i(TAG, "width==: " + width);
                Log.i(TAG, "height==: " + height);*/
                    int count = (int) x * 100 / width;
                    if (count > 100) {
                        count = 100;
                    } else if (count < 0) {
                        count = 0;
                    }
                    progressText = count + "%";
                    setProgress(count);
                    Log.i(TAG, "progressText==: " + progressText);
                    invalidate();//主线程中调用刷新
                    // postInvalidate();//可在非UI线程中调用刷新,底层还是使用handler发送到主线程刷新重绘

                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }

            return true;
        }else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 是否开启拖动有效
     * 默认可以手动拖动
     *
     * @param isOpen true开启可以手动拖动
     */
    public void setCanTouch(boolean isOpen) {
        isCanTouch = isOpen;
    }


    /**
     * 设置进度，getScale（）内会调用
     *
     * @param progress 0-100 ，最大进度默认100
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * 设置进度，getScale（）内会调用
     *
     * @param progress 进度 0-100
     * @param max      最大进度 ，不写则默认100
     */
    public void setProgress(int progress, int max) {
        this.progress = progress;
        this.max = max;
    }

    /**
     * 进度显示百分数
     *
     * @param strText 如写 70%
     */
    private void setProgeressText(String strText) {
        progressText = strText;
    }

    /**
     * 进度比例小数
     *
     * @return
     */
    private float getScale() {
        float scale;
        if (max == 0) {
            scale = 0;
        } else {
            scale = (float) progress / (float) max;
        }
        setProgeressText((int) (scale * 100) + "%");
        return scale;
    }


}
