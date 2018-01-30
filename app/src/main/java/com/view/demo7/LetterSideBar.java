package com.view.demo7;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/1/29.
 */
public class LetterSideBar extends View {

    // 定义26个字母
    public static String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint mPaint;

    //当前触摸的位置字母
    private String mCurrentTouchLetter ;


    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //自定义属性，颜色、字体大小  这里是写死的，没有写自定义属性
        mPaint.setTextSize(sp2px(16));// 设置的是像素
        //默认颜色
        mPaint.setColor(Color.BLUE);
    }

    // sp 转 px
    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, getResources().getDisplayMetrics());
    }


    /**
     *  测量控件的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //宽度 = 左右的padding + 字母宽度(取决于你的画笔)  详情见图即可

        //获取字母宽度
        int textWidth = (int) mPaint.measureText("A");
        //宽度 [计算指定宽度]
        int width = getPaddingLeft() + getPaddingRight() + textWidth ;

        //高度 [可以直接获取]
        int height = MeasureSpec.getSize(heightMeasureSpec) ;

        //算出宽高后测量宽高
        setMeasuredDimension(width , height);
    }


    /**
     * 画26个字母
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        int x = getPaddingLeft() ;

        //每一个字母的高度 总共高度/字母总个数
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length ;

        for (int i = 0; i < mLetters.length; i++) {

            //知道每个字母中心的位置
            //第一个字母中心位置是：字母高度的一半，
            // 第二个字母中心位置是：字母高度一半[itemHeight/2] + 前边字母的高度[i*itemHeight]
            int letterCenterY = itemHeight/2 + i*itemHeight + getPaddingTop() ;

            //基线，基于中心位置 [基线求法为套路，记住即可]
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics() ;
            int dy = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
            int baseLine = letterCenterY + dy ;

            //获取字母宽度
            int textWidth = (int) mPaint.measureText(mLetters[i]);

            int x = getWidth()/2 - textWidth/2 ;

            //如果当前字母高亮，将画笔设置为空色 ；否则设置为蓝色
            if (mLetters[i].equals(mCurrentTouchLetter)){
                mPaint.setColor(Color.RED);
                canvas.drawText(mLetters[i], x, baseLine, mPaint);
            }else{
                mPaint.setColor(Color.BLUE);
                canvas.drawText(mLetters[i] , x , baseLine , mPaint);
            }

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            //ACTION_DOWN和ACTION_MOVE效果相同
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算出当前的字母,获取当前手指触摸的位置，然后重新绘制
                float currentMoveY = event.getY();
                //当前位置 =  currentMoveY/字母高度 ,
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                int currentPosition = (int) (currentMoveY/itemHeight);
                if (currentPosition < 0){
                    currentPosition = 0 ;
                }

                if (currentPosition > mLetters.length- 1){
                    currentPosition = mLetters.length - 1 ;
                }

                mCurrentTouchLetter = mLetters[currentPosition] ;


                if (mListener != null){
                    mListener.touch(mCurrentTouchLetter , true);
                }
                //重新绘制
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (mListener != null){
                    mListener.touch(mCurrentTouchLetter , false);
                }
                 break;
        }
        return true;

    }



    private LetterTouchListener mListener ;
    public interface LetterTouchListener{
        void touch(CharSequence letter , boolean isTouch) ;
    }
    public void setonLetterTouchListener(LetterTouchListener listener){
        this.mListener = listener ;
    }

}
