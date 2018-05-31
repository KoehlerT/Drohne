package hardware;

import hardware.communication.ComThread;
import utility.Managable;

public class HwStart implements Managable{
	
	private HwThread thread;
	private ComThread communicator;
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		thread = new HwThread();
		communicator = new ComThread();
		thread.start();
		communicator.start();
	}

	@Override
	public boolean running() {
		// TODO Auto-generated method stub
		return (thread.getState() != Thread.State.TERMINATED && 
				communicator.getState() != Thread.State.TERMINATED); //return true wenn Thread ausgeführt wird, false wenn beendet (terminated)
	}

}
