package hardware.communication;

import java.io.IOException;

import main.Daten;
import main.Info;

public class ComThread extends Thread{
	
	private Antenne ant;
	private WlanServer wlanServer;
	private Arduino arduinoMng;
	
	private boolean running = true;
	private long startTime;
	
	public ComThread() {
		this.setName("Communication");
		
		if (Info.sensorAttached) {
			arduinoMng = new Arduino();
		}
		
		wlanServer = new WlanServer();
		
		//ant = new Antenne(); Timeout
		
	}
	
	@Override
	public void run() {
		wlanServer.acceptClients();
		while (Daten.running) {
			startTime = System.nanoTime();
			
			if (Info.sensorAttached) {
				arduinoMng.sendControllerInputs();
			}
			
			//Antenne
			//ant.receive();
			//byte[] toSend = Datapackager.packageTransmit();
			//ant.setTransmitBuffer(toSend);
			//ant.send();
			
			wlanServer.receive();
			wlanServer.sendPackage();
			
			int duration = (int)(System.nanoTime() - startTime);
			Daten.setCommunicatorRefresh((int)(1f/((float)duration/1000_000_0000f))); //in 10Hz
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		wlanServer.closeConnection();
		arduinoMng.closeConnection();
		
	}
	
}
