# Manifest

> 参考自[官网](http://developer.android.com/guide/topics/manifest/manifest-intro.html)

每一个`Android`应用的根目录都有一个清单(`AndroidManifest.xml`文件，这个清单文件呈现给`Android`系统关于应用程序的一些基本信息,以及你在运行app之前必须拥有的系统信息，`AndroidManifest`的大致功能如下：

- 它为应用程序命名了包名，包名称是应用程序的唯一标识符 `package="..."`
- 它描述了应用程序的组成----`activities`（活动），`services`（服务），`broadcast` `broadcast receivers`（广播接收器），`content providers`（内容提供者）。它还表明了实现了上述四大组件的类（ 如：默认的工程有`<activity android:name=".MainActivity">`这么一句话，说明有一个`Activity`，而实现了这个`Activity`的类是当前目录下的`MainActivity.java`文件 ）以及他们的属性（如：处理的Intent（意图）消息）。这些声明让`Android`系统知道他们是什么组件，在什么条件下他们能够运行。
- 它决定哪些进程将执行应用程序的组件。
- 它表明应用程序需要什么权限来访问受保护的部件以及和其他应用程序交互。
- 它还表明了别人需要什么权限才能和应用程序的组件交互。
- 它列出了应用程序运行时 提供概要信息和其他信息的`Instrumentation`类。这些声明只有在应用程序处于开发和测试阶段才会存在清单中，他们在发布应用程序之前就被删除了。
- 它表明应用程序需要的最小的`Android API`版本
- 它列出该应用程序必须链接的库。

## AndroidManifest的结构

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

##一些相关约定和规则

>参见[官网](http://developer.android.com/guide/topics/manifest/manifest-intro.html#filec)

#####Elements(标签)
只有`<manifest>`和`<application>`这两个标签是必须的，而且只能出现一次，其他的标签可以出现一次或者不出现，即使是他们有一些必须出现在`manifest`中从而实现一些有意义的事情。
同等级的标签顺序无先后之分，如：`<activity>`, `<provider>`, 和 `<service>`可以是任意排列。（一个类似`<activity-alias>`的子标签必须遵循这样的规则，跟着父标签`<activity>`标签后面）

#####Attributes(属性)
正常情况下，所有的属性值都是可选的。不过，为了达到某个目录必须给标签声明属性值。根据开发文档指引，对于可选的属性，一般有一个默认的缺省值或者默认状态。除了根标签`<manifest>`的属性之外，所有的其他标签的属性必须以`android:`开头，例如`android:alwaysRetainTaskState`。因为前缀是通用的，所以当文档通过名字来索引属性的时候通常可以忽略前缀。

#####Declaring class names(声明类名)

很多标签都类似`java`中的`Object`，包括`<application>`标签和四大组件`activities (<activity>)`, `services (<service>)`, `broadcast receivers (<receiver>)`, 和`content providers (<provider>).`他们都有一个特定的父类。
当你定义一个子类，它大部分情况下是为四大组件定制的，这个子类要通过`name`指明属性值，这个属性值必须包含全包名，例如，一个`Service`子类声明如下：

	<manifest . . . >
	    <application . . . >
        	<service android:name="com.example.project.SecretService" . . . >
            . . .
      	  </service>
        	. . .
    	</application>
	</manifest>

另外，我们可以使用简写，如果说前面的包名都是相同的，那么我们可以指定应用程序的包名（通过`<manifest>`的`package`属性），下面这个例子的效果和上面的一样，只是使用了简写：

	<manifest package="com.example.project" . . . >
	    <application . . . >
        	<service android:name=".SecretService" . . . >
	            . . .
        	</service>
        	. . .
    	</application>
	</manifest>

当我们启动一个组件时，Android为这个组件的子类创建一个实例，如果子类没有指定，就创建组件基类的实例。

##### Multiple values(多重值)

如果一个属性值要被是赋值很多次，这个标签可以一直重复，从而替代一行只有一个标签多个值的情况，如下，一个`intent-filter`可以多个`actions`:

	<intent-filter . . . >
	    <action android:name="android.intent.action.EDIT" />
	    <action android:name="android.intent.action.INSERT" />
	    <action android:name="android.intent.action.DELETE" />
	    . . .
	</intent-filter>

##### Resource values(资源值)

一些属性值可以展现给用户，如一个代表activity的标签和图标，这些属性值必须是本地的且来自资源文件或者主题文件，资源属性值的表达式格式如下：</br>`@[package:]type:name`</br>,另外，当资源文件和应用程序在一个包目录下时包名可以被省略，直接写成特定类型的资源，如：“String”，“drawable”，然后写上想要配置的特定的文件的名称，例如：</br>`<activity android:icon="@drawable/smallPic" . . . >`</br>主题的属性值也可以通过类似的方式，但不是一个 `@` 开头，而是一个 `?` 开头，如下：</br>`?[package:]type:name`</br>

##### 字符串值(String values)

当一个属性值是一个字符串时，双重反斜杠`\\`用来转义，如`\\n`代表一个换行符，`\\uxxxx`代表一个`Unicode`字符

### 文件特性
接下来这部分展现了`manifest`文件时如何实现`Android`的功能的。
</br>
>参见[官网](http://developer.android.com/guide/topics/manifest/manifest-intro.html#filef)

##### Intent Filters

`intents`用来激活应用程序的核心组件(`activities`，`services`，`broadcast receivers`)，它是描述期望行动的一系列相关信息，包括被执行的数据，应执行的组件的类别以及一些其他相关的指令，安卓系统定位一个合适的组件来响应intent，如果需要的的话，就通过这个意图来实例化这个组件的实例。</br>组件声明他们通过`intent-filters`可以响应什么样的`intents`，因为`Android`系统在启动组件之前必须知道一个组件掌控着什么样的`intents`，`intent filters`在`manifest`可以使用`<intent-filter>`指定。一个组件可以有很多的`filters`，但是每个一描述不同的属性。</br>如果一个`intent`明确的指定了要启动的组件，那么`filter`就不会起作用。当`intent`没有明确指定目标，并且通过了`filter`组件才会被启动。更详细的资料请看[Intents andIntent Filters.](http://developer.android.com/guide/components/intents-filters.html)

##### Icons and Labels

一些元素有`icon`和`label`属性，有些还有`description`属性，这个属性可以通过屏幕展现给用户，例如`<permission>`元素这三个属性都有，当用户请求这个权限时，这三个属性定义的东西将呈现给用户。</br>容器元素如果定义了`icon`和`label`属性那么他们的子元素将默认具有相同的定义。这样，如果在`<application>`中定义了他们，所有的组件都将默认具有相同的设置，组件也一样。</br>对`intent filter`设置的`icon`和`label`属性用来代表一个可匹配的组件，例如，定义了`"android.intent.action.MAIN" and"android.intent.category.LAUNCHER"`的`activity`将在应用版上显示，所用的图标就是他们提供的。</br>

##### Permissions

权限用于为访问设备上的代码和数据提供约束条件。这些限制用于保护易被误用的代码和数据，避免不良的用户体验。</br>
权限都需要单独进行定义，一个标签代表一个约束。例如：

	android.permission.CALL_EMERGENCY_NUMBERS
	android.permission.READ_OWNER_DATA
	android.permission.SET_WALLPAPER
	android.permission.DEVICE_POWER

一个特性至少会被一个权限保护。</br>
如果应用想要访问一个有权限保护的特性时，他必须在其`manifest`文件中使用`<uses-permission>`元素进行声明。当应用安装时，安装器会检查应用的作者和签名证书，有时会询问用户，以决定是否赋予应用相应权限。否则，应用尝试访问未授权的特性时将直接失败而用户也不会受到任何通知。</br>应用也可以使用权限来保护自己的组件。可以使用Android（[android.Manifest.permission](http://developer.android.com/reference/android/Manifest.permission.html)）或其他应用定义的任何权限，或者你也可以自己定义，一个新权限可以使用`<permission>`标签来定义，例如：

	<manifest . . . >
	    <permission android:name="com.example.project.DEBIT_ACCT" . . . />
	    <uses-permission android:name="com.example.project.DEBIT_ACCT" />
	    . . .
	    <application . . .>
        	<activity android:name="com.example.project.FreneticActivity"
                  	android:permission="com.example.project.DEBIT_ACCT"
                  	. . . >
            	. . .
        	</activity>
    	</application>
	</manifest>

</br>
注意，这个示例不但声明了权限还使用了权限。其他应用想要访问这个`Activity`必须取得相应的权限</br>如果还是这个例子，某权限在其他的地方进行了声明，那么这里就不用`<permission>`进行声明，但是还是需要使用`<uses-permission>`来获取相应权限。</br>`<permission-tree>`元素，声明了定义与代码中的一组权限的命名空间。`<permission-group>`定义了一个权限组，包含其他地方声明的权限。他只影响如何以组的形式提供权限。该元素不能指定已经属于一个组的权限；他只是提供一个组名。一个指定了组的权限可在`<permission>`中使用`permissionGroup`属性访问。

##### Libraries

所有的应用都默认包含了`Android`工具包，其中包含了构建app所用的基本组件。</br>当然，也有一些功能在其他一些包中提供，如果你要使用这些包，必须明确的指定他们。`Manifest`中使用`<uses-library>`元素来包含他们。

