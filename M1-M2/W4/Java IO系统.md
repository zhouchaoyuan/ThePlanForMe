#Java IO系统
###File类
File类表示一个文件集，我们一个可以调用list方法返回一个字符数组，这个数组包含所有的文件名和文件夹名。list还接受一个实现了FilenameFilter接口的类作为参数，以便对返回结果进行筛选！如：
```java

	import java.util.*;
	import java.io.*;
	public class Demo{
		public static void main(String[] args){
			File path=new File(".");
			//Lambda表达式： path.list((dir,name)-> name.matches(".*\\.java"));
			String[] list=path.list(new FilenameFilter(){
				public boolean accept(File dir,String name){
					return name.matches(".*\\.java");
				}
			});
			System.out.println(Arrays.toString(list));
		}
	}

```

###输入和输出
包括两种类型，分别是InputStream和OutputStream，他们分别有read()和write()方法。

- `InputStream`可以从不同的数据源产生输入的类，有：
	- 字节数组：`ByteArrayInputStream`
	- String对象：`StringBufferInputStream`
	- 文件：`FileInputStream`
	- 管道：`PipedInputStream`
	- 由其他种类的流组成的序列：`SequenceInputStream`
	- 其他数据源如Internet链接
- `OutputStream`该类别的类决定了输出要去往的方向，有：
	- 字节数组：`ByteArrayOutputStream`
	- 文件：`FileOutputStream`
	- 管道：`PipedOutputStream`


其中有`FilterInputStream`和`FilterOutputStream`分别用来控制特定的`InputStream`和`OutputStream`两个类:

- `FilterInputStream`有`DataInputStream`和`BufferedInputStream`等子类;
- `FilterOutputStream`有`DataOutputStream`、`PrintStream`和`BufferedOutputStream`等子类。

后来，由于国际化`Reader`和`Writer`逐渐取代（2字节unicode原因）`InputStream`和`OutputStream`。
后来可以通过装饰器（`InputStreamReader`和`OutputStreamWriter`）转换成前者。其具有类似的继承结构！

###一些流的使用方法：
#####读取
- **1、按行读取字符串：**

我们从一个文件中读取数据转换成字符串，可以使用如下方法（结合`BufferedReader`使用）：
```java

	public String read(String fileName){
		BufferedReader reader=null;
		try{
			String line;
			StringBuilder response=new StringBuilder();
			reader=new BufferedReader(new FileReader(fileName));
			while((line=reader.readLine())!=null){
				response.append(line);
			}
			reader.close();
			return response.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
 
```

- **2、按字符读取(使用`StringReader`)：**StringReader可以将字符串作为缓存，然后用read()方法挨个字符（返回int，需转型）读取！
- **3、按字节读取(使用`DataInputStream`)：**用`ByteArrayInputStream`（继承自`InputStream`）构造一个`DataInputStream`，然后getByte()挨个读取！

#####输出
- **1、使用PrintWriter（格式化输出）：**`PrintWriter pw=new PrintWriter("file");`
- **2、使用DataOutputStream：**`DataOutputStream dps=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));`
	
其中`DataOutputStream`和`DataInputStream`配合使用可以准确地读取数据（无论读和写的平台有多门的不相同），而`RandomAccessFile`可以随机读写文件，结合了两者的优点。

###标准I/O

在标准的I/O模型中，java提供`System.in` `System.out` `System.err` 前者属于没有包装的`InputStream`，后两个是`PrintStream`,
`InputStream`可以通过`new BufferedReader(new InputStreamReader(System.in))`包装;
`PrintStream`则一步就包装`new PrintWriter(System.out)`
###重定向
System提供三个静态方法支持我们重定向，分别是：

- setIn(InputStream)
- setOut(PrintStream)
- setErr(PrintStream)

对于大量的输出数据我们一般重定向输出到文件，一下是一个例子：
```java

	import java.io.*;
	public class Demo{
		public static void main(String[] args)throws IOException{
			PrintStream console=System.out;
			BufferedInputStream in=new BufferedInputStream(new FileInputStream("Demo.java"));//重定向的InputStream
			PrintStream out=new PrintStream(new FileOutputStream("out.txt"));//重定向到out.txt
			System.setIn(in);//设置输入流
			System.setOut(out);//设置标准输出流
			System.setErr(out);//设置错误输出流
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			String line;
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
			out.close();//关闭输出流
			System.setOut(console);	
		}
	}

``` 
ps:由于重定向是字节流，所以我们只可以使用字节操作流：`InputStream`和`OutputStream`

