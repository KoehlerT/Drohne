#include "com_koehlert_camera_Camera.h"
#include "camera.h"
#include <iostream>

//Global Vars
CCamera* myCamera;
char* buffer;
int Width;
int Height;
int bufferlength;
int delay;

int getFrame(){
	
	int error = 0;
	do {
		error = myCamera->ReadFrame(0,buffer,bufferlength);
		usleep(delay);
		
	}while(!error);
	return error;
}

/*
 * Class:     com_koehlert_camera_Camera
 * Method:    init
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_com_koehlert_camera_Camera_init
  (JNIEnv *env, jobject thiz, jint width, jint height, jint framerate){
	  
	  std::cout << "width: " << width << " Height: " << height << std::endl;
	  myCamera = StartCamera(width, height, framerate, 1, true);
	 
	 Width = width;
	 Height = height; 
	  
	  //delay = (int)((1.0f/(float)framerate)*1000000);
	  delay = 10;
	  
	  bufferlength = Width * Height * 4;
	  buffer = new char[bufferlength];
}

/*
 * Class:     com_koehlert_camera_Camera
 * Method:    stop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_koehlert_camera_Camera_stop
  (JNIEnv *, jobject){
	  std::cout << "stopping Camera" << std::endl;
	StopCamera();
	delete[] buffer;
}

JNIEXPORT void JNICALL Java_com_koehlert_camera_Camera_getRGBAData
  (JNIEnv *env, jobject thiz, jobject jbuf){
	
	jbyte* buf;
	buf = (jbyte*)env->GetDirectBufferAddress(jbuf);
	
	//for (int i = 0; i<10;i++){
		getFrame();
	//}
	
	
	
	for (int i = 0; i < bufferlength; i++){
		buf[i] = buffer[i];
	}
	
	//memcpy(buf,buffer,bufferlength);
	
}
