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
		
		
		while (Daten.running) {
			if (Info.sensorAttached) {
				ArduinoSender.getTransmitPackage(Send);
				comm.setTransmitBuffer(Send);
				comm.comm();
				comm.getReceived(Recv);
				ArduinoData.getArduinoData(Recv);
			}
			
			
			/*try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	

}
