/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_koehlert_camera_Camera */

#ifndef _Included_com_koehlert_camera_Camera
#define _Included_com_koehlert_camera_Camera
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_koehlert_camera_Camera
 * Method:    init
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_com_koehlert_camera_Camera_init
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     com_koehlert_camera_Camera
 * Method:    stop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_koehlert_camera_Camera_stop
  (JNIEnv *, jobject);

/*
 * Class:     com_koehlert_camera_Camera
 * Method:    getRGBAData
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_com_koehlert_camera_Camera_getRGBAData
  (JNIEnv *, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
