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
		//SerialPort[] ports = SerialPort.getCommPorts();
		SerialPort p = SerialPort.getCommPort("COM3");
		System.out.println("P: "+p.getDescriptivePortName());
		/*for (int i = 0; i< ports.length; i++) {
			String descr = ports[i].getPortDescription();
			if (!descr.equals("Serial0")){
				comPort = ports[i];
			}
		}*/
		comPort = p;
		
		if (comPort == null) {
			System.out.println("Kein Validen Port gefunden");
		}else {
			System.out.println("Verbinde mit Port "+comPort.getPortDescription());
		}
	}

	
	public void sendData(byte[] toSend) {
		comPort.writeBytes(toSend, toSend.length);
	}

	@Override
	public int getListeningEvents() {
		System.out.println("Get listening Events...");
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}


	@Override
	public void serialEvent(SerialPortEvent event) {
		// TODO Auto-generated method stub
		if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
	         return;
		
		System.out.println("Daten Empfangen:");
		byte[] data = new byte[comPort.bytesAvailable()];
		int numRead = comPort.readBytes(data, data.length);
		System.out.println("Read: "+numRead+" bytes: ");
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i]+", ");
		}
		System.out.println();
	}
}
