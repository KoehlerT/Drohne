//Compile Command:
//sudo g++ -Wall -shared -fPIC -o /lib/drohne/SPICommunication.so hardware_communication_SPIComm.cpp SPI.cpp -I /usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include -I /usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include/linux


#include "SPI.h"
#include "hardware_communication_SPIComm.h"
#include <iostream>
#include <wiringPi.h>


jbyte* bb_receive;
jbyte* bb_transmit;
int bSize;
int uSleepTime;

JNIEXPORT jint JNICALL Java_hardware_communication_SPIComm_init
  (JNIEnv *env, jobject thiz, jobject bbIn, jobject bbOut, jint spd, jint bufferSize, jint nanoSleep){
    std::cout << "INIT" << std::endl;
    bb_receive = (jbyte*)(env)->GetDirectBufferAddress(bbIn);
    bb_transmit = (jbyte*)(env)->GetDirectBufferAddress(bbOut);
    bSize = bufferSize;
    uSleepTime = nanoSleep;

    int res = init(spd);
    return res;
}



 JNIEXPORT void JNICALL Java_hardware_communication_SPIComm_transmit
    (JNIEnv *env, jobject thiz){

        shakeHands(uSleepTime);
        for (int i = 0; i < bSize; i++){
            digitalWrite(16,HIGH);
            usleep(uSleepTime);
            bb_receive[i] = spiTxRx(bb_transmit[i]);

        }
}
