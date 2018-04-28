package hardware;

public class HardwareCommunicator {
	
	private SerialDevice baseStation;
	private Datapackager packager;
	
	
	public HardwareCommunicator() {
		baseStation = new SerialDevice();
		packager = new Datapackager();
	}
	
	public void PrepareAndSend() {
		byte[] toSend = packager.getTransmitPackage();
		baseStation.sendData(toSend);
	}
}
