package com.drohne.rf;

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

public class RFModule {
	
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
	
	//Send and Receiving data
	
	private final byte RXcommand = 0b0010_0011;
	private final byte[] emptyReceive = new byte[33];
	
	private byte[] txAddress = new byte[] {(byte)0xDC,(byte)0x8C,(byte)0xEA,(byte)0x72};
	private byte[] transmit = new byte[33];
	private byte[] receive = new byte[33]; //Garbage [0], payload[1:32]
	
	public RFModule() {
		emptyReceive[0] = RXcommand;
		transmit[0] = (byte)0b00100000;
		
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
		setTxAddress();
	}
	
	public void pwrUp() {
		PWR.high();
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			System.out.println("Problem nach Antenne Power Up");
			e.printStackTrace();
		}
	}
	
	public void pwrDown() {
		PWR.low();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			System.out.println("Problem nach Antenne Power Down");
			e.printStackTrace();
		}
	}
	
	public void receive() {
		if (PWR.isLow())
			pwrUp();
		TXE.low();
		CE.high();
		
		try {Thread.sleep(1);
		} catch (InterruptedException e1) {e1.printStackTrace();}
		
		System.out.println("Waiting for incoming Data");
		while(DR.isLow()); //Waiting for incoming Data
		
		//Get Payload
		getPayload();
	}
	
	public void send() {
		if (PWR.isLow())
			pwrUp();
		CE.low();
		TXE.high();
		
		try {Thread.sleep(1);} 
		catch (InterruptedException e) {e.printStackTrace();}
		
		try {spi.write(transmit);}
		catch (IOException e) {e.printStackTrace();}
		System.out.println("Sending");
		//Send
		CE.high();
		while(DR.isLow());
		CE.low();
		System.out.println("Data has been sent");
	}
	
	
	public void setTxRegister(byte[] content) {
		for (int i = 0; i < 32; i++) {
			transmit[i+1] = content[i];
		}
	}
	
	private void getPayload() {
		System.out.println("Getting payload");
		try {
			receive = spi.write(emptyReceive);
			
			System.out.println("Received");
			printBinaryArray(receive);
		} catch (IOException e) {
			System.out.println("Fehler beim Epfangen");
			e.printStackTrace();
		}
		
	}
	
	
	private void setTxAddress() {
		byte[] toSend = new byte[5];
		toSend[0] = (byte)0b00100010;
		for (int i = 0; i < 4; i++) {
			toSend[i+1] = txAddress[i];
		}
		
		try {
			spi.write(toSend);
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
			//Frequenz: 100 (Dec) 0x64 (Hex)
			writeConfRegister((byte)0x0,(byte)0x64);
			
			//HFREQ_PLL: 1
			//Output Power: -10dBm PA_PWR: 00
			//Config byte 1: [7:6] none, RX_RED_PWR, PA_PWR[1:0], FREQ_PLL, 8th bit CH_NO 
			byte b1 = 0b00000010;
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
			
			//RX Address 32 Bit: 0x956BCCB6
			writeConfRegister((byte)0x5,(byte)0x95);
			writeConfRegister((byte)0x6, (byte)0x6B);
			writeConfRegister((byte)0x7,(byte)0xCC);
			writeConfRegister((byte)0x8,(byte)0xB6);
			
			//No External Clock UP_CLK_FREQ: 0
			//CRC_MODE: 1; CRC_EN: 1; XOF: 011; UP_CLK_EN: 0; UP_CLK_FREQ: 11
			writeConfRegister((byte)0x9,(byte)0b11011011);
			
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
	
	private void printBinaryArray(byte[] bin) {
		for (int i = 0; i < bin.length; i++) {
			byte content = bin[i];
			String str = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
			System.out.println("Array ["+i+"]: "+str);
		}
	}
	
	private void checkContent(int size, byte command) {
		byte[] toSend = new byte[size+1];
		toSend[0] = command;
		
		try {
			byte[] recv = spi.write(toSend);
			printBinaryArray(recv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
}
