/**
*
* @author chaoyuanzhou@foxmail.com
* @version 1.0
* 接口List<E>，属于序列，实现了Collection<E>，ArrayList和LinkedList都是他的子类
* 接口Map<K,V>，将键映射到值的对象，TreeMap，HashMap，LinkedHashMap都是他的子类
*
* 文件包含两个类：
*	1、ListAndMapDemo：
*		a、通过testList展示了list的一些功能
*		b、通过testMap展示了map的一些功能
*
*	2、Countries(取自《Java编程思想》)：
*		a、通过static的方法names产生“序列”来供给测试
*		b、通过static的方法capitals产生“键值对”来供给测试
*/
import java.util.*;
public class ListAndMapDemo
{
	static String s;
	static boolean b=false;		
	static Iterator<String>it;
	static ListIterator<String>lit;
	public static void main(String[] args){
		testList(new ArrayList<String>(Countries.names(3)));
		testList(new LinkedList<String>(Countries.names(3)));
		System.out.println("\n============================================\n");
		testMap(new TreeMap<String,String>(Countries.capitals(3)));
		testMap(new HashMap<String,String>(Countries.capitals(3)));
		testMap(new LinkedHashMap<String,String>(Countries.capitals(3)));
	}
	public static void testList(List<String> list){		
		System.out.println("initialize...\n----------------------------");
		System.out.println("The list is: "+list);
		list.add(0,"zhouchaoyuan");
		System.out.println("add zhouchaoyuan in location 0: "+list);
		list.add("acgege");
		System.out.println("add acgege in last: "+list);
		list.addAll(Countries.names(4));
		System.out.println("add 4 country in last"+list);
		list.addAll(2,Countries.names(4));
		System.out.println("add 4 country in location 2: "+list);
		b=list.contains("zcy");
		System.out.println("contains elements zcy: "+b);
		b=list.containsAll(Countries.names(4));
		System.out.println("contains elements first 4 countrise: "+b);
		s=list.get(1);
		it=list.iterator();
		lit=list.listIterator();
		lit=list.listIterator(2);
		System.out.println(list.size());
		list.remove("zhouchaoyuan");
		System.out.println("After remove zhouchaoyuan: "+list);
		list.set(4,"zcy");
		System.out.println("After modify the element in location 4: "+list);
		list.clear();
		System.out.println("After clear: "+list);
	}
	public static void testMap(Map<String,String>map){
		System.out.println("initialize...\n----------------------------");
		System.out.println("The map is: "+map);
		map.put("zhouchaoyuan","23");
		System.out.println("After put one elements in map: "+map);
		map.putAll(Countries.capitals(2));//相同的被忽略了
		System.out.println("After put 2 elements in map: "+map);
		b=map.containsKey("CHINA");
		System.out.println("map containsKey CHINA: "+b);
		b=map.containsValue("zcy");
		System.out.println("map containsValue zcy: "+b);
		s=map.get("CHINA");
		System.out.println("The value map CHINS is: "+s);
		System.out.println("Key set is: "+map.keySet());
		map.remove("CYPRUS");
		System.out.println("After remove CYPRUS: "+map);
		System.out.println("Value set is: "+map.values());
		System.out.println("The size of the map is: "+map.size());
	}
}

/**
*Countries
*---FlyweightMap
*-------Entry(EntrySet need)
*-------EntrySet(entrySet() need)
*-----------Iter(iterator need)
*/
class Countries{
	public static final String[][] DATA={//部分数据
		//Asia
		{"CHINA","Beijing"},{"CYPRUS","Nicosia"},
		{"INDIA","New Delhi"},{"INDONESIA","Jakarta"},
		{"IRAN","Tatran"},{"IRAQ","Baghdad"},
		//Africa
		{"ALGERIA","Algiers"},{"ANGOLA","Luanda"},
		{"BENIN","Porto-Novo"},{"BOTSWANA","Gaberone"},
		{"CAMEROON","Yaounde"},{"CAPER VERDE","Praia"}
	};
	private static class FlyweightMap extends AbstractMap<String,String>{
		private static class Entry implements Map.Entry<String,String>{
			private int index;
			public Entry(int index){
				this.index=index;
			}
			public String getKey(){
				return DATA[index][0];
			}
			public String getValue(){
				return DATA[index][1];
			}
			public int hashCode(){
				return DATA[index][0].hashCode();
			}
			public boolean equals(Object o){
				return DATA[index][0].equals(o);
			}
			public String setValue(String value){
				throw new UnsupportedOperationException();
			}
		}
		static class EntrySet extends AbstractSet<Map.Entry<String,String>>{
			private int size;
			EntrySet(int size){
				if(size<0)
					this.size=0;
				else if(size>DATA.length)
					size=DATA.length;
				else 
					this.size=size;
			}
			public int size(){return size;}
			private class Iter implements Iterator<Map.Entry<String,String>>{
				Entry entry=new Entry(-1);
				public boolean hasNext(){
					return entry.index<size-1;
				}
				public Map.Entry<String,String> next(){
					entry.index++;
					return entry;
				}
				public void remove(){
					throw new UnsupportedOperationException();
				}
			}
			public Iterator<Map.Entry<String,String>> iterator(){//可以迭代
				return new Iter();
			}
		}
		private static Set<Map.Entry<String,String>> entries=new EntrySet(DATA.length);
		public Set<Map.Entry<String,String>> entrySet(){
			return entries;
		}
	}
	static Map<String,String> select(final int size){
		return new FlyweightMap(){
			public Set<Map.Entry<String,String>> entrySet(){
				return new EntrySet(size);
			}
		};
	}
	//键值对
	static Map<String,String>map=new FlyweightMap();
	public static Map<String,String> capitals(){
		return map;
	}
	public static Map<String,String> capitals(int size){
		return select(size);
	}
	//国家名字
	static List<String>names=new ArrayList<String>(map.keySet());
	public static List<String> names(){
		return names;
	}
	public static List<String> names(int size){
		return new ArrayList<String>(select(size).keySet());
	}
}



