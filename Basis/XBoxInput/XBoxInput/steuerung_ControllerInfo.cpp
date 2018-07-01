#include "steuerung_ControllerInfo.h"
#include "CXBOXController.h"

#define BOOLRET(x) if(x){return JNI_TRUE;}else{return JNI_FALSE;}

CXBOXController* Controller;



/*
* Class:     steuerung_ControllerInfo
* Method:    initController
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_steuerung_ControllerInfo_initController
(JNIEnv *, jobject) {
	Controller = new CXBOXController(1);
	if (Controller->IsConnected())
		return 0;
	else
		return 1;
}

/*
* Class:     steuerung_ControllerInfo
* Method:    deleteController
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_steuerung_ControllerInfo_deleteController
(JNIEnv *, jobject) {
	if (Controller == nullptr)
		return 1;
	delete(Controller);
	Controller = nullptr;
	return 0;
}

/*
* Class:     steuerung_ControllerInfo
* Method:    isConnected
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_isConnected
(JNIEnv *env, jobject) {
	bool connected = Controller->IsConnected();
	if (true) {
		return JNI_TRUE;
	}
	else {
		return JNI_FALSE;
	}
	
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_A
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1A
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_A);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_B
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1B
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_B);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_X
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1X
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_X);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_Y
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1Y
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_Y);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_D_UP
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1D_1UP
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_DPAD_UP);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_D_DOWN
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1D_1DOWN
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_DPAD_DOWN);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_D_LEFT
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1D_1LEFT
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_DPAD_LEFT);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_D_RIGHT
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1D_1RIGHT
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_DPAD_RIGHT);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_START
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1START
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_START);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_BACK
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1BACK
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_BACK);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_TH_RIGHT
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1TH_1RIGHT
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_RIGHT_THUMB);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_TH_LEFT
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1TH_1LEFT
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_LEFT_THUMB);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_SH_RIGHT
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1SH_1RIGHT
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_RIGHT_SHOULDER);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getGamepad_SH_LEFT
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_steuerung_ControllerInfo_getGamepad_1SH_1LEFT
(JNIEnv *, jobject) {
	BOOLRET(Controller->GetState().Gamepad.wButtons & XINPUT_GAMEPAD_LEFT_SHOULDER);
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getTrigger_LEFT
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_steuerung_ControllerInfo_getTrigger_1LEFT
(JNIEnv *, jobject) {
	return (int)Controller->GetState().Gamepad.bLeftTrigger;
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getTrigger_RIGHT
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_steuerung_ControllerInfo_getTrigger_1RIGHT
(JNIEnv *, jobject) {
	return (int)Controller->GetState().Gamepad.bRightTrigger;
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getThumb_LX
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_steuerung_ControllerInfo_getThumb_1LX
(JNIEnv *, jobject) {
	return (int)Controller->GetState().Gamepad.sThumbLX;
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getThumb_LY
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_steuerung_ControllerInfo_getThumb_1LY
(JNIEnv *, jobject) {
	return (int)Controller->GetState().Gamepad.sThumbLY;
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getThumb_RX
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_steuerung_ControllerInfo_getThumb_1RX
(JNIEnv *, jobject) {
	return (int)Controller->GetState().Gamepad.sThumbRX;
}

/*
* Class:     steuerung_ControllerInfo
* Method:    getThumb_RY
* Signature: ()I
*/
JNIEXPORT jint JNICALL Java_steuerung_ControllerInfo_getThumb_1RY
(JNIEnv *, jobject) {
	return (int)Controller->GetState().Gamepad.sThumbRY;
}

/*
* Class:     steuerung_ControllerInfo
* Method:    Vibrate
* Signature: (FF)V
*/
JNIEXPORT void JNICALL Java_steuerung_ControllerInfo_Vibrate
(JNIEnv *env, jobject thiz, jfloat left, jfloat right) {
	if (right < 0 || right > 1)
		right = 0;
	if (left < 0 || left > 1)
		left = 0;
	Controller->Vibrate(((float)65535 * left), ((float)65535 * right));
}