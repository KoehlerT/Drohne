package bilderkennung;
//Imports
import utility.*;

public class BildStart implements Managable {
	/*Typischer Wrapper einer Thread klasse
	 * 
	 * */
	private BildThread bThread;
	
	@Override
	public void start() {
		
		starteThread();
		System.out.println("Bilderkennungsthread wurde gestartet!");
	}
	
	private void starteThread() {
		//Startet nur einen Platzhalter. Finale implementierung Sp�ter
		bThread = new BildThread();
		bThread.start();
	}
	
	@Override
	public boolean running() {
		return (bThread.getState() != Thread.State.TERMINATED); //return true wenn Thread ausgef�hrt wird, false wenn beendet (terminated)
	}

}
