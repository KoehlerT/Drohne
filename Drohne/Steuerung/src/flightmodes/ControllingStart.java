package flightmodes;

import utility.Managable;

public class ControllingStart implements Managable{

	ControllingThread thread;
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		thread = new ControllingThread();
		thread.start();
	}

	@Override
	public boolean running() {
		// TODO Auto-generated method stub
		return false;
	}

}