###进程控制
使用java程序来创建一个进程。在java.lang包中有`ProcessBuilder`，它用来管理一个进程属性集，调用start方法可以创建一个新的 Process 实例。如：

```java

    import java.io.*;
    public class OSExecuteDemo
    {
    	public static void main(String[] args){
    		OSExecute.Command("javap OSExecuteDemo");
    	}
    }
    class OSExecute
    {
    	public static void Command(String command){
    		boolean err=false;
    		try{
    			String line;
    			Process process=new ProcessBuilder(command.split(" ")).start();
    			BufferedReader br=new BufferedReader(new InputStreamReader(process.getInputStream()));
    			while((line=br.readLine())!=null){
    				System.out.println(line);
    			}
    			BufferedReader errors=new BufferedReader(new InputStreamReader(process.getErrorStream()));
    			while((line=errors.readLine())!=null){
    				System.out.println(line);
    				err=true;
    			}
    		}catch(Exception e){
    			throw new RuntimeException(command);
    		}
    		if(err){
    			throw new RuntimeException(command);
    		}
    	}
    }

```

输出：

    Compiled from "OSExecuteDemo.java"
    public class OSExecuteDemo {
      public OSExecuteDemo();
      public static void main(java.lang.String[]);
    }

###新I/O
#####缓冲区和通道
在java.nio.*包重新实现了旧的IO，并且提高了访问速度！以下是一个关于文件IO的读写，通过`FileInputStream`，`FileOutputStream`，`RandomAccessFile`,比普通的File快，一般的做法是将文件和FileChannel（通道）关联，然后和ByteBuffer（仅此一个可以和通道交互，CharBuffer等不行）交互，如：

```java

    import java.io.*;
    import java.nio.*;
    import java.nio.channels.*;
    public class Demo
    {
    	public static void main(String[] args)throws Exception{
    		//写文件
    		FileChannel fc=(new FileOutputStream("data.txt")).getChannel();//获得一个通道
    		fc.write(ByteBuffer.wrap("some data".getBytes()));//将ByteBuffer写入fc通道
    		fc.close();
    		//追加
    		fc=(new RandomAccessFile("data.txt","rw")).getChannel();//可读可写
    		fc.position(fc.size());
    		fc.write(ByteBuffer.wrap("other data".getBytes()));
    		fc.close();
    		//读文件
    		fc=(new FileInputStream("data.txt")).getChannel();
    		ByteBuffer bb=ByteBuffer.allocate(1<<10);
    		fc.read(bb);
    		bb.flip();//Buffer中，一系列通道读取或放置 操作之后，调用此方法为一系列通道写入或相对获取 操作做好准备
    		while(bb.hasRemaining()){//Buffer中，告知在当前位置和限制之间是否有元素
    			System.out.println((char)bb.get());
    		}
    	}
    }
```

上面的代码我们是挨个字符输出，我们可以在`getByte()`的时候指定编码为`UTF-16BE`，然后直接使用`System.out.println(b.asCharBuffer());`就可以全部输出。另外当要复制文件的时候我们可以为两个文件分别开辟缓冲区`ByteBuffer`，然后调用实例方法`transterTo`或者`transterFrom`。

当我们想缓冲区放进各种基本类型的时候可以这样：`b.asXXXBuffer().put();`其中b是`ByteBuffer`类型，XXX代表基本类型，如`b.asIntBuffer().put(1234)`。`b.asIntBuffer()`只是得到一个视图缓冲器，看起来是`IntBuffer`，实际上还是`ByteBuffer`，所以对他的修改都还是回映射到`ByteBuffer`。

#####字节存放次序：

```java

    import java.nio.*;
    import java.util.*;
    public class Demo
    {
    	public static void main(String[] args){
    		ByteBuffer b=ByteBuffer.wrap(new byte[12]);
    		b.asCharBuffer().put("abcdef");		
    		System.out.println(Arrays.toString(b.array()));
    		b.rewind();//重绕此缓冲区,返回数据开始部分
    		b.order(ByteOrder.LITTLE_ENDIAN);
    		b.order(ByteOrder.BIG_ENDIAN);
    		System.out.println(Arrays.toString(b.array()));
    		b.rewind();//重绕此缓冲区,返回数据开始部分
    		b.order(ByteOrder.LITTLE_ENDIAN);
    		b.asCharBuffer().put("abcdef");	
    		System.out.println(Arrays.toString(b.array()));
    	}
    }

```
输出：

	[0, 97, 0, 98, 0, 99, 0, 100, 0, 101, 0, 102]
	[0, 97, 0, 98, 0, 99, 0, 100, 0, 101, 0, 102]
	[97, 0, 98, 0, 99, 0, 100, 0, 101, 0, 102, 0]
