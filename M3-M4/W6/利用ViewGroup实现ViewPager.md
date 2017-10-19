# 利用ViewGroup实现ViewPager

### 简单几项功能

- 手指触屏缓慢滑动超过一半会切页
- 手指触屏缓慢滑动`ViewPager`也随着滑动
- 手指按下快速滑动的有动画效果向左或者向右切页
- 滑到第一页不能向右滑动，最后一页不能向左滑动
- 能实现切页回调方法处理所需`Tab`
- 简单的`Adapter`提供数据

### 效果图

![效果图](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W6/ViewPager.gif)

### 简单原理

- 大概就是将子`View`横向整齐排列，每个子`View`的宽度和屏幕宽度一样，即第二个子`View`左上角坐标是`(x,0)`，第三个子`View`左上角坐标是`(2x,0)`等，然后通过`scrollBy(int x, int y)`和`scrollTo(int x, int y)`对`ViewGroup`进行操作来实现。简单来说`scrollBy`是相对偏移而`scrollTo`是绝对偏移，详细看郭神[Demo](http://blog.csdn.net/guolin_blog/article/details/48719871)
- 一般自定义View会重写以下函数
	- `onMeasure`
	- `onLayout`
	- `onDraw`
	- `onSizeChanged`
	- `onTouchEvent`
- 之后的代码只重写了`onLayout`,`onTouchEvent`,另外还重写了`computeScroll`来实现平滑滑动效果。

在代码中有详细的的注释，所以直接贴代码吧，如下：


```java

package cn.zhouchaoyuan.customview.pager;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chaoyuan on 2016/2/19.
 * 通过实现{@link MyPagerAdapter}向该类提供数据
 */
public class MyViewPager extends ViewGroup {

    /**
     * 当前页面的下标，从0开始
     */
    private int mCurItem;
    /**
     * 按下屏幕的X轴的坐标
     */
    private int firstX;
    /**
     * 手势处理探测器两边
     */
    private GestureDetector mDetector;
    /**
     * 动画辅助类变量，用Scroller代替更佳
     */
    private ScrollDistance mScrollDistance;
    /**
     * 简单控制页面变换回调的变量
     */
    private OnPageChangeListener onPageChangeListener = null;
    /**
     * 适配器变量
     */
    private MyPagerAdapter adapter = null;

    /**
     * 接口以实现回调，可用于实现滑动指示条的功能
     */
    interface OnPageChangeListener {
        /**
         * 页面变换是回调这个方法
         *
         * @param position 处于屏幕的Fragment的标号，从0开始
         */
        void onPageSelected(int position);
    }

    /**
     * 接口，简单模拟实现适配器，用于提供数据，针对View
     */
    interface MyPagerAdapter {
        /**
         * 获得子view的个数
         *
         * @return 返回一个int数据，代表子view的个数
         */
        int getCount();

        /**
         * 根据position返回一个View
         *
         * @param position 眼返回的View的位置
         * @return 根据位置返回的View
         */
        View getItem(int position);
    }

    public MyViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewPager();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void setAdapter(MyPagerAdapter adapter) {
        this.adapter = adapter;
        onAdapterChange();
    }

    private void onAdapterChange() {
        int count = adapter.getCount();

        for (int i = 0; i < count; i++) {
            addView(adapter.getItem(i));
        }
        //当某些元素变化会引起组件大小变化时，需要调用requestLayout方法
        //requestLayout();
    }

    /**
     * 处理一些初始化工作，无论从哪个构造函数进入都调用此函数进行初始化
     */
    private void initViewPager() {
        mCurItem = 0;

        //创建手势探测器来处理触屏事件
        mDetector = new GestureDetector(getContext(), new GestureListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //对子view进行排列，调用子view的layout方法，四个参数分别对应 左、上、右、下的坐标
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(i * getWidth(), 0, getWidth() * (i + 1), getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void computeScroll() {
        //调用invalidate()会回调此方法，在这里实现动画效果
        if (mScrollDistance != null && !mScrollDistance.computeScrollOffset()) {
            int newX = (int) mScrollDistance.currentX;
            Log.e("acjiji", "computeScroll" + newX);
            scrollTo(newX, 0);
            // 再次刷新
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 使用工具来解析触摸事件
        boolean result = mDetector.onTouchEvent(event);
        //事件被处理就直接返回
        if (result) return result;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int position;
                int secondX = (int) event.getX();
                if (secondX - firstX > getWidth() / 2) {
                    position = mCurItem - 1;
                } else if (firstX - secondX > getWidth() / 2) {
                    position = mCurItem + 1;
                } else {
                    position = mCurItem;
                }
                position = position < 0 ? 0 :
                        (position >= adapter.getCount() ? adapter.getCount() - 1 : position);
                if (position != mCurItem) {
                    mCurItem = position;
                    //页面变换，在设置变换监听的时候回调该函数
                    if (onPageChangeListener != null) {
                        onPageChangeListener.onPageSelected(mCurItem);
                    }
                }
                mScrollDistance = new ScrollDistance(getScrollX(),
                        mCurItem * getWidth() - getScrollX());
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                firstX = (int) event.getX();
                break;
        }
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        /*
        * 必须总是实现onDown()方法，并返回true。这一步是必须的，因为所有的gestures都是从
        * onDown()开始的。如果你在onDown()里面返回false，系统会认为你想要忽略后续的gesture,
        * 那么GestureDetector.OnGestureListener的其他回调方法就不会被执行到了
        * */
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /*
        * onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        * 在屏幕上拖动事件。无论是用手拖动view，或者是以抛的动作滚动，都会多次触发,这个方法
        * 在ACTION_MOVE动作发生时就会触发
        *
        * e1表示最开始触发本次这一系列Event事件的那个ACTION_DOWN事件，
        * e2表示触发本次Event事件的那个ACTION_MOVE事件，
        * distanceX、distanceY分别表示从上一次调用onScroll调用到这一次onScroll调用在
        * x和y方向上滑动的距离。这里需要稍微留意的是，distanceX、distanceY的正负并不是像之前
        * ViewGroup里坐标显示的正负那样，而是向左滑动值distanceX为正，向右滑动值为distanceX负
        * 滑屏：手指触动屏幕后，稍微滑动后立即松开
        *   onDown----->onScroll---->onScroll---->onScroll---->………----->onFling
        * */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            super.onScroll(e1, e2, distanceX, distanceY);
            int x = getScrollX();
            //第一页不能向右滑动，最后一页不能向左滑动，直接返回
            if (x < 0 || x > getWidth() * (adapter.getCount() - 1))
                return false;
            scrollBy((int) distanceX, 0);

            //返回false可以继续处理后续交互，比如滑动超过一半距离
            return false;
        }

        /*
         * onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) ：滑屏，用户按下
         * 触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
         * 参数解释：
         * e1：第1个ACTION_DOWN MotionEvent
         * e2：最后一个ACTION_MOVE MotionEvent
         * velocityX：X轴上的移动速度，像素/秒
         * velocityY：Y轴上的移动速度，像素/秒
         * */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int position;
            if (e1.getX() < e2.getX()) {
                position = mCurItem - 1;
            } else {
                position = mCurItem + 1;
            }
            position = position < 0 ? 0 :
                    (position >= adapter.getCount() ? adapter.getCount() - 1 : position);
            if (position != mCurItem) {
                mCurItem = position;
                //页面变换，在设置变换监听的时候回调该函数
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(mCurItem);
                }
            }
            mScrollDistance = new ScrollDistance(getScrollX(),
                    mCurItem * getWidth() - getScrollX());
            invalidate();
            return true;
        }
    }

    /**
     * 内部类，用来辅助处理滑过一半距离或者快速或者的动画效果
     * */
    class ScrollDistance {
        public int startX;
        public int distanceX;
        public long startTime;
        public long currentX;
        public long duration = 250;
        public boolean isFinish = false;

        public ScrollDistance(int startX, int distanceX) {
            this.startX = startX;
            this.isFinish = false;
            this.duration = 250;
            this.startTime = SystemClock.uptimeMillis();
            this.distanceX = distanceX;
        }

        /**
         * 计算一下当前的运行状态
         *
         * @return true：表示运行结束; false：表示还在运行
         */
        public boolean computeScrollOffset() {
            if (isFinish) {
                return isFinish;
            }
            // 计算一下滑动运行了多久时间
            long passTime = SystemClock.uptimeMillis() - startTime;

            if (passTime < duration) {
                currentX = startX + distanceX * passTime / duration;
            } else {
                currentX = startX + distanceX;
                isFinish = true;
            }
            return false;
        }
    }
}

```

上面的自定义View写完了，接下来就是使用了，新建一个Activity，其xml文件`activity_pager_demo.xml`如下：

```xml

<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="cn.zhouchaoyuan.customview.pager.PagerDemo">

    <cn.zhouchaoyuan.customview.pager.MyViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></cn.zhouchaoyuan.customview.pager.MyViewPager>

</FrameLayout>

```

`java`文件`PagerDemo.java`如下:

```java

package cn.zhouchaoyuan.customview.pager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.zhouchaoyuan.customview.R;

public class PagerDemo extends AppCompatActivity {

    private final int COUNT = 4;
    private MyViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_demo);
        pager = (MyViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter());
        pager.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(PagerDemo.this, "Step " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyPagerAdapter implements MyViewPager.MyPagerAdapter {

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public View getItem(int position) {
            TextView content = new TextView(PagerDemo.this);
            content.setText(titles[position] + "\n\n" + contents[position]);
            content.setTextSize(18);
            return content;
        }
    }

	//繁琐的测试数据
    private final static String[] titles = {"一、获取安装文件", "二、安装IDE", "三、建立AVD", "四、自动更新SDK"};

    private final static String[] contents = {"1、首先进入Android官网并下载安装文件 https://developer.android.com/sdk/index.html\n" +
            "\n" +
            "下载完毕即得到一个exe文件。",

            //split

            "1、安装Android Studio之前必须确保安装了JDK，对于Android 5.0以上版本必须保证JDK7版本以上。 安装JDK(Java SE Development Kit 7)，下载完JDK之后配置系统环境变量JAVA_HOME，操作步骤为“系统高级设置->高级->环境变量”，然后添加系统变量，名字为JAVA_HOME，变量值为C:\\Program Files\\Java\\jdk1.8.0_45（假设JDK为此安装路径）。\n\n" +
                    "2、双击之前下载的exe文件，启动之后他将检索JAVA_HOME变量并在我们连续点击下一步的情况下自动安装Android Studio。\n\n" +
                    "3、在已经安装了的Android Studio添加SKD包，分几小步：" + "\n" + "\t点击 Tools > Android > SDK Manager打开如下界面 " + "\t获得最新的SDK tools，勾选以下选项\n" +
                    "a、Android SDK Tools(必选，最新的Android软件开发工具)\n" +
                    "b、Android SDK Platform-tools(必选，最新的Android稳定版软件开发工具)\n" +
                    "c、Android SDK Build-tools (建议最高版本)\n" +
                    "d、SDK Platform(必选，开发环境中至少要一个SDK Platform你才能编译app)\n" +
                    "e、ARM EABI v7a System Image(建议勾选，模拟器的系统，选择合适的版本就行)\n" +
                    "f、Android Support Repository(建议勾选，包含支持Library的专用库)\n" +
                    "g、Android Support Library(建议勾选，可以让你使用最新的Android APIs)\n" +
                    "h、Google Repository(建议勾选，包含支持Library的专用库)\n" +
                    "i、Google Play services(建议勾选，包含谷歌服务和客户端库以及示例代码)\n\n" +
                    "点击Install X packages，即可完成安装，若是安装受阻,说明被墙，可以通过设置代理服务器(见Proxy Settings)安装。其他可以勾选的选项包可自行勾选，每项的详细用途参考这里\n" +
                    "经过上述的步骤，你现在就可以在Android Studio上构建app了。",

            //split

            "1、启动 Android Virtual Device Manager（AVD Manager）的两种方式：\n" +
                    "\n" +
                    "a、用Android Studio, Tools > Android > AVD Manager,或者点击工具栏里面Android Virtual Device Manager\n" +
                    "b、在命令行窗口中，把当前目录切换到<sdk>/tools/ 后执行\n\n" + "2、点击Create Virtual Device.\n\n" + "3、在Select Hardware窗口，选择一个设备，比如 Nexus 6，点击Next。\n" +
                    "\n" +
                    "4、选择列出的合适系统镜像.\n" +
                    "\n" +
                    "5、校验模拟器配置，点击Finish",

            //split

            "对于安装了的SDK，Android官方往往更新很快，我们可以设置自动检查更新，以获得最新的SDK，自动检查更新SDK根据以下步骤：\n" +
                    "\n" +
                    "1、选择File > Settings > Appearance & Behavior > System Settings > Updates.\n\n" +
                    "2、勾选Android SDK自动检测更新并选择更新channel，一般选择stable channel。\n\n" +
                    "3、点击OK或者Apply就可以自动检查更新了\n"
	};
}

```

上面代码借鉴了几个例子，后来发现郭老师有一个实现挺简单的，[记下](http://blog.csdn.net/guolin_blog/article/details/48719871)。