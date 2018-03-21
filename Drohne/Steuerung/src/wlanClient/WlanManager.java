package wlanClient;
import utility.*;

public class WlanManager implements Managable{

	private ClientThread thread;
	
	@Override
	public void start() {
		thread = new ClientThread();
		thread.start();
		
	}

	@Override
	public boolean running() {
		
		return true;
	}

}
