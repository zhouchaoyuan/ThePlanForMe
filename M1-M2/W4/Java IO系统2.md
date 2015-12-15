#Java IO系统
###新I/O
#####缓冲区和通道
在java.nio.*包重新实现了旧的IO，并且提高了访问速度！以下是一个关于文件IO的读写，通过`FileInputStream`，`FileOutputStream`，`RandomAccessFile`,比普通的File快。
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

上面的代码我们是挨个字符输出，我们可以在`getByte()`的时候指定编码为`UTF-16BE`，然后直接使用`asCharBuffer()`就是全部输出。另外当要复制文件的时候我们可以为两个文件分别开辟缓冲区`ByteBuffer`，然后调用实例方法`transterTo`或者`transterFrom`。

当我们想缓冲区放进各种基本类型的时候可以这样：`b.asXXXBuffer().put();`其中b是`ByteBuffer`类型，XXX代表基本类型，如`b.asIntBuffer().put(1234)`。`b.asIntBuffer()`只是得到一个视图缓冲器，看起来是`IntBuffer`，实际上还是`ByteBuffer`，所以对他的修改都还是回映射到`ByteBuffer`。


