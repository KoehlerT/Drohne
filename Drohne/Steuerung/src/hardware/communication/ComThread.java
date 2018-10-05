package hardware.communication;

import main.Daten;

public class ComThread extends Thread{
	
	//private Antenne ant;
	private WlanServer wlanServer;
	//private Arduino arduinoMng;
	
	private long startTime;
	
	public ComThread() {
		this.setName("Communication");
		
		wlanServer = new WlanServer();
		
		//ant = new Antenne(); Timeout
		
	}
	
	@Override
	public void run() {
		wlanServer.acceptClients();
		while (Daten.running) {
			startTime = System.nanoTime();
			
			//Antenne
			//ant.receive();
			//byte[] toSend = Datapackager.packageTransmit();
			//ant.setTransmitBuffer(toSend);
			//ant.send();
			
			wlanServer.sendPackage();
			wlanServer.receive();
			
			Daten.setLastComm(System.nanoTime());
			
			int duration = (int)(System.nanoTime() - startTime);
			Daten.setCommunicatorRefresh((int)(1f/((float)duration/1000_000_0000f))); //in 10Hz
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		wlanServer.closeConnection();
		
	}
	
}
