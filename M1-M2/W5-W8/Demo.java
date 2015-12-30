import java.util.*;
class Person{
	private int age;
	private String name;
	public int getAge(){
		return age;
	}
	public String getName(){
		return name;
	}
	Person(int age,String name){
		this.age=age;
		this.name=name;
	}
	public String toString(){
		return "姓名："+name+",年龄："+age;
	}
}
public class Demo{
	public static void main(String[] args){
		Person[] persons={new Person(23,"zhouchaoyuan"),new Person(22,"acgege"),
			new Person(22,"acjiji"),new Person(21,"chaoyuanzhou")};

		Arrays.sort(persons,new Comparator<Person>(){//传入一个策略

			public int compare(Person o1,Person o2){
				if(o1.getAge()==o2.getAge()){
					return o1.getName().compareToIgnoreCase(o1.getName());
				}
				return o1.getAge()-o2.getAge();//年龄不会是long情况，减就行了
			}
		});

		for(Person person:persons){
			System.out.println(person);
		}
	}
}
/**
output:
姓名：chaoyuanzhou,年龄：21
姓名：acgege,年龄：22
姓名：acjiji,年龄：22
姓名：zhouchaoyuan,年龄：23
*/