package hardware;
import java.io.IOException;

//import jdk.dio.spibus.SPIDeviceConfig;
import main.Communicator;
class SPIManager {
	/*Code abgeschrieben von:
	 * https://docs.oracle.com/javame/8.0/me-dev-guide/spi.htm
	 * https://blogs.oracle.com/javatraining/using-device-io-with-java-embedded-suite-on-a-raspberry-pi
	 * */
	//SPIDeviceConfig arduino;
	
	public SPIManager() {
		try {
			Communicator c = new Communicator(10,4,200000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
