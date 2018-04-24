package hardware;

import com.fazecast.jSerialComm.*;

public class SerialDevice implements SerialPortDataListener{
	
	SerialPort comPort = null;
	
	SerialDevice(){
		getPort();
		comPort.setBaudRate(57600);
		comPort.openPort();
		comPort.addDataListener(this);
		
	}
	
	
	private void getPort() {
		SerialPort[] ports = SerialPort.getCommPorts();
		for (int i = 0; i< ports.length; i++) {
			String descr = ports[i].getPortDescription();
			if (!descr.equals("Serial0")){
				comPort = ports[i];
			}
		}
		
		if (comPort == null) {
			System.out.println("Kein Validen Port gefunden");
		}else {
			System.out.println("Verbinde mit Port "+comPort.getPortDescription());
		}
	}


	@Override
	public int getListeningEvents() {
		System.out.println("Get listening Events...");
		return 0;
	}


	@Override
	public void serialEvent(SerialPortEvent event) {
		// TODO Auto-generated method stub
		byte[] data = event.getReceivedData();
		System.out.println("Daten Empfangen:");
		System.out.println(data);
	}
}
