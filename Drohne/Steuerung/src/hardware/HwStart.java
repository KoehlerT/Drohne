package hardware;

import utility.Managable;

public class HwStart implements Managable{
	
	private HwThread thread;
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		thread = new HwThread();
		thread.start();
	}

	@Override
	public boolean running() {
		// TODO Auto-generated method stub
		return (thread.getState() != Thread.State.TERMINATED); //return true wenn Thread ausgeführt wird, false wenn beendet (terminated)
	}

}
