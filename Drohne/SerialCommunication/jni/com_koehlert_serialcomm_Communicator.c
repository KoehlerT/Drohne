#include <jni.h>
#include "com_koehlert_serialcomm_Communicator.h"
#include "comm.h"


int length_RX;
int length_TX;
char* RX_Buffer;
char* TX_Buffer;

JNIEXPORT jint JNICALL Java_com_koehlert_serialcomm_Communicator_init
  (JNIEnv *env, jobject thiz, jobject _tx_buf, jobject _rx_buf, jint _tx_len, jint _rx_len){
	  
	  RX_Buffer = (char*)env->GetDirectBufferAddress(_rx_buf);
	  TX_Buffer = (char*)env->GetDirectBufferAddress(_tx_buf);
	  
	  length_RX = _rx_len;
	  length_TX = _tx_len;
	  
	  int error = init(TX_Buffer, RX_Buffer, length_TX, length_RX);
	  
	  return error;
  }


JNIEXPORT jint JNICALL Java_com_koehlert_serialcomm_Communicator_transceive
  (JNIEnv *env, jobject thiz){
	  
	  int error = transceive();
	  
	  return error;
  }
/*
 * Class:     com_koehlert_serialcomm_Communicator
 * Method:    stop
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_koehlert_serialcomm_Communicator_stop
  (JNIEnv *, jobject){
	  
	  stop();
	  return 0;
  }
