import java.util.*;
class ReversibleArrayList<T> extends ArrayList<T>{
	ReversibleArrayList(Collection<T> c){super(c);}
	public Iterable<T> reversed(){//适配器使用
		return new Iterable<T>(){//实现Iterable的匿名类
			public Iterator<T> iterator(){//必须实现的方法
				return new Iterator<T>(){//实现Iterator的匿名类
					int index=size()-1;
					public boolean hasNext(){return index>-1;}
					public T next(){return get(index--);}
					public void remove(){//foreach时不能移除
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
}
public class AdapterDemo{
	public static void main(String[] args){
		ReversibleArrayList<String>list=
			new ReversibleArrayList<String>(
				Arrays.asList("this is a question".split(" ")));
		for(String str:list){
			System.out.print(str+" ");
		}
		System.out.println("");
		for(String str:list.reversed()){
			System.out.print(str+" ");
		}
	}
}