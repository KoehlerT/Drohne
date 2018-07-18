package hardware;

public class HardwareCommunicator {
	
	//private SerialDevice baseStation;
	private WlanClient wlanClient;
	
	public HardwareCommunicator() {
		//baseStation = new SerialDevice();
		wlanClient = new WlanClient();
		
		//Sender & Receiver
		new Thread() {
			@Override
			public void run() {
				wlanClient.connect();
				while (true) {
					wlanClient.sendControls();
					wlanClient.receive();
					try {Thread.sleep(100);}catch(InterruptedException e) {e.printStackTrace();}
				}
			}
		}.start();
		
		/*//Receivder
		new Thread() {
			@Override
			public void run() {
				while(true) {
					wlanClient.receive();
				}
				
			}
		}.start();*/
		
		
	}
	
	public void PrepareAndSend() {
		//byte[] toSend = Datapackager.getTransmitPackage();
		//baseStation.sendData(toSend);
		
	}
}
