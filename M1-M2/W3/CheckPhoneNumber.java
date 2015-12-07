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
*	e、不会出现154打头的，因为154谐音“要我死” ╮(╯﹏╰)╭ 见 http://www.chahaoba.com/154
* 3、手机号码固定11位，原因如下：
*	a、前3位是网络识别号
*	b、中间4位是地区编号
*	c、最后4位是用户号码，每段一万用户
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
		return PhoneNumber.matches("^(((13|18)[0-9])|(15[0-35-9]))\\d{8}$");
	}
	public static void main(String[] args){
		System.out.println(check("01234567890"));//false
		System.out.println(check("234322333"));//false
		System.out.println(check("172637816246753"));//false
		System.out.println(check("1577327x310"));//false
		System.out.println(check("10000000000"));//false
		System.out.println(check("14773270310"));//false
		System.out.println(check("15473270310"));//false
		System.out.println(check("15773270310"));//true
	}
}