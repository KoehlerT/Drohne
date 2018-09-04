package hardware;
/*Quelle:
 * git clone https://github.com/adafruit/Adafruit-Raspberry-Pi-Python-Code.git
cd Adafruit-Raspberry-Pi-Python-Code/
git checkout 9ff733d59242a02f7ccd0222001ce80f6090a978 
cd Adafruit_BMP085
 * */

import java.io.IOException;
import java.util.Arrays;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.util.Console;

import main.Daten;

public class Altitude{
	
	public static final int I2C_ADDR = 0x77;
	
	private I2CDevice device;
	
	//Konstanten
	//Operating Modes
	public static final int MODE_ULTRAPOWER = 0;
	public static final int MODE_STANDARD = 1;
	public static final int MODE_HIGHRES = 2;
	public static final int MODE_ULTRAHIGHRES = 3;
	
	//Registers
	private static final short CAL_AC1 = 0xAA;
	private static final short CAL_AC2 = 0xAC;
	private static final short CAL_AC3 = 0xAE;
	private static final short CAL_AC4 = 0xB0;
	private static final short CAL_AC5 = 0xB2;
	private static final short CAL_AC6 = 0xB4;
	private static final short CAL_B1 =  0xB6;
	private static final short CAL_B2 =  0xB8;
	private static final short CAL_MB =  0xBA;
	private static final short CAL_MC =  0xBC;
	private static final short CAL_MD =  0xBE;
	
	private static final short CONTROL = 0xF4;
	private static final short TEMPDATA = 0xF6;
	private static final short PRESSUREDATA = 0xF6;
	private static final short READTEMPCMD = 0x2E;
	private static final short READPRESSURECMD = 0x34;
	
	//Private fields
	private int cal_AC1 = 0;
	private int cal_AC2 = 0;
	private int cal_AC3 = 0;
	private int cal_AC4 = 0;
	private int cal_AC5 = 0;
	private int cal_AC6 = 0;
	private int cal_B1 = 0;
	private int cal_B2 = 0;
	private int cal_MB = 0;
	private int cal_MC = 0;
	private int cal_MD = 0;
	
	private int Mode;
	
	private double altitude_old = 0;
	
