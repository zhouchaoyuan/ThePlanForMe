#Java IO系统
###File类
File类表示一个文件集，我们一个可以调用list方法返回一个字符数组，这个数组包含所有的文件名和文件夹名。list还接受一个实现了FilenameFilter接口的类作为参数，以便对返回结果进行筛选！如：
	
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