enum State{
	ONLINE,OFFLINE,INVISIBLE;
}
class Context{  
    private State state;
	public Context(){
		state=State.OFFLINE;  
    }
	public Context(State state){
		this.state=state;  
    }
	public void setState(State state){
		this.state=state;
	}
	public void method(){
		switch(state){
			case ONLINE:
				System.out.println("您的qq现在是在线状态");
				break;
			case OFFLINE:
				System.out.println("您的qq现在是离线状态");
				break;
			case INVISIBLE:
				System.out.println("您的qq现在是隐身状态");
				break;
			default:
				System.out.println("恭喜，您的qq已经被盗");
		}
	}
}  
public class MethodOfState{
	public static void main(String[] args){
		Context context=new Context();//默认状态，离线
		context.method();
		context.setState(State.ONLINE);//改为在线
		context.method();
		context.setState(State.INVISIBLE);//改为隐身
		context.method();
	}
}