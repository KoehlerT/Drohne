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
float offset;

void setup() {
  
Serial.begin(57600);
  if (!IMU.begin()) 
  {
  Serial.println("IMU9250 sensor not found");
  while (1) {}
  int setAccelRange(2);
  status = IMU.setGyroRange(MPU9250::GYRO_RANGE_250DPS);
  status = IMU.calibrateGyro();
  }

  status = IMU.setGyroRange(MPU9250::GYRO_RANGE_500DPS);
  status = IMU.calibrateGyro();
  
  for (int i = 0; i < 1000; i++){
    IMU.readSensor();
    offset += IMU.getGyroZ_rads();
    delay(1);
  }
  offset/=1000;
  Serial.print(offset);
}

float angle = 0;

void loop() {

IMU.readSensor();
ax = IMU.getAccelX_mss();
ay = IMU.getAccelY_mss();
az = IMU.getAccelZ_mss();

gx = IMU.getGyroX_rads();
gy = IMU.getGyroY_rads();
gz = IMU.getGyroZ_rads();

temperature = IMU.getTemperature_C();
/*Serial.println(ax);
Serial.println(ay);
Serial.println(az);
Serial.println(gx);
Serial.println(gy);
Serial.println(gz);*/
gz-=offset;
Serial.println(gz);
Serial.println(gz*(float)360);
angle += gz*((float)(360)/(float)1000);
Serial.print("Winkel gesamt: ");
Serial.println(angle);
//Serial.println(temperature);
Serial.println();

delay(1);
}
