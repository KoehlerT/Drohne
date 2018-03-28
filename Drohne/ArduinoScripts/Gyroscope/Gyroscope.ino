#include <Wire.h>
#include <MPU9250.h>
MPU9250 IMU(Wire,0x68);

 
float ax = 0;
float ay = 0;
float az = 0;

float gx = 0;
float gy = 0;
float gz = 0;

float temperature;

int status;

void setup() {
  
Serial.begin(9600);
  if (!IMU.begin()) 
  {
  Serial.println("IMU9250 sensor not found");
  while (1) {}
  int setAccelRange(2);
  int setGyroRange(250);
  status = IMU.calibrateGyro();
  }
}

void loop() {

IMU.readSensor();
ax = IMU.getAccelX_mss();
ay = IMU.getAccelY_mss();
az = IMU.getAccelZ_mss();

gx = IMU.getGyroX_rads();
gy = IMU.getGyroY_rads();
gz = IMU.getGyroZ_rads();

temperature = IMU.getTemperature_C();
Serial.println();
Serial.println();
Serial.println();
Serial.println(ax);
Serial.println(ay);
Serial.println(az);
Serial.println(gx);
Serial.println(gy);
Serial.println(gz);
Serial.println(temperature);
Serial.println();

delay(2000);
}
