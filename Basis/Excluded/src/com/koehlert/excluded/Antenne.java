package com.koehlert.excluded;

import java.io.IOException;

import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.*;

public class Antenne {
	//Klasse zum Kontrollieren des nRF905
	
	/*Pin Layout:
	 * GPIO 18 (01): DR - Input
	 * GPIO 17 (00): PWR - Output
	 * GPIO 27 (02): CE - Output
	 * GPIO 22 (03): TxEN - Output
	 * 
	 * GPIO 8: CSN - Output
	 * */
	
	private GpioPinDigitalOutput PWR;
	private GpioPinDigitalOutput CE;
	private GpioPinDigitalOutput TXE;
	
	private GpioPinDigitalInput DR;
	
	private int SpiSpeed = 1000000;
	private SpiDevice spi;
	
	private boolean running = true;
	private long timeout = 1000000000l; //1s
	
	private final byte transmitCommand = 0b00100000; //W_TX_PAYLOAD
	private byte[] transmitBuffer = new byte[33]; //transmitBuffer[0] = Write TX-Buffer Command
	
	private final byte[] emptyReceive = new byte[33];//Receive
	
	public Antenne() {
		transmitBuffer[0] = transmitCommand;
		emptyReceive[0] = 0b0010_0100;
		
		GpioController contr = GpioFactory.getInstance();
		
		PWR = contr.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Power",PinState.LOW);
		CE = contr.provisionDigitalOutputPin(RaspiPin.GPIO_02, "CE",PinState.LOW);
		TXE = contr.provisionDigitalOutputPin(RaspiPin.GPIO_03,"TX_Enable",PinState.LOW);
		
		DR = contr.provisionDigitalInputPin(RaspiPin.GPIO_01,"Data Ready");
		
		try {
			spi = SpiFactory.getInstance(SpiChannel.CS0, SpiSpeed, SpiDevice.DEFAULT_SPI_MODE);
		} catch (IOException e) {
			System.out.println("SPI Antenne Fehler");
			running = false;
			e.printStackTrace();
		}
		
		configure();
		setTransmitAddress();
	}
	
