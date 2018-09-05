package flowertracking;

import main.Info;
import utility.Managable;

public class TrackerStart implements Managable{
	
	TrackerThread thread;
	
	@Override
	public void start() {
		if (Info.CamAttached) {
			thread = new TrackerThread();
			thread.start();
		}
	}

	@Override
	public boolean running() {
		if (thread == null)
			return false;
		return thread.getState() != Thread.State.TERMINATED;
	}

}
