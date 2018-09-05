#include "comm.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>
#include <iostream>

int uart0_filestream = -1;
char* transmitBuff;
char* receiveBuff;
int TX_length;
int RX_length;


int init(char* _transmitBuff, char* _receiveBuff, int _TX_length, int _RX_length){
	printf("init\n");
	
	//vars
	transmitBuff = _transmitBuff;
	receiveBuff = _receiveBuff;
	TX_length = _TX_length;
	RX_length = _RX_length;
	
	
	//init
	uart0_filestream = -1;
	uart0_filestream = open("/dev/serial0", O_RDWR | O_NOCTTY | O_NDELAY);
	if (uart0_filestream == -1) {
		printf("[ERROR] UART open()\n");
		return -1;
	}

	struct termios options;
	tcgetattr(uart0_filestream, &options);
		options.c_cflag = B230400 | CS8 | CLOCAL | CREAD; 
		options.c_iflag = IGNPAR;
		options.c_oflag = 0;
		options.c_lflag = 0;
	
	
	tcflush(uart0_filestream, TCIFLUSH);
	tcsetattr(uart0_filestream, TCSANOW, &options);
	
	
	
	
	return 0;
	
}

int transceive(){
	blockForHandshake();
	
	//Bytes Empfangen
	if (uart0_filestream != -1)	{
		int out = write(uart0_filestream, &transmitBuff[0], TX_length); // 
		if (out < 0) {
			printf("[ERROR] UART TX\n");
			return -1;
		} else {
			//printf("[STATUS: TX %i Bytes] %s\n", out, transmitBuff);
		}
	} // if uart0
	
	usleep(600);
	
	
	char Tmp_Buf[RX_length + 5];
	// Bytes empfangen
	if (uart0_filestream != -1) {
		
		int rx_length = read(uart0_filestream, (void*)Tmp_Buf, RX_length+5);

		if (rx_length < 0) {
			printf("[ERROR] UART RX\n");
			return -1;
		} else if (rx_length == 0) {
			printf("[ERROR] UART RX - no data\n");
			return -2;
		} else {
			//Okay
			
		} //rx_length check
	} //if uart0
	
	int indBuf = 0;
	for (int i = 0; (i < RX_length + 5 && indBuf < RX_length);i++){
		if (!(indBuf == 0 && Tmp_Buf[i] == (char)65)){
			receiveBuff[indBuf] = Tmp_Buf[i];
			indBuf++;
		}
	}
	
	return 0;
}

void blockForHandshake(){
	bool handshake = false;
	char message[2]{'R','Q'};
	char rx_buf[2];
	if (uart0_filestream != -1){
		while (!handshake){
			write(uart0_filestream, message, 2);
			usleep(500);
			int rx_read = read(uart0_filestream, (void*)rx_buf, 1);
			
			handshake = ((rx_read >= 1));
			
		}
		
	}
	
}

void stop(){
	printf("close Serial Communication\n");
	close(uart0_filestream);
}
