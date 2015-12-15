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