	public Altitude(int mode) {
		Mode = mode;
		//findAvailableBusses();
		try {
			I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
			device = bus.getDevice(I2C_ADDR);
			
			readCalibrationData();
			//printCalibrationData();
			System.out.println("Temp: "+readTemperature()+"°C");
			System.out.println("Pressure: "+readPressure()+" Pa");
			System.out.println("Altitude: "+readAltitude()+" m");
			
		} catch (UnsupportedBusNumberException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Public
	
	public void readAllSensorData() {
		Daten.setPrsAltitude(readAltitude());
		Daten.setTemperature(readTemperature());
		Daten.setPressure(readPressure());
	}
	
	public float readTemperature() {
		int UT = 0;
		int X1 = 0;
		int X2 = 0;
		int B5 = 0;
		float temp = 0f;
		
		UT = readRawTemperature();
		if (UT == -1)
			System.out.println("FEHLER!");
		
		X1 = ((UT - cal_AC6) * cal_AC5) >> 15;
		X2 = (cal_MC << 11) / (X1 + cal_MD);
		B5 = X1 + X2;
		
		temp = (float)((B5+8)>>4)/10.0f;
		
		return temp;
	}
	
	public float readPressure() {
		//Gets Compensated pressure in pascal
		int UT = 0;
		int UP = 0;
		int B3 = 0;
		int B5 = 0;
		int B6 = 0;
		int X1 = 0;
		int X2 = 0;
		int X3 = 0;
		int p = 0;
		int B4 = 0;
		int B7 = 0;
		
		UT = readRawTemperature();
		UP = readRawPressure();
		if (UT == -1 || UP == -1)
			System.out.println("FEHLER!");
		
		//Temperatur
		X1 = ((UT-cal_AC6)*cal_AC5)>>15;
		X2 = (cal_MC << 11) / (X1 + cal_MD);
		B5 = X1 + X2;
		
		//Druck
		B6 = B5 - 4000;
		X1 = (cal_B2*(B6*B6)>>12)>>11;
		X2 = (cal_AC2 * B6) >> 11;
		X3 = X1 + X2;
		B3 = (((cal_AC1*4+X3)<<Mode)+2)/4;
		
		X1 = (cal_AC3 * B6) >> 13;
		X2 = (cal_B1 * ((B6*B6)>>12))>>16;
		X3 = ((X1 + X2)+2)>>2;
		B4 = (cal_AC4 * (X3+32768)) >> 15;
		B7 = (UP - B3)* (50000 >> Mode);
		
		if (B7 < 0x80000000)
			p = (B7*2)/B4;
		else
			p = (B7 / B4) * 2;
		
		X1 = (p>>8)*(p>>8);
		X1 = (X1*3038)>>16;
		X2 = (-7357*p) >> 16;
		
		p = p + ((X1+X2+3791)>>4);
		
		return p;
		
	}
	
	public float readAltitude() {
		return readAltitude(101325);
	}
	
	public float readAltitude(int seaLevelPressure) {
		double altitude = 0f;
		double pressure = (float)readPressure();
		altitude = 44330.0 * (1.0 - Math.pow(pressure/seaLevelPressure, 0.1903));
		
		if (altitude_old == 0)
			altitude_old = altitude;
		
		altitude = (altitude * 0.5) + (altitude_old * 0.5);
		
		return (float)altitude;
	}
	//Helper
	
	private void readCalibrationData() {
		try {
			cal_AC1 = readSigned(CAL_AC1);
			cal_AC2 = readSigned(CAL_AC2);
			cal_AC3 = readSigned(CAL_AC3);
			cal_AC4 = readUnsigned(CAL_AC4);
			cal_AC5 = readUnsigned(CAL_AC5);
			cal_AC6 = readUnsigned(CAL_AC6);
			cal_B1 = readSigned(CAL_B1);
			cal_B2 = readSigned(CAL_B2);
			cal_MB = readSigned(CAL_MB);
			cal_MC = readSigned(CAL_MC);
			cal_MD = readSigned(CAL_MD);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException");
			e.printStackTrace();
		}
	}
	
	private int readRawTemperature() {
		try {
			device.write(CONTROL, (byte)READTEMPCMD);
			Thread.sleep(5);
			byte[] buffer = new byte[2];
			device.read(TEMPDATA, buffer, 0, 2);
			int rawData = (buffer[0] << 8) + buffer[1];
			return rawData;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
		
	}
	
	private int readRawPressure() {
		try {
			device.write(CONTROL, (byte) ((byte)READPRESSURECMD+(Mode << 6)));
			
			if (Mode == MODE_ULTRAPOWER)
				Thread.sleep(5);
			else if (Mode == MODE_HIGHRES)
				Thread.sleep(14);
			else if (Mode == MODE_ULTRAHIGHRES)
				Thread.sleep(26);
			else
				Thread.sleep(8);
			
			int msb = device.read(PRESSUREDATA);
			int lsb = device.read(PRESSUREDATA+1);
			int xlsb = device.read(PRESSUREDATA+2);
			
			int raw = ((msb<<16)+(lsb << 8) + xlsb) >> (8-Mode);
			
			return raw;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private int readSigned(short register) throws IOException {
		int result = 0;
		int high = device.read(register);
		if (high > 127)
			high -=256;
		int low = device.read(register+1);
		
		result = high;
		result = (result << 8) + (low );
		return result;
	}
	
	private int readUnsigned(short register) throws IOException {
		int result = 0;
		int high = device.read(register);
		int low = device.read(register+1);
		
		result = high /*& 0x00000011*/;
		result = (result << 8) + (low );
		return result;
	}
	
	
	//Debug
	
	private void printCalibrationData() {
		System.out.println("Calibration:");
		System.out.println("AC1: "+cal_AC1);
		System.out.println("AC2: "+cal_AC2);
		System.out.println("AC3: "+cal_AC3);
		System.out.println("AC4: "+cal_AC4);
		System.out.println("AC5: "+cal_AC5);
		System.out.println("AC6: "+cal_AC6);
		System.out.println("B1: "+cal_B1);
		System.out.println("B2: "+cal_B2);
		System.out.println("MB: "+cal_MB);
		System.out.println("MC: "+cal_MC);
		System.out.println("MD: "+cal_MD);
		
		
	}
	
	private void findAvailableBusses() {
		try {
            int[] ids = I2CFactory.getBusIds();
            System.out.println("Found follow I2C busses: " + Arrays.toString(ids));
        } catch (IOException exception) {
        	System.out.println("I/O error during fetch of I2C busses occurred");
        }

        // find available busses
        for (int number = I2CBus.BUS_0; number <= I2CBus.BUS_17; ++number) {
            try {
                @SuppressWarnings("unused")
				I2CBus bus = I2CFactory.getInstance(number);
                System.out.println("Supported I2C bus " + number + " found");
            } catch (IOException exception) {
            	System.out.println("I/O error on I2C bus " + number + " occurred");
            } catch (UnsupportedBusNumberException exception) {
            	System.out.println("Unsupported I2C bus " + number + " required");
            }
}
	}
}
