import java.util.*;
public class GreenhouseController
{
	public static void main(String[] args){
		GreenhouseControlls gc=new GreenhouseControlls();
		gc.addEvent(gc.new Bell(90000000));
		Event[] eventList={
			gc.new LightOn(0),
			gc.new LightOff(40000000)
		};
		gc.addEvent(gc.new Restart(200000000,eventList));
//		System.out.println(gc.get().size());//4
		gc.addEvent(new GreenhouseControlls.Terminate(500000000));		
		gc.run();
	}
}
abstract class Event
{
	private long eventTime;
	protected final long delayTime;
	public Event(long delayTime){
		this.delayTime=delayTime;
		start();
	}
	public void start(){
		eventTime=System.nanoTime()+delayTime;
	}
	public boolean ready(){
		return System.nanoTime()>=eventTime;
	}
	public abstract void action();
}
class Controller
{
	private List<Event> eventList=new ArrayList<Event>();
	public List<Event> get(){return eventList;}
	public void addEvent(Event c){
		eventList.add(c);
	}
	public void run(){
		while(eventList.size()>0){
			for(Event e:new ArrayList<Event>(eventList)){
				if(e.ready()){
					System.out.println(e);
					e.action();
					eventList.remove(e);
				}
			}
			/*
			System.out.println("-----------------");
			//for(Event e:eventList){
			//	System.out.println(e);
			//}
			System.out.println("~~~~~~~~~~~~~~~~~");
			*/
		}
	}
}
class GreenhouseControlls extends Controller
{
	private boolean light=false;
	public class LightOn extends Event
	{
		public LightOn(long delayTime){super(delayTime);}
		public void action(){
			light=true;
		}
		public String toString(){
			return "Light is On!";
		}
	}
	public class LightOff extends Event
	{
		public LightOff(long delayTime){super(delayTime);}
		public void action(){
			light=false;
		}
		public String toString(){
			return "Light is Off!";
		}
	}

	public class Bell extends Event
	{
		public Bell(long delayTime){super(delayTime);}
		public void action(){
			addEvent(new Bell(delayTime));
		}
		public String toString(){
			return "Bing!";
		}
	}

	public class Restart extends Event
	{
		private Event[] eventList;
		public Restart(long delayTime,Event[] eventList){
			super(delayTime);
			this.eventList=eventList;
			for(Event e:eventList){
				addEvent(e);
			}
//			System.out.println("zhouchaoyuan: "+get().size());
		}
		public void action(){
			for(Event e:eventList){
				start();
				addEvent(e);
			}
			start();
			addEvent(this);
		}
		public String toString(){
			return "Restart System!";
		}
	}
	public static class Terminate extends Event
	{
		public Terminate(long delayTime){
			super(delayTime);
		}
		public void action(){
			System.exit(0);
		}
		public String toString(){
			return "Terminating!";
		}
	}
}
/*
Light is On!
Light is Off!
Bing!
Bing!
Restart System!
Light is On!
Light is Off!
Bing!
Bing!
Restart System!
Light is On!
Light is Off!
Bing!
Terminating!
*/