#File

文件存储是`Android`中最基本的一种数据存储方式，它不会对存储的内容进行任何的格式化处理，所有的数据都原封不动的保存到文件当中，因而它比较适合存储一些简单的文本和二进制数据。

###内外部存储的一些关联和区别

- `Internal storage:`
	- 总是可用的
	- 这里的文件默认只能被我们的`app`所访问
	- 当用户卸载`app`的时候，系统会把`internal`内该`app`相关的文件都清除干净
	- `Internal`是我们在想确保不被用户与其他`app`所访问的最佳存储区域。

- `External storage:`
	- 并不总是可用的，因为用户有时会通过USB存储模式挂载外部存储器，当取下挂载的这部分后，就无法对其进行访问了
	- 是大家都可以访问的，因此保存在这里的文件可能被其他程序访问
	- 当用户卸载我们的app时，系统仅仅会删除`external`根目录（[getExternalFilesDir()](http://developer.android.com/reference/android/content/Context.html#getExternalFilesDir(java.lang.String))）下的相关文件
	- External是在不需要严格的访问权限并且希望这些文件能够被其他app所共享或者是允许用户通过电脑访问时的最佳存储区域。

>Tip: 尽管app是默认被安装到internal storage的，我们还是可以通过在程序的manifest文件中声明android:installLocation 属性来指定程序安装到external storage

###External Storage保存文件

####声明

为了写数据到external storage, 必须在你[manifest](https://github.com/zhouchaoyuan/ThePlanForMe/blob/master/M3-M4/W2/Manifest.md)文件中请求WRITE_EXTERNAL_STORAGE权限：

	<manifest ...>
    	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	    ...
	</manifest>

####使用

当然`external storage`可能是不可用的，比如遇到`SD`卡被拔出等情况时。因此在访问之前应对其可用性进行检查。我们可以通过执行 [`Environment.getExternalStorageState()`](http://developer.android.com/reference/android/os/Environment.html#getExternalStorageState%28%29)来查询`external storage`的状态。若返回状态为[`Environment.MEDIA_MOUNTED`](http://developer.android.com/reference/android/os/Environment.html#MEDIA_MOUNTED), 则可以读写，如：

```java

	/*检查external storage是否可以读写*/
	public boolean isExternalStorageWritable() {
    	return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

```
####分类

`external storage`可能会保存下面两种类型的文件

- `Public files` :这些文件对与用户与其他app来说是`public`的，当用户卸载我们的app时，这些文件应该保留。例如，那些被我们的app拍摄的图片或者下载的文件
- `Private files`: 这些文件完全被我们的app所私有，它们应该在`app`被卸载时删除。尽管由于存储在`external storage`，那些文件从技术上而言可以被用户与其他`app`所访问，但实际上那些文件对于其他`app`没有任何意义。因此，当用户卸载我们的`app`时，系统会删除其下的`private`目录。例如，那些被我们的`app`下载的缓存文件。

一、想要将文件以`public`形式保存在`external storage`中，请使用[`Environment.getExternalStoragePublicDirectory()`](http://developer.android.com/reference/android/os/Environment.html#getExternalStoragePublicDirectory%28java.lang.String%29)方法来获取一个`File`对象，该对象表示存储在`external storage`的目录。这个方法会需要带有一个特定的参数来指定这些`public`的文件类型，以便于与其他`public`文件进行分类。参数类型包括[`Environment.DIRECTORY_MUSIC`](http://developer.android.com/reference/android/os/Environment.html#DIRECTORY_MUSIC)或者[`Environment.DIRECTORY_PICTURES`](http://developer.android.com/reference/android/os/Environment.html#DIRECTORY_PICTURES). 如下:

```java

	public File getAlbumStorageDir(String albumName) {
    	File file = new File(Environment.getExternalStoragePublicDirectory(
    	        Environment.DIRECTORY_PICTURES), albumName);
    	if (!file.mkdirs()) {
    	    Log.e(LOG_TAG, "Directory not created");
    	}
    	return file;
	}

```

二、想要将文件以`private`形式保存在`external storage`中，可以通过执行[`Context.getExternalFilesDir()`](http://developer.android.com/reference/android/content/Context.html#getExternalFilesDir%28java.lang.String%29)来获取相应的目录，并且传递一个指示文件类型的参数。每一个以这种方式创建的目录都会被添加到`external storage`封装我们`app`目录下的参数文件夹下（如下则是`albumName`）。这下面的文件会在用户卸载我们的`app`时被系统删除。如下示例：

```java

	public File getAlbumStorageDir(Context context, String albumName) {
	    File file = new File(context.getExternalFilesDir(
	            Environment.DIRECTORY_PICTURES), albumName);
	    if (!file.mkdirs()) {
	        Log.e(LOG_TAG, "Directory not created");
	    }
	    return file;
	}

```

###Internal Storage保存文件

方法一：当保存文件到internal storage时，可以通过执行下面两个方法之一来获取合适的目录作为`FILE`的对象：

- `Context.getFilesDir()` : 返回一个`File`，代表了我们`app`的`internal`目录
- `Context.getCacheDir()` : 返回一个`File`，代表了我们`app`的`internal`缓存目录。请确保这个目录下的文件能够在一旦不再需要的时候马上被删除

方法二：可以执行[`Context.openFileOutput()`](http://developer.android.com/reference/android/content/Context.html#openFileOutput%28java.lang.String,%20int%29)获取一个[`FileOutputStream`](http://developer.android.com/reference/java/io/FileOutputStream.html)用于写文件到`internal`目录。如下：

```java

	String filename = "myfile";
	String string = "Hello world!";
	FileOutputStream outputStream;
	try {
  		outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
  		outputStream.write(string.getBytes());
  		outputStream.close();
	} catch (Exception e) {
  		e.printStackTrace();
	}

```

执行[`Context.openFileInput`](http://developer.android.com/reference/android/content/Context.html#openFileInput%28java.lang.String%29)获取一个[`FileInputStream`](http://developer.android.com/reference/java/io/FileInputStream.html)用于从internal读取文件，如下：

```java

	public String load(){
		BufferedReader br;
		FileInputStream inputStream;
		StringBuilder content = new StringBuilder();
		try{
			inputStream = openFileInput(filename);
			br = new BufferedReader(new inputStreamReader(inputStream));
			String line="";
			while((line=br.readLine())!=null){
				content.append(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return content.toString();
	}

```


如果需要缓存一些文件，可以使用File.createTempFile()。例如：下面的方法从URL中抽取了一个文件名，然后再在程序的internal缓存目录下创建了一个以这个文件名命名的文件。

```java

 	public File getTempFile(Context context, String url) {
		File file;
   		try {
			String fileName = Uri.parse(url).getLastPathSegment();
   		    file = File.createTempFile(fileName, null, context.getCacheDir());	
		catch (IOException e) {
       	 	// Error while creating file
    	}
    	return file;
	}

```

###删除文件

- 在不需要使用某些文件的时候应删除它。删除文件最直接的方法是直接执行文件的delete()方法

```java

	myFile.delete();

```

- 如果文件是保存在internal storage，我们可以通过Context来访问并通过执行deleteFile()进行删除

```java	

	myContext.deleteFile(fileName);

```


</br>**窃取自**[中文教学](http://hukai.me/android-training-course-in-chinese/basics/data-storage/files.html) 