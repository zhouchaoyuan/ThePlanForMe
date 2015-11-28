/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
* 思路：
*
* 一开始我add函数里面是这样的
* for(int i=0;i<number2;i++){
*		number1++;
* }
* return number1;
*
* 1.当我在测试的时候发现有负数出现的时候就有问题,所以我想到了补码,补码的出现就是为了避免负数的窘境，
* 因为按位相加即可以得到正确结果。于是在加法里面我先用异或运算取出不用进位的，用和运算取出需要进位
* 的并左移一位，然后两者相加就是结果，这个一种递归的实现，然后因为有左移的存在，必定会是一个数是0，
* 所以这个时候返回另外一个数就可以了，后面我用了递推的方法，用了自增！
* 
* 2.另外用了reverse求一个数的相反数，也是利用补码的性质，先给所有位按位取反（包括符号位）,然后自增。
* 因为负数的话显示出来是将原码符号位不变，按位取反，自增1，我这里连符号位都变了，所以变成了相反数。
* 
* 3.减法直接使用加第二数的相反数
*
* 4.乘法利用二进制拆解，然后用sign记录符号，效率比较高
* 
* 5.除法直接枚举解，也用sign记录符号位
*/
public class MyOperation
{
	/**
	* @param number 求的相反数
	* @return 返回number的相反数
	*/
	public static int reverse(int number){
		number=~number;
		return ++number;
	}
	/*
	public static int add(int number1,int number2){
		if(number2==0)
			return number1;
		int xorSum=number1^number2;
		int andSum=number1&number2;
		return add(xorSum,andSum<<1);
	}
	*/

	/**
	* 上面的add是递归实现的，下面使用自增在for循环里面实现
	* @param number1 加法的第一个操作数
	* @param number2 加法的第二个操作数
	* @return 返回两个数相加的结果
	*/
	public static int add(int number1,int number2){
		for(int i=0;;i++){
			if(number2==0)
				return number1;
			int xorSum=number1^number2;
			int andSum=number1&number2;
			number1=xorSum;
			number2=andSum<<1;
		}
	}

	/**
	* @param number1 减法的第一个操作数
	* @param number2 减法的第二个操作数
	* @return 返回两个数相减的结果
	*/
	public static int subtract(int number1,int number2){
		number2=reverse(number2);
		return add(number1,number2);
	}

	/**
	* @param number1 乘法的第一个操作数
	* @param number2 乘法的第二个操作数
	* @return 返回两个数相乘的结果
	*/
	public static int multiply(int number1,int number2){
		int ans=0,sign=0;//记录符号
		if(number1<0){
			sign^=1;
			number1=reverse(number1);
		}
		if(number2<0){
			sign^=1;
			number2=reverse(number2);
		}
		while(number2>0){
			if((number2&1)==1){
				ans=add(ans,number1);
			}
			number2>>=1;
			number1=add(number1,number1);
		}
		if(sign==1)
			return reverse(ans);
		return ans;
	}

	/**
	* @param number1 除法的第一个操作数
	* @param number2 除法的第二个操作数
	* @exception ArithmeticException 除以0异常
	* @return 返回两个数相除的结果
	*/
	public static int divide(int number1,int number2){
		int ans=0,sign=0;//记录符号
		if(number2==0){  
			throw new ArithmeticException("除数不能为0");
		}
		if(number1<0){
			sign^=1;
			number1=reverse(number1);
		}
		if(number2<0){
			sign^=1;
			number2=reverse(number2);
		}
		while(number1>=number2){
			ans++;
			number1=subtract(number1,number2);
		}
		if(sign==1)
			return reverse(ans);
		return ans;
	}

	public static void main(String[] args) 
	{
		System.out.println(add(4,6));
		System.out.println(add(4,-6));
		System.out.println(add(-4,6));
		System.out.println(add(-4,-6));

		System.out.println(subtract(6,4));
		System.out.println(subtract(4,6));
		System.out.println(subtract(6,-4));
		System.out.println(subtract(-6,4));
		System.out.println(subtract(-6,-4));
		System.out.println(subtract(-4,6));

		System.out.println(multiply(5,7));
		System.out.println(multiply(5,-7));
		System.out.println(multiply(-5,7));
		System.out.println(multiply(-5,-7));

//		System.out.println(divide(5,0));
		System.out.println(divide(3,5));
		System.out.println(divide(5,3));
		System.out.println(divide(5,2));
		System.out.println(divide(9,3));
		System.out.println(divide(9,-3));
		System.out.println(divide(-9,3));
		System.out.println(divide(-9,-3));
		MyOperation[] ops=new MyOperation[5];
		for(MyOperation op:ops){
			System.out.println(op);
		}
	}
}
