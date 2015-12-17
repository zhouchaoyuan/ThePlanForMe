#Java IO系统
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