/**
output:


initialize...
----------------------------
The list is: [CHINA, CYPRUS, INDIA]
add zhouchaoyuan in location 0: [zhouchaoyuan, CHINA, CYPRUS, INDIA]
add acgege in last: [zhouchaoyuan, CHINA, CYPRUS, INDIA, acgege]
add 4 country in last[zhouchaoyuan, CHINA, CYPRUS, INDIA, acgege, CHINA, CYPRUS, INDIA, INDONESIA]
add 4 country in location 2: [zhouchaoyuan, CHINA, CHINA, CYPRUS, INDIA, INDONESIA, CYPRUS, INDIA, acgege, CHINA, CYPRUS, INDIA, INDONESIA]
contains elements zcy: false
contains elements first 4 countrise: true
13
After remove zhouchaoyuan: [CHINA, CHINA, CYPRUS, INDIA, INDONESIA, CYPRUS, INDIA, acgege, CHINA, CYPRUS, INDIA, INDONESIA]
After modify the element in location 4: [CHINA, CHINA, CYPRUS, INDIA, zcy, CYPRUS, INDIA, acgege, CHINA, CYPRUS, INDIA, INDONESIA]
After clear: []
initialize...
----------------------------
The list is: [CHINA, CYPRUS, INDIA]
add zhouchaoyuan in location 0: [zhouchaoyuan, CHINA, CYPRUS, INDIA]
add acgege in last: [zhouchaoyuan, CHINA, CYPRUS, INDIA, acgege]
add 4 country in last[zhouchaoyuan, CHINA, CYPRUS, INDIA, acgege, CHINA, CYPRUS, INDIA, INDONESIA]
add 4 country in location 2: [zhouchaoyuan, CHINA, CHINA, CYPRUS, INDIA, INDONESIA, CYPRUS, INDIA, acgege, CHINA, CYPRUS, INDIA, INDONESIA]
contains elements zcy: false
contains elements first 4 countrise: true
13
After remove zhouchaoyuan: [CHINA, CHINA, CYPRUS, INDIA, INDONESIA, CYPRUS, INDIA, acgege, CHINA, CYPRUS, INDIA, INDONESIA]
After modify the element in location 4: [CHINA, CHINA, CYPRUS, INDIA, zcy, CYPRUS, INDIA, acgege, CHINA, CYPRUS, INDIA, INDONESIA]
After clear: []

============================================

initialize...
----------------------------
The map is: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi}
After put one elements in map: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi, zhouchaoyuan=23}
After put 2 elements in map: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi, zhouchaoyuan=23}
map containsKey CHINA: true
map containsValue zcy: false
The value map CHINS is: Beijing
Key set is: [CHINA, CYPRUS, INDIA, zhouchaoyuan]
After remove CYPRUS: {CHINA=Beijing, INDIA=New Delhi, zhouchaoyuan=23}
Value set is: [Beijing, New Delhi, 23]
The size of the map is: 3
initialize...
----------------------------
The map is: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi}
After put one elements in map: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi, zhouchaoyuan=23}
After put 2 elements in map: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi, zhouchaoyuan=23}
map containsKey CHINA: true
map containsValue zcy: false
The value map CHINS is: Beijing
Key set is: [CHINA, CYPRUS, INDIA, zhouchaoyuan]
After remove CYPRUS: {CHINA=Beijing, INDIA=New Delhi, zhouchaoyuan=23}
Value set is: [Beijing, New Delhi, 23]
The size of the map is: 3
initialize...
----------------------------
The map is: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi}
After put one elements in map: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi, zhouchaoyuan=23}
After put 2 elements in map: {CHINA=Beijing, CYPRUS=Nicosia, INDIA=New Delhi, zhouchaoyuan=23}
map containsKey CHINA: true
map containsValue zcy: false
The value map CHINS is: Beijing
Key set is: [CHINA, CYPRUS, INDIA, zhouchaoyuan]
After remove CYPRUS: {CHINA=Beijing, INDIA=New Delhi, zhouchaoyuan=23}
Value set is: [Beijing, New Delhi, 23]
The size of the map is: 3

*/