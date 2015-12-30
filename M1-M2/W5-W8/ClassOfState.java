import java.util.*;
interface State{
	void act();
}
class Online implements State{
	public void act(){
		System.out.println("您的qq现在是在线状态");
	}
}
class Offline implements State{
	public void act(){
		System.out.println("您的qq现在是离线状态");
	}
}
class Invisible implements State{
	public void act(){
		System.out.println("您的qq现在是隐身状态");
	}
}
class Context{
	private int index=-1;
    private State state;
	private List<State> list=Arrays.asList(
		new Offline(),new Online(),new Invisible());
	public Context(){
		state=new Offline();  
    }
	public Context(State state){
		this.state=state;
    }
	public void setState(State state){
		this.state=state;
	}
	public boolean hasNextState(){
		return index+1<list.size();
	}
	public State nextState(){
		return list.get(++index);
	}
	public void method(){
		state.act();
	}
}
public class ClassOfState{	
	public static void main(String[] args){
		Context context=new Context();
		context.method();
		context.setState(new Online());
		context.method();
		context.setState(new Invisible());
		context.method();

		System.out.println("---------------------");
		while(context.hasNextState()){//还有下一个状态
			context.nextState();//下一个状态
			context.method();
		}
	}
}