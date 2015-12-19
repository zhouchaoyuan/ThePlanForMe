/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
*
* 思路：
* 直接使用BigDecimal处理加减乘除
* 既然不要界面的话，那么就直接输入一个表达式计算吧，表达式类似：
*
* 123 + 4
* 123 - 4
* 123 * 4
* 123 / 4
* 12.3 + 0.7
* 12.3 - 1.3
* 12.3 * 2.7
* 12.3 / 3.7
* 0123 + 4
* 000 + 00
* 0 - -088
* -12.3 * 2.7
* +12.3 / -3.7
* 00123 / 000
*
*需要注意的是:
*	1.除法不能直接用计算机的除法，因为他会下取整
*	2.除法除数不能为零
*	3.无限小数采用15位向0方向取整
*	4.输出结果去掉小数点的后面的数的后导0，如：12.3 + 0.7 = 13
*
*/
import java.util.*;
import java.math.*;
public class Calculator{
	Calculator(){
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------欢迎使用只有加减乘除的计算器--------------");
		System.out.println("使用方法是输入一个表达式，操作符与左右两个操作数用空格隔开");
		Scanner cin=new Scanner(System.in);
		while(cin.hasNext()){//EOF结尾 win按F6或者ctrl+z  linux按使用ctrl+d
			String calc=cin.nextLine();
			//System.out.println(calc);
			String[] all=calc.split(" ");

			if(all.length!=3){//两个操作数和一个运算符
				System.out.println("输入有错误，请重新输入");
				continue;
			}
			String first=all[0],oper=all[1],second=all[2];
			//都使用BigDecimal处理，什么前导零什么乱七八糟的都一并给处理了
			BigDecimal firstNum=new BigDecimal(first);//精确表示
			BigDecimal secondNum=new BigDecimal(second);//精确表示			
			if("/".equals(oper)&&secondNum.equals(BigDecimal.ZERO)){
				System.out.println("输入非法，除数为0，请重新输入");
				continue;
			}
			else{
				BigDecimal ans=BigDecimal.ZERO;
				switch(oper){//当前版本适用String类型
					case "+":
						ans=firstNum.add(secondNum);
						break;
					case "-":
						ans=firstNum.subtract(secondNum);
						break;
					case "*":
						ans=firstNum.multiply(secondNum);
						break;
					case "/"://默认长度小数点后15位，向零方向舍入
						ans=firstNum.divide(secondNum,15,RoundingMode.DOWN);
						break;
					default:
				}
				String result=ans.stripTrailingZeros().toPlainString();
				System.out.println(firstNum+" "+oper+" "+secondNum+" = "+result);
			}
		}
	}
	public static void main(String[] args){
		new Calculator();
	}
}
/*

input:

123 + 4
123 - 4
123 * 4
123 / 4
12.3 + 0.7
12.3 - 1.3
12.3 * 2.7
12.3 / 3.7
0123 + 4
000 + 00
0 - -088
-12.3 * 2.7
+12.3 / -3.7
00123 / 000

output:

123 + 4 = 127
123 - 4 = 119
123 * 4 = 492
123 / 4 = 30.75
12.3 + 0.7 = 13
12.3 - 1.3 = 11
12.3 * 2.7 = 33.21
12.3 / 3.7 = 3.324324324324324
123 + 4 = 127
0 + 0 = 0
0 - -88 = 88
-12.3 * 2.7 = -33.21
12.3 / -3.7 = -3.324324324324324
输入非法，除数为0，请重新输入
*/