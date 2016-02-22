#自定义View

`Android`里面定义的view类都继承自`View`。自定义的`view`也可以直接继承`View`，或者你可以通过继承既有的一个子类(例如`Button`或`ImageView`)来节约一点时间。自定义的view应该具备:

- 遵守`Android`标准规则
- 提供自定义的风格属性值并能够被`Android XML Layout`所识别
- 发出可访问的事件
- 能够兼容`Android`的不同平台

###自定义View的绘制

自定义`View`首先是要自定义他的外观，我们可以根据自己的要求来绘制。`Android`的view绘制流程是从`ViewRootImpl`的`performTraversals()`开始的，然后依次执行以下三个主要函数：

- `onMeasure()`:用于测量视图的大小的，详细见[这里](http://blog.csdn.net/ljx19900116/article/details/45311787)
- `onLayout()`:用于给视图进行布局，如在`ViewGroup.onLayout()`中调用`layout`对子视图进行布局
- `onDraw()`:对视图进行绘制

1、自定义`view`最重要的就是绘制，而这是通过重写`onDraw()`方法来达到目的的。我们绘制时需要一张画布`canvas`和一根画笔`paint`，画布`canvas`使用的是`onDraw`的参数，而画笔可以自己创建，一般在构造方法里面创建，因为在`onDraw`方法里面创建绘制对象会严重影响到性能并使得你的UI显得卡顿。

例如创建画笔：

```java

	paint = new Paint(Paint.ANTI_ALIAS_FLAG);//反走样
	paint.setAntiAlias(true);//抗锯齿

```

使用画笔绘制：

```java

	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, 0, 0, paint);//画位图
        canvas.drawBitmap(slideButton, slide_position, 0, paint);
    }

```

2、如果你想更加精确的控制你的view的大小，需要重写`onMeasure()`方法。这个方法的参数是`View.MeasureSpec`，它会告诉你的`view`的父控件的大小。那些值被包装成int类型（32位前两位是模式，后面30位是大小），你可以使用静态方法来获取其中的信息。

```java

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(background.getWidth(), background.getHeight());
    }

```

3、为了正确的绘制你的`view`，你需要知道`view`的大小。复杂的自定义`view`通常需要根据在屏幕上的大小与形状执行多次`layout`计算。而不是假设这个`view`在屏幕上的显示大小。即使只有一个程序会使用你的`view`，仍然是需要处理屏幕大小不同，密度不同，方向不同所带来的影响。

例如我们对所以子View进行横向排列，每一个占据一个手机屏幕大小，代码可以如下：

```java

for (int i = 0; i < getChildCount(); i++) {
	View view = getChildAt(i);
	view.layout(i * getWidth(), 0, getWidth() * (i + 1), getHeight());
}

```

###定义自定义属性

自定义view可以通过XML添加和改变样式，满足如下：

- 为你的view在资源标签下定义自设的属性
- 在你的XML layout中指定属性值
- 在运行时获取属性值
- 把获取到的属性值应用在你的view上

我们可以再在资源文件中建立`res/values/attrs.xml`文件，我们可以有下面的一个小小的实例：

```xml

<resources>
   <declare-styleable name="ToggleButton">
       <attr name="buttonState" format="boolean" />
       <attr name="position" format="enum">
           <enum name="left" value="0"/>
           <enum name="right" value="1"/>
       </attr>
   </declare-styleable>
</resources>

```

这个时候我们就自定义了两个属性`buttonState`（`boolean`值）和`Position`（枚举值），均属于`ToggleButton`项目下的`styleable`实例。

这样的话我们就可以像内置属性一样的来使用这两个属性了，不过我们要指明命名空间为：`xmlns:custom="http://schemas.android.com/apk/res-auto"`，接着就可以使用了：

```xml

<cn.zhouchaoyuan.customview.toggle.ToggleButton
        android:id="@+id/my_toggle_btn"
		custom:buttonState="true"
		custom:position="left"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

```

之后就是在代码中获取这两个值然后应用到我们的view上面：

```java

public ToggleButton(Context context, AttributeSet attrs) {
   super(context, attrs);
   TypedArray a = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.ToggleButton,
        0, 0);

   try {
       buttonState = a.getBoolean(R.styleable.ToggleButton_buttonState, false);
       position = a.getInteger(R.styleable.ToggleButton_position, 0);
   } finally {
       a.recycle();
   }
}

```

获取了这些属性之后便可以通过这两个值来设置了。
>当`view`的某些内容发生变化的时候，需要调用`invalidate()`来通知系统对这个`view`进行`redraw()`，当某些元素变化会引起组件大小变化时，需要调用`requestLayout()`方法。调用时若忘了这两个方法，将会导致`hard-to-find bugs`。

###使得View可交互

对于我们自定义的`view`，用户应该可以感受到UI上的微小变化，并对模仿现实世界的细微之处反应强烈，这个时候就要求我们要很好的处理输入的手势。在`Android`中最常用的输入事件是`touch`，它会触发`onTouchEvent(android.view.MotionEvent)`的回调。可以重写这个方法来处理touch事件：

```java

@Override
public boolean onTouchEvent(MotionEvent event){
	return super.onTouchEvent(event);
}

```

当然touch的功能非常有限，比如fling就无法处理，而Android有提供了[GestureDetector](developer.android.com/reference/android/view/GestureDetector.html),通过传入`GestureDetector.OnGestureListener`的一个实例构建一个`GestureDetector`。如果你只是想要处理几种`gestures`(手势操作)你可以继承`GestureDetector.SimpleOnGestureListener`，而不用实现`GestureDetector.OnGestureListener`接口。如：

```java

class GestureListener extends GestureDetector.SimpleOnGestureListener {
   @Override
   public boolean onDown(MotionEvent e) {
       return true;
   }
}
mDetector = new GestureDetector(getContext(), new GestureListener());

```

之后在`onTouchEvent`里面调用`GestureListener`处理手势即可，如下：

```java

@Override
public boolean onTouchEvent(MotionEvent event) {
   boolean result = mDetector.onTouchEvent(event);
   if (!result) {
       if (event.getAction() == MotionEvent.ACTION_UP) {
           //do something
           result = true;
       }
   }
   return result;
}

```
###自定义View的优化

- 如在onDraw里面尽量避免不必要的操作，也就是说被频繁调用的方法要以尽量少的步骤完成要做的事情
- 使用硬件加速，详细见[这里](http://hukai.me/android-training-course-in-chinese/ui/custom-view/optimize-view.html)

###总的一个例子

效果图如下：

![toggleButton](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W6/toggleButton.gif)

自定义view的代码：

```java

package cn.zhouchaoyuan.customview.toggle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.zhouchaoyuan.customview.R;

/**
 * Created by chaoyuan on 2016/2/18.
 * 滑动开关类
 */
public class ToggleButton extends View{

    /**
     * 开关的背景
     */
    private Bitmap background;
    /**
     * 滑动键
     */
    private Bitmap slideButton;
    /**
     * 滑动键的位置
     */
    private int slide_position = 0;
    /**
     * 滑动时上一次横坐标值
     */
    private int lastX;
    /**
     * 滑动键左边界的最大范围
     */
    private int MAX_WIDTH;
    /**
     * 判断是否是滑动事件的布尔变量
     */
    private boolean isDrag = false;
    /**
     * 判断开关是否开启的布尔变量
     */
    private boolean buttonState = false;

    public ToggleButton(Context context) {
        super(context);
        init();
    }
    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ToggleButton,
                0, 0);

        try {
            buttonState = a.getBoolean(R.styleable.ToggleButton_buttonState, false);
        } finally {
            a.recycle();
        }
        init();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(background.getWidth(), background.getHeight());
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//反走样
        paint.setAntiAlias(true);//抗锯齿
        canvas.drawBitmap(background, 0, 0, paint);//画位图
        canvas.drawBitmap(slideButton, slide_position, 0, paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("onTouchEvent","ACTION_DOWN" + event.getX());
                isDrag = false;
                lastX = (int)event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("onTouchEvent","ACTION_MOVE" + event.getX());
                if(Math.abs((int)event.getX() - lastX) > 4){//大于4个像素视为拖拽
                    isDrag = true;
                }
                int disX = (int)event.getX() - lastX;
                lastX = (int)event.getX();
                slide_position += disX;
                if(slide_position < 0){
                    slide_position = 0;
                }
                else if(slide_position > MAX_WIDTH){
                    slide_position = MAX_WIDTH;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("onTouchEvent","ACTION_UP" + event.getX());
                if(slide_position < MAX_WIDTH / 2){
                    buttonState = false;
                }
                else {
                    buttonState = true;
                }
                onButtonStateChange();
                break;
        }
        invalidate();
        // 返回true意味着消费掉本次事件，不让其他控件还可以接收到这个事件
        return true;
    }
    private void init() {
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        slideButton = BitmapFactory.decodeResource(getResources(), R.drawable.slidebutton);
        MAX_WIDTH =  background.getWidth() - slideButton.getWidth();
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!isDrag) {
                    buttonState = !buttonState;
                    onButtonStateChange();
                    invalidate();
                }
            }
        });
        onButtonStateChange();
    }

    /**
     * 按钮的状态改变，当按键状态改变调用此函数设置滑键的位置
     */
    protected  void onButtonStateChange(){
        if(buttonState){
            slide_position = background.getWidth() - slideButton.getWidth();
        }
        else{
            slide_position = 0;
        }
    }
}


```

布局文件如下：

```xml

<RelativeLayout  xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:custom="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <cn.zhouchaoyuan.customview.toggle.ToggleButton
        android:id="@+id/my_toggle_btn"
        custom:buttonState="true"
        android:layout_width="wrap_content"
        android:layout_centerInParent="true"
        android:layout_height="wrap_content" />
</RelativeLayout>

```

主`Activity`如下：

```java

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();

        setContentView(R.layout.main);
    }
}

```

自定义属性文件如下：

```xml

<resources>
   <declare-styleable name="ToggleButton">
       <attr name="buttonState" format="boolean" />
       <attr name="position" format="enum">
           <enum name="left" value="0"/>
           <enum name="right" value="1"/>
       </attr>
   </declare-styleable>
</resources>

```

===========================================================
> 以上小结均出自以下三个链接，真的非常详细，就贯彻了“拿来主义”吧：

[google官方教程中文版](http://hukai.me/android-training-course-in-chinese/ui/custom-view/create-view.html)

[郭神的对view的绘制解析](http://blog.csdn.net/guolin_blog/article/details/12921889)

[第四维空间](http://blog.csdn.net/ljx19900116/article/details/45311611)