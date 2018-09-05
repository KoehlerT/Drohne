#include <stdio.h>
#include <unistd.h>
#include "camera.h"
#include <iostream>

CCamera* myCamera;

void getFrame(char * buffer, int size);

//entry point
int main(int argc, const char **argv)
{
	std::cout << "Hello Camera" << std::endl;
	myCamera = StartCamera(512,512,30,1,true);
	
	char buffer[512*512*4];
	
	for (int i = 0; i < 10; i++){
		//Read frame
		getFrame(buffer,sizeof(buffer));

		//std::cout << "Return value: " << error << std::endl;
		for (int i = 0; i < 30; i++){
			std::cout << (int)buffer[i] << ", ";
		}
		std::cout << std::endl;
	}
	
	std::cout << "will stop camera" << std::endl;
	StopCamera();
	
}

void getFrame(char* buffer, int size){
	
	int error = 0;
	do {
		error = myCamera->ReadFrame(0,buffer,size);
		usleep(10);
		
	}while(!error);
	
}
