import java.util.concurrent.*;
class PriorityThreadFactory implements ThreadFactory{//实现线程工厂
	public Thread newThread(Runnable r){//实现的方法
		Thread thread=new Thread(r);
		thread.setPriority(Thread.MIN_PRIORITY);//改变优先级
		return thread;
	}
}
public class PriorityFromFactory implements Runnable{
	public void run(){
		try{
			while(!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println(Thread.currentThread());
			}
		}catch(InterruptedException e){
			System.out.println("Interrupted!");
		}
	}
	public static void main(String[] args)throws Exception{
		ExecutorService exe=Executors.newCachedThreadPool(
			new PriorityThreadFactory());//使用已经有的线程工厂
		for(int i=0;i<10;i++){
			exe.execute(new PriorityFromFactory());
		}
		TimeUnit.SECONDS.sleep(2);
		exe.shutdownNow();
	}
}