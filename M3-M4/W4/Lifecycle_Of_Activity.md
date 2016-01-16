#Activity

`android`并不像其他编程范式一样从`main()`方法开始执行，它根据生命周期的不同阶段唤起对应的回调函数来执行代码。系统存在启动与销毁一个`activity`的一套有序的回调函数，一下图片展示了`activity`的生命周期：![activity](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W4/basic-lifecycle.png)，合理利用`activity`的回调函数可以实现以下一些非常重要的功能：

- 使用`app`的时候，不会因为有来电通话或者切换到其他`app`而导致程序`crash`
- 用户没有激活某个组件时不会消耗宝贵的系统资源
- 离开`app`并且一段时间后返回，不会丢失用户的使用进度
- 设备发生屏幕旋转时不会`crash`或者丢失用户的使用进度。

启动应用程序时，系统会调用在[AndroidManifest](https://github.com/zhouchaoyuan/ThePlanForMe/blob/master/M3-M4/W2/Manifest.md)中声明了带有`MAIN action`和`LAUNCHER category`标签的`<intent-filter>`的`activity`，如果没有有声明这样的`activity`主界面列表里面不会呈现app图标如下：

	<activity android:name=".MainActivity" android:label="@string/app_name">
	    <intent-filter>
	        <action android:name="android.intent.action.MAIN" />
	        <category android:name="android.intent.category.LAUNCHER" />
	    </intent-filter>
	</activity>

`activity`被启动时，`onCreate()`方法将被调用，我们可以重写`onCreate()`方法实现自己的初始化处理逻辑。

还是见[中文教学](http://hukai.me/android-training-course-in-chinese/basics/activity-lifecycle/starting.html)吧，详细。 