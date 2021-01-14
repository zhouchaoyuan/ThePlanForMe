/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
* ˼·��
*
* һ��ʼ��add����������������
* for(int i=0;i<number2;i++){
*		number1++; 
* }
* return number1;
*
* 1.�����ڲ��Ե�ʱ�����и������ֵ�ʱ���������,�������뵽�˲���,����ĳ��־���Ϊ�˱��⸺���ľ�����
* ��Ϊ��λ��Ӽ����Եõ���ȷ����������ڼӷ������������������ȡ�����ý�λ�ģ��ú�����ȡ����Ҫ��λ
* �Ĳ�����һλ��Ȼ��������Ӿ��ǽ�������һ�ֵݹ��ʵ�֣�Ȼ����Ϊ�����ƵĴ��ڣ��ض�����һ������0��
* �������ʱ�򷵻�����һ�����Ϳ����ˣ����������˵��Ƶķ���������������
* 
* 2.��������reverse��һ�������෴����Ҳ�����ò�������ʣ��ȸ�����λ��λȡ������������λ��,Ȼ��������
* ��Ϊ�����Ļ���ʾ�����ǽ�ԭ�����λ���䣬��λȡ��������1��������������λ�����ˣ����Ա�����෴����
* 
* 3.����ֱ��ʹ�üӵڶ������෴��
*
* 4.�˷����ö����Ʋ�⣬Ȼ����sign��¼���ţ�Ч�ʱȽϸ�
* 
* 5.����ֱ��ö�ٽ⣬Ҳ��sign��¼����λ
*/
public class MyOperation
{
	/**
	* @param number ����෴��
	* @return ����number���෴��
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
	* �����add�ǵݹ�ʵ�ֵģ�����ʹ��������forѭ������ʵ��
	* @param number1 �ӷ��ĵ�һ��������
	* @param number2 �ӷ��ĵڶ���������
	* @return ������������ӵĽ��
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
	* @param number1 �����ĵ�һ��������
	* @param number2 �����ĵڶ���������
	* @return ��������������Ľ��
	*/
	public static int subtract(int number1,int number2){
		number2=reverse(number2);
		return add(number1,number2);
	}

	/**
	* @param number1 �˷��ĵ�һ��������
	* @param number2 �˷��ĵڶ���������
	* @return ������������˵Ľ��
	*/
	public static int multiply(int number1,int number2){
		int ans=0,sign=0;//��¼����
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
	* @param number1 �����ĵ�һ��������
	* @param number2 �����ĵڶ���������
	* @exception ArithmeticException ����0�쳣
	* @return ��������������Ľ��
	*/
	public static int divide(int number1,int number2){
		int ans=0,sign=0;//��¼����
		if(number2==0){  
			throw new ArithmeticException("��������Ϊ0");
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
