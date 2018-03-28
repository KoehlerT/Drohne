package wlanClient;
import utility.*;

public class WlanManager implements Managable{

	private ClientThread thread;
	private String host;
	
	public WlanManager(String host) {
		this.host = host;
	}
	
	
	@Override
	public void start() {
		thread = new ClientThread(host);
		thread.start();
		
	}

	@Override
	public boolean running() {
		
		return true;
	}

}