	public void pwrUp() {
		PWR.high();
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void pwrDown() {
		PWR.high();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send() {
		if (PWR.isLow())
			pwrUp();
		TXE.high();
		CE.low();
		
		try {Thread.sleep(1);
		} catch (InterruptedException e1) {e1.printStackTrace();}
		
		try {
			long starttime = System.nanoTime();
			//Set Transmit Register
			spi.write(transmitBuffer);
			CE.high(); //SEND
			
			//Wait for Transmission Ending
			while(DR.isLow());
			CE.low(); //End this Transmission
			
			System.out.println("Data has been sent");
			System.out.println("in "+((System.nanoTime()-starttime)/1000000)+" ms");
		} catch (IOException e) {
			System.out.println("Transmit Register Set Error");
			e.printStackTrace();
		}
	}
	
	public void setTransmitBuffer(byte[] toSend) {
		for (int i = 0; i < 32; i++) {
			if (i < toSend.length)
				transmitBuffer[i+1] = toSend[i];
			else
				transmitBuffer[i+1] = 0;
			
		}
	}
	
	public void receive() {
		if (PWR.isLow())
			pwrUp();
		TXE.low();
		CE.high();
		
		try {Thread.sleep(1);
		} catch (InterruptedException e1) {e1.printStackTrace();}
		long startTime = System.nanoTime();
		
		System.out.println("Waiting for data");
		while(DR.isLow()) {
			 if ((System.nanoTime()-startTime) > timeout) {
				 System.out.println("Receiving TIMED OUT:" +(System.nanoTime()-startTime));
				 return;
			 }
		}
		getRxData();
	}
	
	
	private void getRxData() {
		
		try {
			byte[] payload = spi.write(emptyReceive);
			Datapackager.untangleReceived(payload);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	//------------------------------Konfiguration
	public void setTransmitAddress() {
		byte[] toSend = new byte[5];
		toSend[0] = 0b00100010; //Command (Set Transmit Address)
		toSend[1] = (byte)0x95;// Byte 0 TX Address
		toSend[2] = (byte)0x6B;// Byte 1 ...
		toSend[3] = (byte)0xCC;
		toSend[4] = (byte)0xB6;
		try {
			spi.write(toSend);
			
			//Check
			System.out.println("Transmit address:");
			checkContent(4,(byte)0b00100011);
		} catch (IOException e) {
			System.out.println("Antenna Error: Transmit set");
			e.printStackTrace();
		}
	}
	
	private void checkContent(int size, byte command) {
		byte[] toSend = new byte[size+1];
		toSend[0] = command;
		
		try {
			byte[] recv = spi.write(toSend);
			
			for (int i = 0; i < recv.length; i++) {
				byte content = recv[i];
				String bin = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
				System.out.println("Content "+i+": "+bin);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void configure() {
		if (!running)
			return;
		try {Thread.sleep(20);
		} catch (InterruptedException e1) {e1.printStackTrace();}
		
		
		try {
			//Frequenz: 100 (Dec) 0x64 (Hex) => 8..MHz
			//Freq: 116 0x74 => 434MHz
			writeConfRegister((byte)0x0,(byte)0x74);
			
			//HFREQ_PLL: 1
			//Output Power: -10dBm PA_PWR: 00
			//Config byte 1: [7:6] none, AUTO_RETR, RX_RED_PWR, PA_PWR[1:0] (10dBm), FREQ_PLL (=0: 434MHz), 8th bit CH_NO 
			byte b1 = 0b00001100;
			writeConfRegister((byte)0x1,b1);
			
			//RX-address width: 100 (4 bytes) RX_AFW: 100
			//TX-address width: 100 (4bytes) TX_AFW: 100
			//7 none, TX_AFW[2:0], 3 none, RX_AFW[2:0]
			byte b2 = 0b01000100;
			writeConfRegister((byte)0x2,b2);
			
			//RX Payload width: 32 bytes RX_PW: 100000
			writeConfRegister((byte)0x3,(byte)0x20);
			
			//TX Payload width: 32 bytes TX_PW: 100000
			writeConfRegister((byte)0x4,(byte)0x20);
			
			//RX Address 32 Bit: 0xDC8CEA72
			writeConfRegister((byte)0x5,(byte)0xDC);
			writeConfRegister((byte)0x6, (byte)0x8C);
			writeConfRegister((byte)0x7,(byte)0xEA);
			writeConfRegister((byte)0x8,(byte)0x72);
			
			//No External Clock UP_CLK_FREQ: 0
			//CRC_MODE: 1; CRC_EN: 1; XOF: 011; UP_CLK_EN: 0; UP_CLK_FREQ: 00
			writeConfRegister((byte)0x9,(byte)0b11011000);
			
			System.out.println("Checking registers");
			for (int i = 0; i <= 9; i++) {
				byte content = readConfRegister((byte)i);
				String bin = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
				System.out.println("Config 0x"+i+": "+bin);
			}
			
		}catch (IOException e) {
			System.out.println("Configuration Fehler");
			e.printStackTrace();
		}
		
		
	}
	
	private void writeConfRegister(byte regNum, byte content) throws IOException {
		byte instr = (byte) (regNum & 0b00001111); //Instruction = 0000AAAA (AAAA => Address)
		spi.write(new byte[] {instr,content});
	}
	
	private byte readConfRegister(byte regNum) throws IOException {
		byte instr = (byte)(regNum & 0b00001111); // Instruction = 0001AAAA (AAAA => Address)
		instr = (byte)(instr | 0b00010000);
		
		byte[] res = spi.write(new byte[] {instr,0b0});
		return res[1];
		
	}
	
}
