#include <Wire.h>
#include <Adafruit_ADS1015.h>              //Read Voltage Sensor
#include <MPU9250.h>
#include <Adafruit_BMP085.h>

int cal_int = 2000;
#define adcScale 0.1875f

//Sensor Bibs
MPU9250 IMU(Wire,0x68);
Adafruit_ADS1115 ads;
Adafruit_BMP085 bmp;

int gyro_roll, gyro_pitch, gyro_yaw;
int gyro_axis[4];
int gyro_axis_cal[4];
byte datenplatzhalter[10];

//Duration
unsigned long duration, mpuRead, voltageRead, preassureRead;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(57600);

  Wire.begin();                                                //Start the I2C as master.
  ads.begin();                                             //Start I2C ADS
  Serial.println(IMU.begin());
  bmp.begin(0);

  set_gyro_registers();
}

void loop() {
  // put your main code here, to run repeatedly:
  /*Serial.write(27);       // ESC command
  Serial.print("[2J");    // clear screen command
  Serial.write(27);
  Serial.print("[H");*/
  gyro_signalen();

  delay(500);
}

void set_gyro_registers(){

}

void gyro_signalen(){
  //Read the L3G4200D or L3GD20H
  unsigned long startTime = micros();
  //Read the MPU-6050

  IMU.readSensor();                                                // Reads Sensorinformations of the MPU9250
  gyro_roll = IMU.getGyroY_rads();                                 // Takes the Informations and gives it as rad/s
  gyro_pitch = IMU.getGyroY_rads();                                // X and Y are switched, because of the mounting on the board
  gyro_yaw = IMU.getGyroZ_rads();

  gyro_roll = -gyro_roll;
  gyro_pitch = -gyro_pitch;
  mpuRead = (micros()-startTime);

  if(cal_int == 2000){
    gyro_axis[1] -= gyro_axis_cal[1];                            //Only compensate after the calibration
    gyro_axis[2] -= gyro_axis_cal[2];                            //Only compensate after the calibration
    gyro_axis[3] -= gyro_axis_cal[3];                            //Only compensate after the calibration
  }

  if (cal_int == 2000){ //Only calculate if not calibrating
      //datenplatzhalter 10 Elemente!
      //Read Voltages
      for (int v = 0; v < 4; v++) {
          int voltage = (ads.readADC_SingleEnded(v) * adcScale);
          datenplatzhalter[v*2] = (byte)(voltage &0x00FF);
          datenplatzhalter[v*2+1] =(byte)(voltage >> 8);
      }

      voltageRead = (micros()-startTime)-mpuRead;

      //Bis index 7
      //float altitude = bmp.readAltitude(startLuftdruck);
      float altitude = bmp.readAltitude();
      int alt = (int)(altitude * 100);
      datenplatzhalter[8] = (byte)alt;
      datenplatzhalter[9] = (byte)(alt >> 8);
      preassureRead = (micros()-startTime)-(mpuRead+voltageRead);

  }
  /*
  gyro_roll = gyro_axis[eeprom_data[28] & 0b00000011];
  if(eeprom_data[28] & 0b10000000)gyro_roll *= -1;
  gyro_pitch = gyro_axis[eeprom_data[29] & 0b00000011];
  if(eeprom_data[29] & 0b10000000)gyro_pitch *= -1;
  gyro_yaw = gyro_axis[eeprom_data[30] & 0b00000011];
  if(eeprom_data[30] & 0b10000000)gyro_yaw *= -1;
*/
    duration = (micros()-startTime);
    Serial.print("Sensor Data: ");Serial.print(duration);Serial.println("us");
    Serial.print("Voltage Read: ");Serial.print(voltageRead);Serial.println("us");
    Serial.print("MPU Read: ");Serial.print(mpuRead);Serial.println("us");
    Serial.print("Preassure Read: ");Serial.print(preassureRead);Serial.println("us");
}
