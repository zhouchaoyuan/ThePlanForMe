/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
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
		int ans=0,sign=0;
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
		int ans=0,sign=0;
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

	}
}
