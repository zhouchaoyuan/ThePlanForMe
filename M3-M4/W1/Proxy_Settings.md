# Proxy Settings
---
>参考自[Android 官网](https://developer.android.com/tools/studio/studio-config.html#proxy)，内容大部分都是蹩脚的翻(chao)译(xi)，少部分增删

Proxy也称网络代理(代理服务器)，可在HTTP客户端和Web服务器建立绕过防火墙的快速的安全的连接。

### 一、在Android Studio中使用代理

- 选择`File > Settings > Appearance & Behavior -- System Settings -- HTTP Proxy`
- 在`Android Studio`,打开IDE设置对话框
	- 在`Windows`和`Linux`下,选择`File > Settings > IDE Setting -- HTTP Proxy.`
	- 在`Mac`下, 选择`Android Studio > Preferences > IDE Setting -- HTTP Proxy.`
	
	HTTP Proxy 页面就会出现![HTTP Proxy](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W1/HTTP_Proxy.png)
- 通过勾选`Auto-detect proxy settings`或`Manual proxy configuration`来自动监听代理，这些设置可以在[HTTP Proxy](https://www.jetbrains.com/idea/help/http-proxy.html)找到更详细的解释。我勾选的是`Manual proxy configuration`使用的[这里](http://www.androiddevtools.cn/)的代理服务器。
- 点击`Apply`即可完成设置

### 二、SDK Manager HTTP代理设置

`SDK Manager`有一个独立的 `HTTP Proxy` 设置页面，为了设置代理，打开`SDK Manager`然后打开`SDK Manger Page`页面,具体操作如下：

- 在`Windows`下, 在下拉菜单选择`Tools > Options`
- 在`Mac`和`Linux`, 在系统菜单选择`Tools > Options`

填写`HTTP Proxy Serve`和`HTTP Proxy Port`，填写内容可从[这里](http://www.androiddevtools.cn/)获取。

### 三、Gradle HTTP代理设置

见[官网](https://developer.android.com/tools/studio/studio-config.html#proxy)，不是很懂，学习gradle的时候看吧。

### 四、在Eclipse中使用代理

暂缺→_→