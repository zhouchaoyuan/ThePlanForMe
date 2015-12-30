interface Strategy{
    public int work(int a,int b);
} 
public class StrategyTest{
    public static void print(int a,int b,Strategy strategy){
        System.out.println(strategy.work(a,b));
    }
    public static void main(String[] args){
        print(4,5,new AddStrategy());
        print(4,5,new SubtractCommand());
    }
}
class AddStrategy implements Strategy{
    public int work(int a,int b){
        return a+b;
    }
}
class SubtractCommand implements Strategy{
    public int work(int a,int b){
        return a-b;
    }
}