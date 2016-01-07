#Eclipse开发环境搭建

在还没有`Android Studio`的时候，`Android`工程师都使用`Eclipse`安装`ADT(Android Development Toolkit)`插件来开发`Android`应用的，这也是谷歌官方推荐的。
> 不过目前`Android`官网已经没有用Eclipse开发的环境搭建教程了，在[http://developer.android.com/tools/sdk/eclipse-adt.html](http://developer.android.com/tools/sdk/eclipse-adt.html)还有ADT的下载，但是在[Installing the Eclipse Plugin](http://developer.android.com/tools/help/adt.html)已经建议说“You should migrate your app development projects to Android Studio as soon as possible（你必须尽快把你的app开发工程迁移到`Android Studio`上）”，也就是说`Eclipse`已经被抛弃了。

###Eclipse的搭建方法如下：

- 首先先下载JDK([Java SE Development Kit 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html))，下载完JDK之后配置系统环境变量`JAVA_HOME`，操作步骤为“系统高级设置->高级->环境变量”，然后添加系统变量，名字为`JAVA_HOME`，变量值为`C:\Program Files\Java\jdk1.8.0_45`（假设JDK为此安装路径）。
- 安装[Eclipse](http://www.eclipse.org/downloads/)，一般情况下我们都已经安装了Eclipse，所以使用此法。
- 打开`Eclipse`并选择菜单栏`Help->Install New Software`，一般出现下图：![Install New Software](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/Install_New_Software.png)
- 点击"添加可获得软件按钮"
- 在Name编辑框输入`Android Development Tools`,在Location编辑框输入`https://dl-ssl.google.com/android/eclipse/`，如下图：![Add Repository](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/Add_Repository.png)
- 点击OK之后会pending一段时间，过一会就出现下图：![Install](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/Install.png)
- 勾选要安装的插件(目前（2016/1/7）的最新版).一般要包括`ADT，DDMS 和 Android’s debugging tool`,点击finish，等待下载安装完毕，然后重启Eclipse就有如下的工具栏：</br></br>![menu](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/menu.png)</br></br>
- 在Eclipse添加SKD包
	- 点击菜单栏的![](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/ico.jpg)启动`SDK Manager`
	- 获得最新的`SDK tools`，勾选以下选项
		- Android SDK Tools(必选，最新的Android软件开发工具)
		- Android SDK Platform-tools(必选，最新的Android稳定版软件开发工具)
		- Android SDK Build-tools (建议最高版本)
		- SDK Platform(必选，开发环境中至少要一个SDK Platform你才能编译app)
		- ARM EABI v7a System Image(建议勾选，模拟器的系统，选择合适的版本就行)
		- Android Support Repository(建议勾选，包含支持Library的专用库)
		- Android Support Library(建议勾选，可以让你使用最新的Android APIs)
		- Google Repository(建议勾选，包含支持Library的专用库)
		- Google Play services(建议勾选，包含谷歌服务和客户端库以及示例代码)
	
	点击`Install X packages`，即可完成安装，若是安装受阻,说明被墙，可以通过设置代理服务器(见[Proxy Settings]())安装。其他可以勾选的选项包可自行勾选，每项的详细用途参考[这里](https://developer.android.com/tools/help/sdk-manager.html)
- 经过上述的步骤，你现在就可以再`Eclipse`上构建app了。
