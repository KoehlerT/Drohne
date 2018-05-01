package hardware;

public class HardwareCommunicator {
	
	//private SerialDevice baseStation;
	private WlanClient wlanClient;
	
	public HardwareCommunicator() {
		//baseStation = new SerialDevice();
		wlanClient = new WlanClient();
	}
	
	public void PrepareAndSend() {
		//byte[] toSend = Datapackager.getTransmitPackage();
		//baseStation.sendData(toSend);
		wlanClient.sendControls();
		wlanClient.receive();
	}
}
