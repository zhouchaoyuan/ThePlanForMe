#Manifest

> 参考自[这里](http://developer.android.com/guide/topics/manifest/manifest-intro.html#filec)

每一个`Android`应用的根目录都有一个清单(`AndroidManifest.xml`文件，这个清单文件呈现给`Android`系统关于应用程序的一些基本信息,以及你在运行app之前必须拥有的系统信息，`AndroidManifest`的大致功能如下：

- 它为应用程序命名了包名，包名称是应用程序的唯一标识符 `package="..."`
- 它描述了应用程序的组成----`activities`（活动），`services`（服务），`broadcast` `broadcast receivers`（广播接收器），`content providers`（内容提供者）。它还表明了实现了上述四大组件的类（ 如：默认的工程有`<activity android:name=".MainActivity">`这么一句话，说明有一个`Activity`，而实现了这个`Activity`的类是当前目录下的`MainActivity.java`文件 ）以及他们的属性（如：处理的Intent（意图）消息）。这些声明让`Android`系统知道他们是什么组件，在什么条件下他们能够运行。
- 它决定哪些进程将执行应用程序的组件。
- 它表明应用程序需要什么权限来访问受保护的部件以及和其他应用程序交互。
- 它还表明了别人需要什么权限才能和应用程序的组件交互。
- 它列出了应用程序运行时 提供概要信息和其他信息的`Instrumentation`类。这些声明只有在应用程序处于开发和测试阶段才会存在清单中，他们在发布应用程序之前就被删除了。
- 它表明应用程序需要的最小的`Android API`版本
- 它列出该应用程序必须链接的库。

###Manifest的结构

下面展示了Manifest的基本结构和一些可以在Manifest出现的标签以及每个标签的属性

	<?xml version="1.0" encoding="utf-8"?>
	
	<manifest>
	
	    <uses-permission />
    	<permission />
    	<permission-tree />
    	<permission-group />
    	<instrumentation />
    	<uses-sdk />
	    <uses-configuration />  
    	<uses-feature />  
    	<supports-screens />  
	    <compatible-screens />  
	    <supports-gl-texture />  
	
    	<application>
	
    	    <activity>
    	        <intent-filter>
    	            <action />
    	            <category />
    	            <data />
    	        </intent-filter>
    	        <meta-data />
    	    </activity>
	
    	    <activity-alias>
    	        <intent-filter> . . . </intent-filter>
    	        <meta-data />
    	    </activity-alias>
	
    	    <service>
    	        <intent-filter> . . . </intent-filter>
    	        <meta-data/>
    	    </service>
	
    	    <receiver>
    	        <intent-filter> . . . </intent-filter>
    	        <meta-data />
    	    </receiver>
	
    	    <provider>
    	        <grant-uri-permission />
    	        <meta-data />
    	        <path-permission />
    	    </provider>
	
    	    <uses-library />
	
    	</application>
	
	</manifest>

接着是按字典序列出所有能在manifest出现的标签，只有这些是合法标签，你不能添加自己的标签或者属性。

    <action>
    <activity>
    <activity-alias>
    <application>
    <category>
    <data>
    <grant-uri-permission>
    <instrumentation>
    <intent-filter>
    <manifest>
    <meta-data>
    <permission>
    <permission-group>
    <permission-tree>
    <provider>
    <receiver>
    <service>
    <supports-screens>
    <uses-configuration>
    <uses-feature>
    <uses-library>
    <uses-permission>
    <uses-sdk>

###一些相关约定和规则

参见[这里](http://developer.android.com/guide/topics/manifest/manifest-intro.html#filec)

###特性

参见[这里](http://developer.android.com/guide/topics/manifest/manifest-intro.html#filef)
