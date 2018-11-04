package hardware.communication;

import hardware.dataHandling.ArduinoData;
import hardware.dataHandling.ArduinoSender;
import hardware.dataHandling.Datapackager;
import main.Daten;
import main.Info;

public class Arduinothread extends Thread{
	
	//private Arduino arduinoMng;
	private SPIComm comm;
	private byte[] Send = new byte[10];
	private byte[] Recv = new byte[10];
	
	public Arduinothread() {
		
		
		this.start();
	}
	
	@Override
	public void run(){
		if (Info.sensorAttached) {
			comm = new SPIComm(); 
		}
		
		long startTime = System.nanoTime();
		long refreshTime = System.nanoTime();
		int cmm;
		int tot;
		int refr;
		
		while (Daten.running) {
			startTime = System.nanoTime();
			if (Info.sensorAttached) {
				ArduinoSender.getTransmitPackage(Send);
				comm.setTransmitBuffer(Send);
				
				refr = (int)((System.nanoTime() - refreshTime)/1000);
				comm.comm();
				refreshTime = System.nanoTime();
				cmm = (int)((System.nanoTime() - startTime)/1000);
				
				comm.getReceived(Recv);
				ArduinoData.getArduinoData(Recv);
			}
			tot = (int)((System.nanoTime() - startTime)/1000);
			if (tot > 4200) {
				System.out.println(cmm+" "+tot);
			}
			if (refr > 1000)
				System.out.println("--------------------------Refresh: "+refr);
			
			
		}
	}
	

}
