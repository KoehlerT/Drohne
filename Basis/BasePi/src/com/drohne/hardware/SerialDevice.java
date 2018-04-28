package com.drohne.hardware;

import java.io.IOException;

import com.drohne.main.StartBase;
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
	RFModule antenna;
	
	byte[] receivedComp = new byte[8];
	int countRecvComp = 0;
	
	byte lastByte = 0;
	
	public void write (byte[] data) {
		try {
			serial.write(data);
			
			System.out.println("Data written");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void dataReceived(SerialDataEvent event) {
		try {
			byte[] empf = event.getBytes();
			System.out.println("Empfangen: "+empf.length+" count "+countRecvComp);
			for (int i = 0; i < empf.length;i++) {
				
				byte thisByte = empf[i];
				if (countRecvComp < receivedComp.length) {
					receivedComp[countRecvComp] = thisByte;
					System.out.println((countRecvComp)+": "+thisByte);
					countRecvComp++;
				}else {
					System.out.println("Verworfen: "+thisByte);
				}
					
				
				if (lastByte == (byte)0b01001010 && thisByte == (byte)0b10010110) {
					System.out.println("STOP");
					packageReady();
				}else {
					lastByte = thisByte;
				}
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void packageReady(){
		StartBase.getAntenna().setTxRegister(receivedComp);
		System.out.println("Daten Empfangen + in TX Geschrieben "+receivedComp.length);
		printBinaryArray(receivedComp);
		countRecvComp = 0;
	}
	
	//-------------------------------INIT--------------------------
		public SerialDevice() {
			serial = SerialFactory.createInstance();
			openPort();
			serial.addListener(this);
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
		
		
		private void printBinaryArray(byte[] bin) {
			for (int i = 0; i < bin.length; i++) {
				byte content = bin[i];
				String str = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
				System.out.println("Array ["+i+"]: "+str);
			}
		}
}
