/**
* @author chaoyuanzhou@foxmail.com
* @version 1.0
*
* 查资料得：
* 手机号编码规则：
* 1、只能以1开头。原因如下：
*	a、0是作为长途号码开头的
*	b、2~8是普通的固定电话
*	c、9开头的是商业信心台或者客户服务使用的，如95533
* 2、第二位只能是3、5、8，原因如下：
*	a、如果是0、1的话容易拨打成10086、110
*	b、如果是2、4的话容易拨打成120、145（网卡专属号段）
*	c、6,7是会构成互联网接入号码，如165，17911
*	d、19开头号码有可能是特服号或者国外号码
* 3、手机号码固定11位，原因如下：
*	a、前3位是网络识别号
*	b、中间4位是地区编号
*	c、最后4位是用户号码，每段一万用户
* 4、不会出现154打头的，原因如下：
*	a、因为154谐音“要吾死” ╮(╯﹏╰)╭ 见 http://www.chahaoba.com/154
* 5、显然手机号只能包含数字
*/
public class CheckPhoneNumber
{
	/**
	* @param PhoneNumber 要判断是否合法的手机号
	* @return 返回PhoneNumber是否合法，true合法，false不合法
	*/
	public static boolean check(String PhoneNumber){
		/**
		* 正则表达式，从开头到结尾匹配一整串，
		* 对于前三位：13或者18开头的第三位可以是任意数
		*             15开头的第三位除了不能是4之外其他数都可以
		* 对于后八位：只要是刚好8位数字就行
		*/
		//正则写法有多种，还可以是^(((13|18)[0-9])|(15[0-35-9]))\\d{8}$
		return PhoneNumber.matches("^((1[38]\\d)|(15[^4|\\D]))\\d{8}$");
	}
	public static void main(String[] args){

		//非1开头
		System.out.println(check("05773270310"));//false
		System.out.println(check("25773270310"));//false
		System.out.println(check("35773270310"));//false
		System.out.println(check("45773270310"));//false
		System.out.println(check("55773270310"));//false
		System.out.println(check("65773270310"));//false
		System.out.println(check("75773270310"));//false
		System.out.println(check("85773270310"));//false
		System.out.println(check("95773270310"));//false

		//非3，5，8为第二位的
		System.out.println(check("10773270310"));//false
		System.out.println(check("11773270310"));//false
		System.out.println(check("12773270310"));//false
		System.out.println(check("14773270310"));//false
		System.out.println(check("16773270310"));//false
		System.out.println(check("17773270310"));//false
		System.out.println(check("19773270310"));//false

		//154打头的
		System.out.println(check("15473270310"));//false

		//长度不是11位或包含非数字的
		System.out.println(check("135322333"));      //false
		System.out.println(check("152637816246753"));//false
		System.out.println(check("1577327x310"));    //false
		System.out.println(check("1577_270310"));    //false
		
		//正确的
		System.out.println(check("15773270310"));//true
		System.out.println(check("18318850119"));//true
	}
}