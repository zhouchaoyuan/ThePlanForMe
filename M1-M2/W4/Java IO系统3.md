#Java IO系统
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