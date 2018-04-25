package hardware;

public class HardwareCommunicator {
	
	private SerialDevice baseStation;
	
	
	public HardwareCommunicator() {
		baseStation = new SerialDevice();
	}
	
	public void send(byte[] toSend) {
		baseStation.sendData(toSend);
	}
}
