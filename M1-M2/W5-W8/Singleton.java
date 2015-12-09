public class Singleton
{
	//私有构造器
	private Singleton(){};
	//私有域
	private static Singleton instance=new Singleton();	
	//共有类方法
	public static Singleton getInstance(){
		return instance;
	}
	public void print(){
		System.out.println("I am SingleTon!");
	}
	public static void main(String[] args){
		Singleton st=getInstance();
		st.print();
	}
}
/*
enum SingleTon{
	INSTANCE;
}
*/