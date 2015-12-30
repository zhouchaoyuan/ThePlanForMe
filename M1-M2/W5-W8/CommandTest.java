//最终执行命令的CPU
class CPU{
	public void execute(){//执行命令
		System.out.println("CPU接收到指令，正在执行...");
	}
}
//电脑用来传递命令给CPU
class Computer{
	private CPU cpu;//设定接受者是cpu
	Computer(CPU cpu){
		this.cpu=cpu;
	}
	public void transmit(){//传递命令
		cpu.execute();
	}
}

interface Command{
	void execute();
}
//实现接口的具体命令
class ConcreteCommand implements Command{
	private Computer computer;//设定传递者是电脑
    ConcreteCommand(Computer com){
        computer=com;
    }
    public void execute(){//编码发送命令
        computer.transmit();
    }
}
//程序猿
class Coder{
	private ConcreteCommand con;
	Coder(ConcreteCommand con){
		this.con=con;
	}
	public void send(){//编码发送命令
		con.execute();
	}
}

public class CommandTest{
	public static void main(String[] args){
		Coder coder=new Coder(new ConcreteCommand(
			new Computer(new CPU())));
		coder.send();//编码
	}
}