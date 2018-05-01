//#include <Wire.h>
#include "MPU9250.h"

// an IMU object with the MPU-9250 sensor on I2C bus 0 with address 0x68
MPU9250 IMU(Wire,0x68);
int status;

void setup() {
  // serial to display data
  Serial.begin(57600);
  while(!Serial) {}

  // start communication with IMU
  status = IMU.begin();
  if (status < 0) {
    Serial.println("IMU initialization unsuccessful");
    Serial.println("Check IMU wiring or try cycling power");
    Serial.print("Status: ");
    Serial.println(status);
    //while(1) {}
  }
  getAccelBiases();
  getMagBiases();
}

void getAccelBiases(){
    Serial.println("Accelerometer");
    status = IMU.calibrateAccel();
    Serial.print("Bias X: ");
    Serial.println(IMU.getAccelBiasX_mss());
    Serial.print("Bias Y: ");
    Serial.println(IMU.getAccelBiasY_mss());
    Serial.print("Bias Z: ");
    Serial.println(IMU.getAccelBiasZ_mss());

    Serial.print("SclFac X: ");
    Serial.println(IMU.getAccelScaleFactorX());
    Serial.print("SclFac Y: ");
    Serial.println(IMU.getAccelScaleFactorY());
    Serial.print("SclFac Z: ");
    Serial.println(IMU.getAccelScaleFactorZ());

}

void getMagBiases(){
    Serial.println("Magnetometer");
    status = IMU.calibrateMag();
    Serial.print("Bias X: ");
    Serial.println(IMU.getMagBiasX_uT());
    Serial.print("Bias Y: ");
    Serial.println(IMU.getMagBiasY_uT());
    Serial.print("Bias Z: ");
    Serial.println(IMU.getMagBiasZ_uT());

    Serial.print("SclFac X: ");
    Serial.println(IMU.getMagScaleFactorX());
    Serial.print("SclFac Y: ");
    Serial.println(IMU.getMagScaleFactorY());
    Serial.print("SclFac Z: ");
    Serial.println(IMU.getMagScaleFactorZ());
}

void loop() {
  // read the sensor
  IMU.readSensor();
  // display the data
  Serial.print(IMU.getAccelX_mss(),6);
  Serial.print("\t");
  Serial.print(IMU.getAccelY_mss(),6);
  Serial.print("\t");
  Serial.print(IMU.getAccelZ_mss(),6);
  Serial.print("\t");
  Serial.print(IMU.getGyroX_rads(),6);
  Serial.print("\t");
  Serial.print(IMU.getGyroY_rads(),6);
  Serial.print("\t");
  Serial.print(IMU.getGyroZ_rads(),6);
  Serial.print("\t");
  Serial.print(IMU.getMagX_uT(),6);
  Serial.print("\t");
  Serial.print(IMU.getMagY_uT(),6);
  Serial.print("\t");
  Serial.print(IMU.getMagZ_uT(),6);
  Serial.print("\t");
  Serial.println(IMU.getTemperature_C(),6);
  delay(100);
}
