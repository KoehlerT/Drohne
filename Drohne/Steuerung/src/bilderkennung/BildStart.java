package bilderkennung;
//Imports
import utility.*;

public class BildStart implements Managable {
	private BildThread bThread;
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		starteThread();
	}
	
	private void starteThread() {
		bThread = new BildThread();
		bThread.start();
	}
	
	@Override
	public boolean running() {
		return (bThread.getState() != Thread.State.TERMINATED); //return true wenn Thread ausgeführt wird, false wenn beendet (terminated)
	}

}
