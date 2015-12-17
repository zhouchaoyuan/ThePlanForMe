/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
* 使用开源类库XOM来解析(www.xom.nu)
*
* XOM步骤（参考《java编程思想》）：
* 1.将文件路径包装成一个Reader，然后通过Buider构造Document
* 2.Document获取根元素，然后解析子元素
*
*ps：使用时导入jar包 xom-1.2.10.jar，快速方便，以下方式适合已知XML结构的XML文档
*/
import nu.xom.*;
import java.io.*;
import java.util.*;
public class ReadXMLFile extends ArrayList<Language>
{
	ReadXMLFile(String fileName)throws Exception{
		//使用字符（两个字节）流构建Document，从文件读取使用流，网络使用URL
		Document doc=new Builder().build(new BufferedReader(new FileReader(fileName)));
		Elements elements=doc.getRootElement().getChildElements();
		for(int i=0;i<elements.size();i++){
			add(new Language(elements.get(i)));
		}
	}
	public static void main(String[] args)throws Exception{
		System.out.println(new ReadXMLFile("data.xml"));
	}
}
//Element 类
class Language
{
	private String id,ide,name,descript;
	public Language(Element language){
		id=language.getFirstChildElement("id").getValue();
		name=language.getFirstChildElement("name").getValue();
		ide=language.getFirstChildElement("ide").getValue();
		descript=language.getFirstChildElement("descript").getValue();
	}
	public String toString(){
		return "编号:"+id+"; 名字:"+name+"; 使用的ide:"+ide+"; 描述:"+descript+"\n";
	}
}