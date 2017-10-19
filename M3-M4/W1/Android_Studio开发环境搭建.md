# Windows开发环境Android Studio的搭建

>安装之前，要科学上网→_→，win下可使用[lantern](https://github.com/getlantern/lantern)，速度快，还是免费的，[lantern官网](http://getlantern.org/)，目测这个只能通过浏览器翻墙，后面可能需要设置代理。

### 一、获取安装文件

首先进入Android官网并下载安装文件
[https://developer.android.com/sdk/index.html](https://developer.android.com/sdk/index.html)

下载完毕即得到一个exe文件。

### 二、安装IDE

- 1、安装`Android Studio`之前必须确保安装了JDK，对于`Android 5.0`以上版本必须保证`JDK7`版本以上。
安装JDK([Java SE Development Kit 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html))，下载完JDK之后配置系统环境变量`JAVA_HOME`，操作步骤为“系统高级设置->高级->环境变量”，然后添加系统变量，名字为`JAVA_HOME`，变量值为`C:\Program Files\Java\jdk1.8.0_45`（假设JDK为此安装路径）。
- 2、双击之前下载的exe文件，启动之后他将检索`JAVA_HOME`变量并在我们连续点击下一步的情况下自动安装`Android Studio`。
- 3、在已经安装了的`Android Studio`添加SKD包，分几小步：
	- 点击菜单栏的![](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/ico.jpg)或者`Tools > Android > SDK Manager`打开如下界面 ![SDK Setting](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/SDK_Setting.png)然后通过`Lauch Standalone SDK Manager`单独打开`SDK Manager`，如下： ![SDK Manager](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/Android_SDK_Manager.png)
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
	
	点击`Install X packages`，即可完成安装，若是安装受阻,说明被墙，可以通过设置代理服务器(见[Proxy Settings](https://github.com/zhouchaoyuan/ThePlanForMe/blob/master/M3-M4/W1/Proxy_Settings.md))安装。其他可以勾选的选项包可自行勾选，每项的详细用途参考[这里](https://developer.android.com/tools/help/sdk-manager.html)
- 经过上述的步骤，你现在就可以在`Android Studio`上构建app了。

### 三、建立AVD（Android Virtual Device）

1. 启动 Android Virtual Device Manager（AVD Manager）的两种方式：
	* 用Android Studio, **Tools > Android > AVD Manager**,或者点击工具栏里面Android Virtual Device Manager![image](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/AVD.png)；
	* 在命令行窗口中，把当前目录切换到`<sdk>/tools/` 后执行：
2. 在AVD Manager 面板中，如下:![AVD Manager](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/AVD_Manager.png)点击Create Virtual Device.

3. 在Select Hardware窗口，选择一个设备，比如 Nexus 6，点击Next。

4. 选择列出的合适系统镜像.

5. 校验模拟器配置，点击Finish，出现下图所有的已经建立的设备：![AVD Manager device](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/AVD_Manager_device.png)

参考自[这里](https://developer.android.com/tools/devices/managing-avds.html)

### 四、自动更新SDK
对于安装了的`SDK，Android`官方往往更新很快，我们可以设置自动检查更新，以获得最新的SDK，自动检查更新SDK根据以下步骤（[见](https://developer.android.com/tools/help/sdk-manager.html)）：

- 选择`File > Settings > Appearance & Behavior > System Settings > Updates.`
- 勾选`Android SDK`自动检测更新并选择更新channel，一般选择`stable channel。`
- 点击`OK`或者`Apply`就可以自动检查更新了


### 有用的学习链接

- [Google Android 官网](https://developer.android.com/training/index.html)
- [Google Android官方教程中文版](http://hukai.me/android-training-course-in-chinese/index.html)
- [无需翻墙直接拥抱Android](http://www.androiddevtools.cn/)