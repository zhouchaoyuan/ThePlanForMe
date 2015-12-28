abstract class Person
{
	public abstract void say();
}
class Headmaster extends Person//班主任只有一个
{
	private Headmaster(){};
	private static Headmaster headMaster=new Headmaster();
	public static Headmaster getHeadMaster(){
		return headMaster;
	}
	public void say(){
		System.out.println(this+" say: I am Headmaster!");
	}
}
class Student extends Person//学生可以有多个人
{
	public void say(){
		System.out.println(this+" say: I am Student!"+this);
	}
}
class Monitor extends Person//可以有一个正班长和多个副班长
{
	public void say(){
		System.out.println(this+" say: I am Monitor!"+this);
	}
}
public class Singleton
{
	public static void main(String[] args){
		Headmaster hm1=Headmaster.getHeadMaster();
		Headmaster hm2=Headmaster.getHeadMaster();
		hm1.say();hm2.say();
		System.out.println("hm1: "+hm1+"; hm2:"+hm2+"; hm1==hm2:"+(hm1==hm2));
		Student stu1=new Student();
		Student stu2=new Student();
		stu1.say();stu2.say();
		System.out.println("stu1: "+stu1+"; stu2:"+stu2+"; stu1==stu2:"+(stu1==stu2));
		Monitor mon1=new Monitor();
		Monitor mon2=new Monitor();
		mon1.say();mon2.say();
		System.out.println("mon1: "+mon1+"; mon2:"+mon2+";mon1==mon2:"+(mon1==mon2));
	}
}