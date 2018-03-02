package com.andyren.pressanim.weiget;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by Andy.Ren on 2017/12/30.
 */
public class PressedAnimationLayout extends RelativeLayout {

    private ObjectAnimator downAnim,upAnim;
    private ClickListener listener;

    public PressedAnimationLayout(Context context) {
        this(context,null);
    }

    public PressedAnimationLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PressedAnimationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /**
         * ObjectAnimator顾名思义,继承自ValueAnimator,所以ValueAnimator所能使用的方法,ObjectAnimator都可以使用,
         * ObjectAnimator同时也重写了几个方法,比如：ofFloat()、ofInt()等
         * ObjectAnimator提供了一个ofPropertyValuesHolder方法，使用PropertyValuesHolder来构造动画.
         *
         * PropertyValuesHolder这个类的意义就是，它其中保存了动画过程中所需要操作的属性和对应的值.
         * 通过ofFloat(Object target, String propertyName, float… values)构造的动画，
         * ofFloat()的内部实现其实就是将传进来的参数封装成PropertyValuesHolder实例来保存动画状态.
         * 在封装成PropertyValuesHolder实例以后，后期的各种操作也是以PropertyValuesHolder为主的.
         *
         * ofFloat()、ofInt()参数：
         * propertyName：表示ObjectAnimator需要操作的属性名.
         *              也就是ObjectAnimator需要通过反射查找对应属性的setProperty()函数的那个property.
         *              ObjectAnimator做动画，并不是根据控件xml中的属性来改变的，
         *              而是通过指定属性所对应的set方法来改变的.
         *              比如下面指定改变scaleX的属性值，ObjectAnimator在做动画时就会到指定控件(Layout)
         *              中去找对应的setScaleX()方法来改变控件中对应的值
         *              在View中已经实现了有关alpha(透明度),
         *              rotaion(Z轴旋转),rotationX(X轴旋转),rotationY(Y轴旋转),
         *              translationX,translationY(平移)，scaleX,scaleY(缩放)相关的set方法.
         *              所以可以在构造ObjectAnimator时直接使用.
         * values：属性所对应的参数，同样是可变长参数，可以指定多个,就是指这个属性值是从哪变到哪,
         *        像下面的例子意思是说改变scaleX的属性值从1f到0.9f,如果是三个就是从哪到哪再到哪，
         *        如果只指定了一个,那么ObjectAnimator会通过查找getProperty()方法来获得初始值.
         *
         * ofPropertyValuesHolder()参数：
         * target：是指需要执行动画的控件,传入this就是指当前这个自定义的Layout执行此动画
         * values：是一个可变长参数，可以传进去多个PropertyValuesHolder实例，
         *        由于每个PropertyValuesHolder实例都会针对一个属性做动画，
         *        所以如果传进去多个PropertyValuesHolder实例，将会对控件的多个属性同时做动画操作。
         *
         * setInterpolator()：插值器,控制动画的速度
         * android:interpolator="@android:anim/accelerate_interpolator"
         * animation.setInterpolator(new AccelerateInterpolator());
         * 设置动画为加速动画(动画播放中越来越快)
         * android:interpolator="@android:anim/decelerate_interpolator"
         * animation.setInterpolator(new DecelerateInterpolator());
         * 设置动画为减速动画(动画播放中越来越慢)
         * android:interpolator="@android:anim/accelerate_decelerate_interpolator"
         * animation.setInterpolator(new AccelerateDecelerateInterpolator());
         * 设置动画为先加速在减速(开始速度最快 逐渐减慢)
         * android:interpolator="@android:anim/anticipate_interpolator"
         * animation.setInterpolator(new AnticipateInterpolator());
         * 先反向执行一段，然后再加速反向回来（相当于我们弹簧，先反向压缩一小段，然后在加速弹出）
         * android:interpolator="@android:anim/anticipate_overshoot_interpolator"
         * animation.setInterpolator(new AnticipateOvershootInterpolator());
         * 同上先反向一段，然后加速反向回来，执行完毕自带回弹效果（更形象的弹簧效果）
         * android:interpolator="@android:anim/bounce_interpolator"
         * animation.setInterpolator(new BounceInterpolator());
         * 执行完毕之后会回弹跳跃几段（相当于我们高空掉下一颗皮球，到地面是会跳动几下）
         * android:interpolator="@android:anim/cycle_interpolator"
         * animation.setInterpolator(new CycleInterpolator(2));
         * 循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2* mCycles* Math.PI* input)
         * android:interpolator="@android:anim/linear_interpolator"
         * animation.setInterpolator(new LinearInterpolator());
         * 线性均匀改变
         * android:interpolator="@android:anim/overshoot_interpolator"
         * animation.setInterpolator(new OvershootInterpolator());
         * 加速执行，结束之后回弹
         * 默认Interpolator是匀速改变
         *
         * setEvaluator()：根据数字进度计算当前值,插值器返回数字进度
         * 计算Evaluator表达式：(位置 = 开始值 + (结束值 - 开始值) * 数字进度)
         * Evaluator是根据插值器返回的进度转换成当前位置所对应的值
         * 还要注意一点：只有在动画数值类型一样时,所对应的Evaluator才能通用.
         * 所以ofInt()对应的Evaluator类名叫IntEvaluator,而ofFloat()对应的Evaluator类名叫FloatEvaluator
         */
        PropertyValuesHolder valueHolder_1 = PropertyValuesHolder.ofFloat(
                "scaleX", 1f, 0.9f);
        PropertyValuesHolder valuesHolder_2 = PropertyValuesHolder.ofFloat(
                "scaleY", 1f, 0.9f);
        downAnim = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_1, valuesHolder_2);
        downAnim.setDuration(200);
        downAnim.setInterpolator(new LinearInterpolator());
        downAnim.setEvaluator(new FloatEvaluator());

        PropertyValuesHolder valueHolder_3 = PropertyValuesHolder.ofFloat(
                "scaleX", 0.9f, 1f);
        PropertyValuesHolder valuesHolder_4 = PropertyValuesHolder.ofFloat(
                "scaleY", 0.9f, 1f);
        upAnim = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_3,
                valuesHolder_4);
        upAnim.setDuration(200);
        upAnim.setInterpolator(new LinearInterpolator());
        upAnim.setEvaluator(new FloatEvaluator());
    }

    public void setClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downAnim.start();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                upAnim.start();
                if (listener != null) {
                    listener.onClick();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public interface ClickListener {
        void onClick();
    }
}
