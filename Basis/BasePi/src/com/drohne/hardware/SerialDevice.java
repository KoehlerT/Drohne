package com.drohne.hardware;

import java.io.IOException;

import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPort;
import com.pi4j.io.serial.StopBits;

public class SerialDevice implements SerialDataEventListener{
	
	Serial serial;
	
	public SerialDevice() {
		serial = SerialFactory.createInstance();
		openPort();
		serial.addListener(this);
	}
	
	public void write (byte[] data) {
		try {
			//serial.write(data);
			serial.write("HI");
			//serial.write('\r');
			//serial.write('\n');
			
			System.out.println("Data written");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void openPort() {
		
		SerialConfig config = new SerialConfig();
		
		try {
			System.out.println("Default Port "+SerialPort.getDefaultPort());
			
			config.device(/*SerialPort.getDefaultPort()*/"/dev/ttyS0")
				.baud(Baud._9600)
				.dataBits(DataBits._8)
				.parity(Parity.NONE)
				.stopBits(StopBits._1)
				.flowControl(FlowControl.NONE);
			
			serial.open(config);
		
		} catch (UnsupportedBoardType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void dataReceived(SerialDataEvent arg0) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Empfangen ");
			System.out.println(arg0.getBytes());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
