/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
* 实现的栈其实是一个链表模拟的出来的
*/
public class MyStack<E>
{
	//节点类,static使得此类的对象不依赖外围类的对象存在，然而在这里并没有什么卵用
	private static class MyNode<U>
	{
		U item;
		int number;
		MyNode<U> next;//指向下一个节点,节点类型为MyNode<U>，没有时为null
		MyNode(){
			item=null;
			next=null;
			number=0;
		}
		MyNode(U item,MyNode<U>next){
			this.item=item;
			this.next=next;
			this.number=next.number+1;
		}
		boolean end(){
			return item==null&&next==null;//最后一个节点，当前信息和写一个节点都是null
		}
	}
	private MyNode<E> top=new MyNode<E>();//初始化空栈
	
	//测试堆栈是否为空
	boolean empty(){
		return top.end();
	}

	//查看堆栈顶部的对象，但不从堆栈中移除它
	E peek(){
		return top.item;
	}

	//移除堆栈顶部的对象，并作为此函数的值返回该对象
	E pop(){
		E result=top.item;
		if(!top.end())
			top=top.next;
		return result;
	}

	//把项压入堆栈顶部。
	E push(E item){
		//前向星，向头结点插入节点
		top=new MyNode<E>(item,top);
		return item;
	}

	//返回对象在堆栈中的位置，以1为基数
	int search(Object o){
		if(top.end())return -1;
		if(o.getClass()!=top.item.getClass())return -1;
		MyNode<E> now=top;
		while(!now.end()){
//			System.out.println("acjiji: "+now.number);
			if(o.equals(now.item))
				return top.number-now.number+1;
			now=now.next;
		}
		return -1;
	}

	public static void main(String[] args){
		MyStack<String> ms=new MyStack<String>();
		for(String s:"zhouchaoyuan is name My ".split(" ")){
			ms.push(s);
		}
		
		System.out.println("name is in position: "+ms.search("name"));

		while(!ms.empty()){
			String nowString=ms.pop();
			System.out.print(nowString+" ");
		}
		System.out.println();

		MyStack<Integer> mi=new MyStack<Integer>();
		int[] in={1,2,3,4};
		for(Integer s:in){
			mi.push(s);
		}

		System.out.println("Integer 2 is in position: "+mi.search(2));
		System.out.println("peek item is:"+mi.peek());

		while(!mi.empty()){
			Integer nowString=mi.pop();
			System.out.println(nowString);
		}
	}
}