package hardware;

import java.io.IOException;

import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.serial.*;

import main.Daten;

public class GPS implements SerialDataEventListener{
	
	private Serial gps;
	private boolean available = false;
	private String buffer = "";
	
	GPS(){
		gps = SerialFactory.createInstance();
		gps.addListener(this);
		
		SerialConfig config = new SerialConfig();
		try {
			config.device(SerialPort.getDefaultPort()).baud(Baud._9600)
			.dataBits(DataBits._8)
			.parity(Parity.NONE)
			.stopBits(StopBits._1)
			.flowControl(FlowControl.NONE);
			
			gps.open(config);
		} catch (UnsupportedBoardType | IOException | InterruptedException e) {
			System.out.println("Problem beim Erstellen des GPS Devices");
			e.printStackTrace();
		}
	}
	

	@Override
	public void dataReceived(SerialDataEvent arg0) {
		// TODO Auto-generated method stub
		try {
			String recv = arg0.getAsciiString();
			buffer += recv; //Empfangenes wird gespeichert
			
			if(buffer.contains("$GPGSA")) {//Interessante Stelle wurde übermittelt
				String interesting = buffer.split("GPGSA")[0].split("GPGGA")[1];
				splitData(interesting);
				buffer = "";
			}
			
		} catch (IOException e) {
			System.out.println("Problem bei: GPS Lesen");
			e.printStackTrace();
		}
	}
	
	private void splitData(String data) {
		String[] parts = data.split(",");
		if (parts[1] == "" || parts[1] == null || parts[1].length() == 0) {
			//Kein GPS empfang
			if (available) {
				available = false;
				Daten.setGpsAvailable(false);
			}
		}else if (parts[1] != ""){
			//Tabelle
			//https://www.engineersgarage.com/embedded/arduino/arduino-gps-interfacing-project-circuit/
			String latStr = parts[2];
			String lonStr = parts[4];
			String satStr = parts[7];
			String altStr = parts[9];
			
			float lat = Float.parseFloat(latStr);
			float lon = Float.parseFloat(lonStr);
			float alt = Float.parseFloat(altStr);
			int sat = Integer.parseInt(satStr);
			
			Daten.setLatitude(lat);
			Daten.setLongitude(lon);
			Daten.setGpsAltitude(alt);
			Daten.setNumGpsSatellites(sat);
			if (!available) {
				available = true;
				Daten.setGpsAvailable(true);
			}
		}
	}
	
	public static void printGps() {
		if (Daten.getGpsAvailable()) {
			System.out.println("Latitude: "+Daten.getLatitude()+" N");
			System.out.println("Longitude: "+Daten.getLongitude()+" E");
			System.out.println("Bei "+Daten.getNumGpsSatellites()+" Satelliten");
		}else {
			System.out.println("Kein GPS Empfang");
		}
		
	}
}
