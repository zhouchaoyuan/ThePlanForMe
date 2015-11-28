/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
*
* SAX步骤（参考《第一行代码》）：
* 1.定义一个getData方法将xml读取出来并返回到一个String
* 2.将读取出来的字符串传入parseXMLWithSAX解析并输出，分以下小步骤：
*	a.SAXParserFactory获取一个实例，并通过这个实例得到一个SAXParser
*	b.通过SAXParser调用getXMLReader()获取一个XMLReader
*	c.设置XMLReader的ContentHandler(自己定义并继承DefaultHandler)并调用parse解析
*/
import java.io.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
class ReadDataFromXMLFile
{
	//读取xml为一个String
	public static String getData(String fileName){

		File file=new File(fileName);
		BufferedReader reader=null;
		try{
			String line;
			StringBuilder response=new StringBuilder();
			reader=new BufferedReader(new FileReader(file));
			while((line=reader.readLine())!=null){
				response.append(line);
			}
			reader.close();
			return response.toString();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try{
					reader.close();
				}catch(Exception e1){
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void parseXMLWithSAX(String content){
		try{
			//能够配置和获取基于 SAX 的解析器以解析 XML 文档的工厂,用静态方法newInstance获取实例
			SAXParserFactory factory=SAXParserFactory.newInstance();
			//使用当前配置的工厂参数创建 SAXParser 的一个新实例。
			SAXParser saxparser=factory.newSAXParser();
			//返回封装的XMLReader。 
			XMLReader xmlReader=saxparser.getXMLReader();
			ContentHandler handler=new ContentHandler();
			xmlReader.setContentHandler(handler);
			//执行解析
			xmlReader.parse(new InputSource(new StringReader(content)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		String response=getData("data.xml");
//		System.out.println(response);
		parseXMLWithSAX(response);
	}
}

class ContentHandler extends DefaultHandler
{
	private String nodeName;
	private StringBuilder id;
	private StringBuilder name;
	private StringBuilder ide;
	private StringBuilder descript;

	@Override
	public void startDocument() throws SAXException{
		id=new StringBuilder();
		name=new StringBuilder();
		ide=new StringBuilder();
		descript=new StringBuilder();
	}
	
	/**
	* @param uri - 名称空间 URI，如果元素没有任何名称空间 URI，或者没有正在执行名称空间处理，则为空字符串。 
    * @param localName - 本地名称（不带前缀），如果没有正在执行名称空间处理，则为空字符串。 
    * @param qName - 限定的名称（带有前缀），如果限定的名称不可用，则为空字符串。 
    * @param attributes - 附加到元素的属性。如果没有属性，则它将是空的 Attributes 对象。 
	* 事实上这个东西好像没用到
	*/
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
//		System.out.println(localName+"  zhouchaoyuan  "+qName);
//		nodeName=localName;
		nodeName=qName;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
//		System.out.println("zhouchaoyuan");
		switch(nodeName){
			case "id":
				id.append(ch,start,length);
				break;
			case "name":
				name.append(ch,start,length);
				break;
			case "ide":
				ide.append(ch,start,length);
				break;
			case "descript":
				descript.append(ch,start,length);
				break;
			default:
				break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
//		System.out.println(qName);
		if("Language".equals(qName)){
			//输出每一个次级Element
			System.out.println(id.toString());
			System.out.println(name.toString());
			System.out.println(ide.toString());
			System.out.println(descript.toString().trim());
			id.setLength(0);
			name.setLength(0);
			ide.setLength(0);
			descript.setLength(0);
		}
	}
	
	@Override 
	public void endDocument() throws SAXException{}
}