package bwie.com.jdemo.utils;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import bwie.com.jdemo.R;

public class BarrageView extends RelativeLayout {
    private int mRowNum = 6;
    private long mDuration = 5000;//弹幕在屏幕显示的时间 默认5s
    private int mAlpha = 180;//背景的透明度0-255
    private int mDirection = FROM_RIGNG_TO_LEFT;//当前弹幕活动方向 默认从右到左
    public static final int FROM_LEFT_TO_RIGHT = 1;//从左到右
    public static final int FROM_RIGNG_TO_LEFT = 2;//从右到左
    private int mScreenWidth;
    private List<TextView> mChildView;
    private LinkedList mRowPosList;
    public BarrageView(Context context) {
        super(context);
        init(context);
    }
    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        DisplayMetrics dm = new DisplayMetrics();
       //获取屏幕信息
        mScreenWidth= dm.widthPixels;
        mChildView = new ArrayList<TextView>();
        mRowPosList = new LinkedList();
    }
    /**
     * 设置弹幕飘动方向
     * @param direction 弹幕飘动方向 默认从右到左 FROM_RIGNG_TO_LEFT
     */
    public void setDirection(int direction){
        mDirection = direction;
    }
    /**
     * 设置弹幕飘屏时间
     * @param duration 弹幕飘屏时间 默认5s
     */
    public void setDuration(long duration){
        mDuration = duration;
    }
    /**
     * 设置飘屏行数
     * @param rowNum 飘屏行数 默认6条
     */
    public void setRowNum(int rowNum){
        mRowNum = rowNum;
    }
    /**
     * 设置item的背景透明度 范围：0~255
     * @param alpha 取值0~255  0为全透明
     */
    public void setBackgroundAlpha(int alpha){
        mAlpha = alpha;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = this.getChildCount();
        for(int i = 0; i < childCount; i++){
            final View view = getChildAt(i);
            if (view != null){
                RelativeLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
                if (lp.leftMargin <= 0){
                    if(mDirection == FROM_RIGNG_TO_LEFT){
                        view.layout(mScreenWidth,lp.topMargin,mScreenWidth+view.getMeasuredWidth(),
                                lp.topMargin+view.getMeasuredHeight());
                    }else if(mDirection == FROM_LEFT_TO_RIGHT){
                        view.layout(-view.getMeasuredWidth(),lp.topMargin,0,
                                lp.topMargin+view.getMeasuredHeight());
                    }
                }
            }
        }
    }
    public void addBarrageItemView(Context context,String text,int textSize,int textColor){
        createBarrageItemView(context,text,textSize,textColor);
    }
    public void addBarrageItemView(Context context,String text){
        createBarrageItemView(context,text,0,0);
    }
    private void createBarrageItemView(Context context,String text,int textSize,int textColor){
        final TextView textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.a9v);
        if (textColor != 0){
            textView.setTextColor(getResources().getColor(textColor));
        }
        if (textSize != 0){
            textView.setTextSize(textSize);
        }
        textView.setText(text);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        textView.measure(w, h);
        int height =textView.getMeasuredHeight();
        int width = textView.getMeasuredWidth();
        int row = new Random().nextInt(100) % mRowNum;
        while (needResetRow(row)){
            row = new Random().nextInt(100) % mRowNum;
        }
        mRowPosList.add(row);
        RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.topMargin = row * (height +100);
//        lastRow = row;
        textView.setLayoutParams(lp);
        textView.setPadding(15,2, 15,2);
        textView.getBackground().setAlpha(mAlpha);
        this.addView(textView);
        ViewPropertyAnimator animator = null;
        if(mDirection == FROM_RIGNG_TO_LEFT){
            animator = textView.animate()
                    .translationXBy(-(mScreenWidth + width + 80));
        }else if(mDirection == FROM_LEFT_TO_RIGHT){
            animator = textView.animate()
                    .translationXBy(mScreenWidth + width + 80);
        }
        animator.setDuration(mDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
//                mChildView.remove(textView);
                BarrageView.this.removeView(textView);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mChildView.add(textView);
    }
    public boolean needResetRow(int row){
        int size = mRowPosList.size();
        int sameRowPos =-1;
        for(int i = size; i > 0; i--){
            if (row ==mRowPosList.size()){
                sameRowPos = i-1;
                break;
            }
        }
        if (sameRowPos != -1){
            TextView tv = mChildView.get(sameRowPos);
            if (mScreenWidth -tv.getX() < tv.getWidth()){
                return true;
            }
        }
        return false;
    }
}