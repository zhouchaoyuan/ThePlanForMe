public class TemplateTest{
    public static void main(String[] args) {
        AbstractCalculator calPlus = new Plus();
        System.out.println(calPlus.calculate("8+8", "\\+"));
        AbstractCalculator calMultiply = new Multiply();
        System.out.println(calMultiply.calculate("8*8", "\\*"));
    }  
}
class Plus extends AbstractCalculator {  
    public int calculate(int num1,int num2) {  
        return num1 + num2;  
    }  
}
class Multiply extends AbstractCalculator {   
    public int calculate(int num1,int num2) {  
        return num1 * num2;  
    }  
}
abstract class AbstractCalculator{//模板
    //主方法，实现对本类其它方法的调用
    public final int calculate(String exp,String opt){  
        String array[] = exp.split(opt);
        return calculate(Integer.parseInt(array[0]),Integer.parseInt(array[1]));  
    }  
    //被子类重写的方法
    abstract public int calculate(int num1,int num2);  
}