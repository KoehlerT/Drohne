package bilderkennung;
//Imports
import utility.*;

public class BildStart implements Managable {
	private BildThread bThread;
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		starteThread();
		System.out.println("Bilderkennungsthread wurde gestartet!");
	}
	
	private void starteThread() {
		//Startet nur einen Platzhalter. Finale implementierung Später
		bThread = new BildThread();
		bThread.start();
	}
	
	@Override
	public boolean running() {
		return (bThread.getState() != Thread.State.TERMINATED); //return true wenn Thread ausgeführt wird, false wenn beendet (terminated)
	}

}