说明默认用大端。

这个和c++里面的大端小端问题是一个道理的，如：

```c++

	#include<iostream>
	struct NODE{
	    char ch1,ch2;
	    NODE(char CH1,char CH2){
        	ch1=CH1;
        	ch2=CH2;
    	}
	};
	int main(){
	    NODE node('a','b');
	    short *p=(short*)&node;
	    ///输出 25185 25185，说明是我的机器使用小端方式
	    std::cout<<(int)((node.ch2<<8)+node.ch1)<<" "<<(*p)<<std::endl;
	    return 0;
	}


```

在java.nio中包含了`ByteBuffer`，`CharBuffer`，`IntBuffer`，`DoubleBuffer`等等,而这些类都继承自Buffer，缓冲区之所以读写如此高效是基于四个索引：

- **position（位置）**：**下一个**要读取或写入的元素的索引。不能为负，不能大于其限制。
- **limit（限制）**：是第一个不应该读取或写入的元素的索引。限制不能为负，不能大于其容量。
- **capacity（容量）**：它所包含的元素的数量。容量不能为负，不能更改。
- **mark（标记）**：一个索引，在调用 reset 方法时会将缓冲区的位置重置为该索引。定义标记时，不能定义为负数，不能大于位置。 

 一般打印`position`和`limit`之间的数据，另外`Buffer`中提供了很多方法与这四个索引打交道，具体见api。

#####内存映射文件
利用内存映射我们可以假定整个文件都存放在内存中，当成一个非常大的数组来访问，如：

```java

	import java.io.*;
	import java.nio.*;
	import java.nio.channels.*;
	public class LargeMappedFiles
	{
		static int length=1<<7+16;
		public static void main(String[] args)throws Exception{
			MappedByteBuffer mb=new RandomAccessFile("test.dat","rw").getChannel()
			.map(FileChannel.MapMode.READ_WRITE,0,length);
			for(int i=0;i<length;i++){
				mb.put((byte)'a');
			}
			System.out.println("写入成功");
			for(int i=length/3;i<length/3+5;i++){
				System.out.print((char)mb.get(i));
			}
		}
	}
```

#####文件加锁
文件加锁允许我们同步的访问某个作为共享资源的文件。多个访问的线程可以位于不同的java虚拟机。除了java线程之外还可以是系统线程。加锁的文件对系统进程一样有效，因为java的加锁直接映射到系统的本地加锁。文件加锁可以通过对`FileChannel`使用:

- `tryLock()`：非阻塞式的，设法获取`FileLock`，不能获取就直接返回
- `lock()`：阻塞式的，阻塞进程直到可以获取`FileLock`。

```java

	import java.io.*;
    import java.util.concurrent.*;
    import java.nio.channels.*;
    public class DemoFileLock
    {
    	public static void main(String[] args)throws Exception{
    		FileChannel fc=new FileOutputStream("o.txt").getChannel();
    		FileLock fl=fc.tryLock();//尝试加锁
    		if(fl!=null){
    			System.out.println("文件加锁成功");
    			TimeUnit.MILLISECONDS.sleep(1000);//休眠1000毫秒
    			fl.release();
    			System.out.println("文件释放成功");
    		}
    	}
    }

```

另外文件加锁分成共享锁和独占锁，哪种锁定我们可以通过`FileLock.isChared()`获得，这两个锁定都必须有底层操作系统支持，如果不支持共享锁的话一般都有转换成独占锁。lock还可以接受参数对文件部分加锁。


###读写压缩格式的数据流
读写压缩格式的数据流时我们可以使用`ZipInputStream`,`GZIPInputStream`,`ZipOutputStream`,`GZIPOutputStream`,GZIP接口一般只用来对单个数据流进压缩，而Zip可以进行多文件保存，以下是一个使用GZIP的例子：

```java

    import java.io.*;
    import java.util.zip.*;
    public class UsingGZIP
    {
    	public static void main(String[] args)throws Exception{
    		BufferedReader br=new BufferedReader(new FileReader("UsingGZIP.java"));
    		BufferedOutputStream bos=new BufferedOutputStream(
    			new GZIPOutputStream(new FileOutputStream("UsingGZIP.zip")));
    		int c;
    		System.out.println("Writing file");
    		while((c=br.read())!=-1){
    			bos.write(c);
    		}
			try{}finally{
    			br.close();
	    		bos.close();
			}
    		System.out.println("finished\nreading file");
    		br=new BufferedReader(new InputStreamReader(
    			new GZIPInputStream(new FileInputStream("UsingGZIP.zip"))));
    		String line;
    		while((line=br.readLine())!=null){
    			System.out.println(line);
    		}
    	}
    }

```
另外我们也可以使用jar命令压缩文件，可以压缩我们自己的jar文件，然后加入classpath变量中供我们自己使用。

