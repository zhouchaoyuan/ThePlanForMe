#Java IO系统
####新I/O
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
    		bb.flip();//Buffer中， 反转此缓冲区
    		while(bb.hasRemaining()){//Buffer中，告知在当前位置和限制之间是否有元素
    			System.out.println((char)bb.get());
    		}
    	}
    }
	```