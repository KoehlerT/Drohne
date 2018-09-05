#include "comm.h"
#include <stdio.h>
#include <iostream>
#include <chrono>
#include <unistd.h>

int main(){
	printf("Hello World\n");
	
	int length_TX = 10;
	int length_RX = 10;
	char* transmit  = new char[length_TX];
	char* receive = new char[length_RX];
	
	init(transmit, receive, length_TX, length_RX);
	
	for (int i = 0; i < length_TX; i++){
		transmit[i] = (char)i;
	}
	
	for (int x = 0; x < 100; x++){
		auto start = std::chrono::high_resolution_clock::now();
		transceive();
		auto finish = std::chrono::high_resolution_clock::now();
		
		std::chrono::duration<double> elapsed = finish - start;
		std::cout << "Elapsed time: " << (elapsed.count()*1000) << " ms\n";
		
		for (int i = 0; i < length_RX; i++){
			std::cout << (int)receive[i] << ", ";
		}
		std::cout << std::endl;
		usleep(1000);
	}
	
	delete[] transmit;
	delete[] receive;
	stop();
}
