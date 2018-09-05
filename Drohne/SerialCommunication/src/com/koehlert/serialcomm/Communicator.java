package com.koehlert.serialcomm;

import java.nio.ByteBuffer;



public class Communicator {

	private native int init(ByteBuffer transmitBuffer, ByteBuffer receiveBuffer, int tx_length, int rx_rength);
	private native int transceive();
	private native int stop();
	
	private ByteBuffer TX_Buf;
	private ByteBuffer RX_Buf;
	
	private int TX_length;
	private int RX_length;
	
	public Communicator(int length_rx, int length_tx){
		TX_Buf = ByteBuffer.allocateDirect(length_tx);
		RX_Buf = ByteBuffer.allocateDirect(length_rx);
		
		TX_length = length_tx;
		RX_length = length_rx;
		
		init(TX_Buf, RX_Buf, length_tx, length_rx);
	}
	
	public void setTransmitBuffer(byte[] buffer){
		//TX_Buf.put(buffer);
		for (int i = 0; i < TX_length; i++){
			TX_Buf.put(i, buffer[i]);
		}
	}
	
	public byte[] getReceiveBuffer(){
		byte[] res = new byte[RX_length];
		for (int i = 0; i < RX_length; i++){
			res[i] = RX_Buf.get(i);
		}
		return res;
	}
	
	public byte[] getReceiveBuffer(byte[] res){
		for (int i = 0; i < RX_length; i++){
			res[i] = RX_Buf.get(i);
		}
		return res;
	}
	
	public void communicate(){
		transceive();
	}
	
	public void close(){
		stop();
	}

}
