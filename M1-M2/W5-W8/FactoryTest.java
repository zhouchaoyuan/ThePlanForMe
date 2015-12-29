//使用匿名内部类的抽象工厂模式
interface Service{
    void work();
}
interface ServiceFactory{
    Service getService();
}
class Waiter implements Service{
    private Waiter(){}//private的构造器
    public void work(){
        System.out.println("男服务员在提供服务");
    }//static域
    public static ServiceFactory factory=new ServiceFactory(){//匿名类
        public Service getService(){
            return new Waiter();
        }
    };
}
class Waitress implements Service{
    private Waitress(){}//private的构造器
    public void work(){
        System.out.println("女服务员在提供服务");
    }//static域
    public static ServiceFactory factory=new ServiceFactory(){//匿名类
        public Service getService(){
            return new Waitress();
        }
    };
}   
public class FactoryTest{
    public static void Factory(ServiceFactory SF){
        Service service=SF.getService();//获得服务
        service.work();//进行服务
    }
    public static void main(String[] args){
        Factory(Waiter.factory);
        Factory(Waitress.factory);
    }
}