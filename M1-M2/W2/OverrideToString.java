/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
*
*/
import java.lang.reflect.*;
public class OverrideToString
{
	int count;
	String firstString;
	String secondString;
	OverrideToString(int c,String f,String s){
		count=c;
		firstString=f;
		secondString=s;
	}
	public String toString(){
		return toStringUtil.toString(this);
		//jar包commons-lang.jar提供ToStringBuilder.reflectionToString直接转换
		//return ToStringBuilder.reflectionToString(this);
	}
	public static void main(String[] args) 
	{
		OverrideToString orts=new OverrideToString(23,"zhouchaoyuan","peter");
		System.out.println(orts);
	}
}

/**
* 编写功能类，里面重写了通用toString，并带有一个参数
* 可以把这个类设置成public放在一个文件中并置于功能包，导入使用
*/
class toStringUtil
{
	public static String toString(Object obj){
		Class c=obj.getClass();
		// 返回一个描述此 Field（包括其一般类型）的字符串。
		Field[] fields=c.getDeclaredFields();
		//使用Stringbuilder作为中间结果而不适用不可变的String
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		for(Field field:fields){
			//返回此 Field 对象表示的字段的名称。
			sb.append(field.getName()+":");
			try{
				// 返回指定对象上此 Field 表示的字段的值,捕捉如果field是private的异常
				sb.append(field.get(obj));
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}
			sb.append(",");
		}
		sb=sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
}