###对象序列化
一般情况下对象会随着程序的结束而消失，当我们在程序结束之前使用对象序列化将一个对象转换成字节，就能够在以后程序重新运行的时候将这个字节序列完全恢复为原来的对象。这个操作也可以通过网络进行。容器，`Class`对象，封装器都支持对象序列化。对象序列化属于轻量级持久性，通过实现`Serializable`实现。下面一个例子将一个对象存到硬盘上面，然后通过另一个程序恢复（这种恢复可对引用里面的引用恢复）：

```java

	//Output.java(先运行)
    import java.io.*;
    class Person implements Serializable{
    	private String name;
    	Person(String name){this.name=name;}
    	public String toString(){
    		return "My name is "+name;
    	}
    }
    public class Output
    {
    	public static void main(String[] args)throws IOException{
			////当然我们可以选择使用缓冲区而不使用文件存储
    		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("out.zcy"));
    		oos.writeObject(new Person("zhouchaoyuan"));
    	}
    }

```

恢复的文件：

```java

	//Input.java(后运行)
    import java.io.*;
    public class Input
    {
    	public static void main(String[] args)throws Exception{
    		ObjectInputStream ois=new ObjectInputStream(new FileInputStream("out.zcy"));
    		Object obj=ois.readObject();
    		System.out.println(obj.getClass());//class Person
    		System.out.println(obj);//My name is zhouchaoyuan
    	}
    }

```

我们还可以通过`Externalizable`实现可控序列化，不过要重写`writeExternal`和`readExternal`两个方法，并且把域通过这个两个方法的参数写入和读出，见p577.

由于`Serializable`是自动保存所有数据域，但我们不想保存某些数据域的时候，我们可以使用关键字`transient`修饰该域。虽然`Externalizable`也可以达到同样的效果，但是前者更佳。

对于两个对象，如果都有指向第三个对象的相同引用，进行序列化然后反序列化,第三个对象依旧只会出现一次。如果同一个对象序列化到同一个流中只会保存一份序列。`static`字段不会自动进行序列化，且其在所有子类中也只出现一份。

###XML

XML使用非常广泛，它比反序列化更具有通用性。以下例子我们从xml文件中读取数据：

```java

    //导入jar包 xom-1.2.10.jar
    import nu.xom.*;
    import java.io.*;
    import java.util.*;
    public class ReadXMLFile extends ArrayList<Person>
    {
    	ReadXMLFile(String fileName)throws Exception{
    		Document doc=new Builder().build(new BufferedReader(new FileReader(fileName)));
    		Elements elements=doc.getRootElement().getChildElements();
    		for(int i=0;i<elements.size();i++){
    			add(new Person(elements.get(i)));
    		}
    	}
    	public static void main(String[] args)throws Exception{
    		System.out.println(new ReadXMLFile("People.xml"));
    	}
    }
    class Person
    {
    	private String first,second;
    	public Person(Element person){
    		first=person.getFirstChildElement("first").getValue();
    		second=person.getFirstChildElement("second").getValue();
    	}
    	public String toString(){
    		return first+" "+second;
    	}
    }

```

People.xml：

    <?xml version="1.0" encoding="ISO-8859-1"?>
    <people>
    	<person>
    		<first>zhou</first>
    		<second>chaoyuan</second>
    	</person>
    </people>


###Preferences
`Preferences`比序列化持久性更好。以键值对存储。不过只能存储基本类型和字符串，字符串长度小于8K。

```java

    import java.util.prefs.*;
    public class Main
    {
    	public static void main(String[] args)throws Exception{
    		//使用当前类作为节点标识符
    		Preferences prefs=Preferences.userNodeForPackage(Main.class);
    		prefs.put("zcy","zhouchaoyuan");
    		prefs.put("jiudian","meituan");
    		prefs.putInt("ten",10);
    		int number=prefs.getInt("number",0)+1;
    		prefs.putInt("number",number);
    		for(String key:prefs.keys()){
    			System.out.println(key+": "+prefs.get(key,null));
    		}
    
    	}
    }

```