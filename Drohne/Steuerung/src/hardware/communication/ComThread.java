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
			try {
				arduinoMng = new Arduino();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		wlanServer = new WlanServer();
		
		//ant = new Antenne(); Timeout
		
	}
	
	@Override
	public void run() {
		wlanServer.acceptClients();
		while (running) {
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
		}
		
	}
	
}
