package kamera;

import utility.*;

public class KameraStart implements Managable {

	KameraThread kThread;
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		starteThread();
	}

	@Override
	public boolean running() {
		// TODO Auto-generated method stub
		return (kThread.getState() != Thread.State.TERMINATED); //return true wenn Thread ausgeführt wird, false wenn beendet (terminated)
	}
	
	private void starteThread() {
		kThread = new KameraThread();
		kThread.start();
	}

}
