# Service

服务（`Service`）是Android实现后台运行的解决方案，它非常适合用于去执行那些不需要和用户交互而且还要求长期运行的任务。服务可由其他应用组件启动，而且即使用户切换到其他应用，服务仍将在后台继续运行。 此外，组件可以绑定到服务，以与之进行交互，甚至是执行进程间通信 (`IPC`)。 例如，服务可以处理网络事务、播放音乐，执行文件 I/O 或与内容提供程序交互，而所有这一切均可在后台进行。 服务运行于主线程，所以有可能阻塞主线程，耗时操作应该放在子线程中。

### 分类
服务大概分为两种：

- 启动服务：组件使用[`startService()`](http://developer.android.com/intl/zh-cn/reference/android/content/Context.html#startService%28android.content.Intent%29)启动，这种方式会回调[`onStartCommand()`](http://developer.android.com/intl/zh-cn/reference/android/app/Service.html#onStartCommand%28android.content.Intent,%20int,%20int%29)
- 绑定服务：组件使用[`bindService()`](http://developer.android.com/intl/zh-cn/reference/android/content/Context.html#bindService%28android.content.Intent,%20android.content.ServiceConnection,%20int%29)绑定到服务。回调方法[`onBind()`](http://developer.android.com/intl/zh-cn/reference/android/app/Service.html#onBind%28android.content.Intent%29)被执行。

>当然，服务也可以同时以这两种方式运行，问题只是在于您是否实现了一组回调方法：`onStartCommand()`（允许组件启动服务）和`onBind()`（允许绑定服务）。 

### 在manifest注册service

和`Activity`一样，我们必须要注册`service`，需添加`<service>`元素作为`<application>`元素的子元素。例如：

	<manifest ... >
		...
		<application ... >
			<service android:name=".ExampleService" />
			...
		</application>
	</manifest>

具体的标签使用见[`<service>`](http://developer.android.com/intl/zh-cn/guide/topics/manifest/service-element.html)

### 生命周期

`Service`的生命周期流程图如下：</br>![service_lifecycle](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W4/service_lifecycle.png)</br>
服务的生命周期比 Activity 的生命周期要简单得多。但是，密切关注如何创建和销毁服务反而更加重要，因为服务可以在用户没有意识到的情况下运行于后台。

服务生命周期（从创建到销毁）可以遵循两条不同的路径：

- 启动服务：该服务在其他组件调用 `startService()` 时创建，然后无限期运行，且必须通过调用 `stopSelf()` 来自行停止运行。此外，其他组件也可以通过调用 `stopService()` 来停止服务。服务停止后，系统会将其销毁。
- 绑定服务：该服务在另一个组件（客户端）调用 `bindService()` 时创建。然后，客户端通过 `IBinder` 接口与服务进行通信。客户端可以通过调用 `unbindService()` 关闭连接。多个客户端可以绑定到相同服务，而且当所有绑定全部取消后，系统即会销毁该服务。 （服务不必自行停止运行。）

这两条路径并非完全独立。也就是说，您可以绑定到已经使用 `startService()` 启动的服务。例如，可以通过使用 `Intent`（标识要播放的音乐）调用 `startService()` 来启动后台音乐服务。随后，可能在用户需要稍加控制播放器或获取有关当前播放歌曲的信息时，`Activity` 可以通过调用 `bindService()` 绑定到服务。在这种情况下，除非所有客户端均取消绑定，否则 `stopService()` 或 `stopSelf()` 不会真正停止服务。 

由于`Service`是抽象类，使用`Service`的时候必须继承它是实现自己的子类，然后重写必要的回调方法。


- `onStartCommand()`
	- 当另一个组件（如 `Activity`）通过调用`startService()`请求启动服务时，系统将调用此方法。一旦执行此方法，服务即会启动并可在后台无限期运行。 如果您实现此方法，则在服务工作完成后，需要由您通过调用`stopSelf()`或`stopService()`来停止服务。（如果您只想提供绑定，则无需实现此方法。） 
- `onBind()`
	- 当另一个组件想通过调用`bindService()`与服务绑定（例如执行`RPC`）时，系统将调用此方法。在此方法的实现中，您必须通过返回 `IBinder` 提供一个接口，供客户端用来与服务进行通信。请务必实现此方法，但如果您并不希望允许绑定，则应返回`null`。 
- `onCreate()`
	- 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用`onStartCommand()`或 `onBind()`之前）。如果服务已在运行，则不会调用此方法。 
- `onDestroy()`
	- 当服务不再使用且将被销毁时，系统将调用此方法。服务应该实现此方法来清理所有资源，如线程、注册的侦听器、接收器等。 这是服务接收的最后一个调用。 

### 创建启动服务

从传统上讲，您可以扩展两个类来创建启动服务：

- `Service`：这是适用于所有服务的基类。扩展此类时，必须创建一个用于执行所有服务工作的新线程，因为默认情况下，服务将使用应用的主线程，这会降低应用正在运行的所有`Activity`的性能。 
- `IntentService`：这是`Service`的子类，它使用工作线程逐一处理所有启动请求。如果您不要求服务同时处理多个请求，这是最好的选择。 您只需实现[`onHandleIntent()`](http://developer.android.com/intl/zh-cn/reference/android/app/IntentService.html#onHandleIntent%28android.content.Intent%29)方法即可，该方法会接收每个启动请求的`Intent`使您能够执行后台工作。 

#### 扩展Service

组件通过调用`startService()`启动，并且回调`Service`的`onStartCommand()`方法，下面示例：

```java

	package cn.zhouchaoyuan.component;
	import android.app.Service;
	import android.content.Intent;
	import android.os.Handler;
	import android.os.IBinder;
	import android.os.Message;
	import android.support.annotation.Nullable;
	import android.util.Log;
	import android.widget.Toast;
	public class ExampleService extends Service{
	    @Nullable
    	@Override
    	public IBinder onBind(Intent intent) {
    	    return null;
    	}
	
    	@Override
    	public void onCreate() {
    	    super.onCreate();
    	}
	
    	@Override
    	public int onStartCommand(Intent intent, int flags, int startId) {
    	    final int MyToast = 1;
    	    final Handler handler = new Handler(){
    	        @Override
    	        public void handleMessage(Message msg) {
    	            switch (msg.what){
    	                case MyToast:
    	                    Toast.makeText(ExampleService.this,"开始服务",Toast.LENGTH_LONG).show();
                        	break;
                    	default:
                        	break;
                	}
            	}
        	};
	
        	new Thread(new Runnable() {
            	@Override
            	public void run() {
            	    Log.e("ExampleService", "ExampleService");
            	    Message msg = new Message();
            	    msg.what = MyToast;
            	    handler.sendMessage(msg);
            	    stopSelf();//在service本身内自动停止，也可以在组件中使用stopService()
            	}
        	}).start();
        	return super.onStartCommand(intent, flags, startId);
    	}

    	@Override
    	public void onDestroy() {
	        super.onDestroy();
	    }
	}

```

上述`onStartCommand()`方法必须返回整型数。整型数是一个值，用于描述系统应该如何在服务终止的情况下继续运行服务（如上所述，IntentService 的默认实现将为您处理这种情况，不过您可以对其进行修改）。从`onStartCommand()`返回的值必须是以下常量之一：

- [`START_NOT_STICKY`](http://developer.android.com/intl/zh-cn/reference/android/app/Service.html#START_NOT_STICKY)：如果系统在`onStartCommand()`返回后终止服务，则除非有挂起`Intent`要传递，否则系统不会重建服务。这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。 
- [`START_STICKY`](http://developer.android.com/intl/zh-cn/reference/android/app/Service.html#START_STICKY)：如果系统在`onStartCommand()`返回后终止服务，则会重建服务并调用`onStartCommand()`，但绝对不会重新传递最后一个`Intent`。相反，除非有挂起`Intent`要启动服务（在这种情况下，将传递这些`Intent`），否则系统会通过空`Intent`调用`onStartCommand()`。这适用于不执行命令、但无限期运行并等待作业的媒体播放器（或类似服务）。 
- [`START_REDELIVER_INTENT`](http://developer.android.com/intl/zh-cn/reference/android/app/Service.html#START_REDELIVER_INTENT)：如果系统在`onStartCommand()`返回后终止服务，则会重建服务，并通过传递给服务的最后一个`Intent`调用`onStartCommand()`。任何挂起`Intent`均依次传递。这适用于主动执行应该立即恢复的作业（例如下载文件）的服务。 

然后在Activity调用：

```java

	Intent intent = new Intent(this, ExampleService.class);
	startService(intent);

```

另外，注册Service是必不可少的：

	<service android:name="cn.zhouchaoyuan.component.ExampleService"></service>

#### 扩展IntentService

扩展自`IntentService`的服务执行以下操作：

- 创建默认的工作线程，用于在应用的主线程外执行传递给`onStartCommand()`的所有`Intent`。
- 创建工作队列，用于将一个`Intent`逐一传递给`onHandleIntent()`实现，这样您就永远不必担心多线程问题。
- 在处理完所有启动请求后停止服务，因此您永远不必调用`stopSelf()`。
- 提供`onBind()`的默认实现（返回 `null`）。
- 提供`onStartCommand()`的默认实现，可将`Intent`依次发送到工作队列和`onHandleIntent()` 实现。

下面实现一个`IntentService`：

```java

	package cn.zhouchaoyuan.component;
	import android.app.IntentService;
	import android.content.Intent;
	import android.util.Log;
	public class ExampleIntentService extends IntentService{
	
    	public ExampleIntentService() {
    	    super("ExampleIntentService");
    	}
	
    	@Override
    	protected void onHandleIntent(Intent intent) {
    	    Log.e("CurrentThreadId",Thread.currentThread().getId()+"");//查看是否自动开启线程
    	}
	
    	@Override
    	public void onDestroy() {
    	    super.onDestroy();
			//查看是否自动销毁，打印主线程的编号
    	    Log.e("CurrentThreadId", Thread.currentThread().getId() + " have stopped auto!");
    	}
	}

```

然后在Activity调用：

```java

	Intent intent = new Intent(this, ExampleIntentService.class);
    Log.e("CurrentThreadId",Thread.currentThread().getId()+"");//查看是否自动开启线程
    startService(intent);

```

另外，注册Service是必不可少的：

	<service android:name="cn.zhouchaoyuan.component.ExampleIntentService"></service>

### 创建绑定服务

`bindService()`原型：[`public abstract boolean bindService (Intent service, ServiceConnection conn, int flags)`](http://developer.android.com/intl/zh-cn/reference/android/content/Context.html#bindService%28android.content.Intent,%20android.content.ServiceConnection,%20int%29)

- `service`：意图，如上代码，需要组件名字和服务的`class`
- `conn`：接受服务开始或者暂停的信息. 只能是`ServiceConnection`实例; 不能为`null`
- `flags`：可以选择的操作，可以是：0, `BIND_AUTO_CREATE`, `BIND_DEBUG_UNBIND`, `BIND_NOT_FOREGROUND`, `BIND_ABOVE_CLIENT`, `BIND_ALLOW_OOM_MANAGEMENT`, 或者 `BIND_WAIVE_PRIORITY`.参数的解释[见](http://developer.android.com/intl/zh-cn/reference/android/content/Context.html#bindService%28android.content.Intent,%20android.content.ServiceConnection,%20int%29)

>[绑定服务的详细解释](http://developer.android.com/intl/zh-cn/guide/components/bound-services.html)

要创建绑定服务，必须实现 `onBind()` 回调方法以返回 `IBinder`，用于定义与服务通信的接口。然后，其他应用组件可以调用 `bindService()` 来检索该接口，并开始对服务调用方法。服务只用于与其绑定的应用组件，因此如果没有组件绑定到服务，则系统会销毁服务,多个客户端可以同时绑定到服务。客户端完成与服务的交互后，会调用 `unbindService()` 取消绑定。一旦没有客户端绑定到该服务，系统就会销毁它。下面进行示例：

创建一个`ServiceConnection`（在这里是内部类）并绑定一个服务：

```java

	class ZcyConnection implements ServiceConnection {//第二个参数
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ExampleService.ZcyBinder myBind = (ExampleService.ZcyBinder) service;
            myBind.work();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { }        ;
    }
    public void StartBindService(View v){
        Intent intent = new Intent(this, ExampleService.class);
        ZcyConnection connection = new ZcyConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

```

修改`ExampleService.java`如下：

```java

	public class ZcyBinder extends Binder {
        public void work(){
            Log.e(this.getClass().getSimpleName(),"downloading");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ZcyBinder();
    }

```

绑定服务的生命流程图：</br>
![service_binding_tree_lifecycle](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W4/service_binding_tree_lifecycle.png)</br>

>**ps：**运行中的服务，可以向用户[发送通知](http://developer.android.com/intl/zh-cn//guide/topics/ui/notifiers/notifications.html)，还可以使用前台服务达到我们的目的。

见详细的[官方教学](http://developer.android.com/intl/zh-cn/guide/components/services.html)和[翻译](http://hukai.me/ android-training-course-in-chinese/background-jobs/run-background-service/index.html)