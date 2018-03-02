## PressedAnimation ##

一款仿携程首页按下动画的自定义控件，使用ObjectAnimator以及PropertyValuesHolder实现，那什么是ObjectAnimator，什么又是PropertyValuesHolder呢，
他们怎么使用呢？先上一张图看看效果

![image](https://github.com/AndyRenJie/PressedAnimation/blob/master/images/20180302.gif)

#### 一、ObjectAnimator ####

ObjectAnimator，继承自ValueAnimator，所以ValueAnimator所能使用的方法，ObjectAnimator也都可以使用，ObjectAnimator同时也重写了几个方法，
比如：ofFloat()、ofInt()等。

``
public static ObjectAnimator ofFloat(Object target, String propertyName, float... values) 
``

ObjectAnimator内部还提供了一个ofPropertyValuesHolder()方法，使用PropertyValuesHolder来构造动画。那PropertyValuesHolder又是什么呢？

``
public static ObjectAnimator ofPropertyValuesHolder(Object target, PropertyValuesHolder... values)
``

#### 二、PropertyValuesHolder ####

PropertyValuesHolder这个类的意义就是，它其中保存了动画过程中所需要操作的属性和对应的值。它也有ofFloat()、ofInt()等方法，
通过ofFloat(Object target, String propertyName, float… values)构造的动画，

``
public static PropertyValuesHolder ofFloat(String propertyName, float... values)
``

ofFloat()的内部实现其实就是将传进来的参数封装成PropertyValuesHolder实例来保存动画状态。在封装成PropertyValuesHolder实例以后，
后期的各种操作也是以PropertyValuesHolder为主的。

#### 三、ofFloat()、ofInt()参数 ####

propertyName：表示ObjectAnimator需要操作的属性名。也就是ObjectAnimator需要通过反射查找对应属性的setProperty()函数的那个property。
ObjectAnimator做动画，并不是根据控件xml中的属性来改变的，而是通过指定属性所对应的set方法来改变的。比如下面这个例子指定改变scaleX()的属性值，

```
PropertyValuesHolder valueHolder = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.9f);  
```

ObjectAnimator在做动画时就会到指定控件(TextView，ImageView，Layout等) 中去找对应的setScaleX()方法来改变控件中对应的值。
而在View中已经实现了有关alpha(透明度)，rotaion(围绕Z轴旋转)，rotationX(围绕X轴旋转)，rotationY(围绕Y轴旋转)，
translationX(X轴平移)，translationY(Y轴平移)，scaleX(X轴缩放)，scaleY(Y轴缩放)相关的set方法。所以可以在构造ObjectAnimator时可以直接使用。

values：属性所对应的参数，同样是可变长参数，可以指定多个，就是指这个属性值是从哪变到哪，像下面的例子意思是说改变scaleX的属性值从1f到0.9f，
如果是三个就是从哪到哪再到哪，

```
ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,"scaleX",1f,0.9f,1f);  
objectAnimator.setDuration(2000);  
objectAnimator.start();  
```

如果只指定了一个，那么ObjectAnimator会通过查找getProperty()方法来获得初始值。

#### 四、ofPropertyValuesHolder()参数 ####

target：是指需要执行动画的控件，传入this就是指当前这个自定义的View执行此动画。

values：是一个可变长参数，可以传进去多个PropertyValuesHolder实例，

```
ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_1, valuesHolder_2);  
```

由于每个PropertyValuesHolder实例都会针对一个属性做动画，所以如果传进去多个PropertyValuesHolder实例，将会对控件的多个属性同时做动画操作。

```
PropertyValuesHolder valueHolder_1 = PropertyValuesHolder.ofFloat(  
                "scaleX", 1f, 0.9f);  
        PropertyValuesHolder valuesHolder_2 = PropertyValuesHolder.ofFloat(  
                "scaleY", 1f, 0.9f);  
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_1, valuesHolder_2);  
        objectAnimator.setDuration(200);  
        objectAnimator.setInterpolator(new LinearInterpolator());  
        downAnim.setEvaluator(new FloatEvaluator());  
```

#### 五、setInterpolator()：插值器,控制动画的速度 ####

android:interpolator="@android:anim/accelerate_interpolator"

animation.setInterpolator(new AccelerateInterpolator());

设置动画为加速动画(动画播放中越来越快)

android:interpolator="@android:anim/decelerate_interpolator"

animation.setInterpolator(new DecelerateInterpolator());

设置动画为减速动画(动画播放中越来越慢)

android:interpolator="@android:anim/accelerate_decelerate_interpolator"

animation.setInterpolator(new AccelerateDecelerateInterpolator());

设置动画为先加速在减速(开始速度最快 逐渐减慢)

android:interpolator="@android:anim/anticipate_interpolator"

animation.setInterpolator(new AnticipateInterpolator());

先反向执行一段，然后再加速反向回来（相当于我们弹簧，先反向压缩一小段，然后在加速弹出）

android:interpolator="@android:anim/anticipate_overshoot_interpolator"

animation.setInterpolator(new AnticipateOvershootInterpolator());

同上先反向一段，然后加速反向回来，执行完毕自带回弹效果（更形象的弹簧效果）

android:interpolator="@android:anim/bounce_interpolator"

animation.setInterpolator(new BounceInterpolator());

执行完毕之后会回弹跳跃几段（相当于我们高空掉下一颗皮球，到地面是会跳动几下）

android:interpolator="@android:anim/cycle_interpolator"

animation.setInterpolator(new CycleInterpolator(2));

循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2* mCycles* Math.PI* input)

android:interpolator="@android:anim/linear_interpolator"

animation.setInterpolator(new LinearInterpolator());

线性均匀改变

android:interpolator="@android:anim/overshoot_interpolator"

animation.setInterpolator(new OvershootInterpolator());

加速执行，结束之后回弹

默认Interpolator是匀速改变
