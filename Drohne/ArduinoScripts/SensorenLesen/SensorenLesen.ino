#include <Wire.h>
#include <MPU9250.h>

int cal_int = 2000;
#define voltageScale 4.88758

//Sensor Bibs
MPU9250 myIMU;

float gyro_roll, gyro_pitch, gyro_yaw;
float acc_x, acc_y, acc_z;
float mag_x, mag_y, mag_z;
int gyro_axis[4];
int gyro_axis_cal[4];
byte datenplatzhalter[10];

//Duration
unsigned long duration, mpuRead, voltageRead, preassureRead;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(57600);

  Wire.begin();                                                //Start the I2C as master.
  //Serial.println(IMU.begin());
  byte c = myIMU.readByte(MPU9250_ADDRESS, WHO_AM_I_MPU9250);
  Serial.print("MPU9250 "); Serial.print("I AM "); Serial.print(c, HEX);
  Serial.print(" I should be "); Serial.println(0x71, HEX);

  set_gyro_registers(c);
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

void set_gyro_registers(byte c){
  if (c == 0x71) // WHO_AM_I should always be 0x68
  {
    Serial.println("MPU9250 is online...");

    // Start by performing self test and reporting values
    myIMU.MPU9250SelfTest(myIMU.SelfTest);
    Serial.print("x-axis self test: acceleration trim within : ");
    Serial.print(myIMU.SelfTest[0],1); Serial.println("% of factory value");
    Serial.print("y-axis self test: acceleration trim within : ");
    Serial.print(myIMU.SelfTest[1],1); Serial.println("% of factory value");
    Serial.print("z-axis self test: acceleration trim within : ");
    Serial.print(myIMU.SelfTest[2],1); Serial.println("% of factory value");
    Serial.print("x-axis self test: gyration trim within : ");
    Serial.print(myIMU.SelfTest[3],1); Serial.println("% of factory value");
    Serial.print("y-axis self test: gyration trim within : ");
    Serial.print(myIMU.SelfTest[4],1); Serial.println("% of factory value");
    Serial.print("z-axis self test: gyration trim within : ");
    Serial.print(myIMU.SelfTest[5],1); Serial.println("% of factory value");

    // Calibrate gyro and accelerometers, load biases in bias registers
    myIMU.calibrateMPU9250(myIMU.gyroBias, myIMU.accelBias);

    myIMU.initMPU9250();
    // Initialize device for active mode read of acclerometer, gyroscope, and
    // temperature
    Serial.println("MPU9250 initialized for active data mode....");

    // Read the WHO_AM_I register of the magnetometer, this is a good test of
    // communication
    byte d = myIMU.readByte(AK8963_ADDRESS, WHO_AM_I_AK8963);
    Serial.print("AK8963 "); Serial.print("I AM "); Serial.print(d, HEX);
    Serial.print(" I should be "); Serial.println(0x48, HEX);

    // Get magnetometer calibration from AK8963 ROM
    myIMU.initAK8963(myIMU.magCalibration);
    // Initialize device for active mode read of magnetometer
    Serial.println("AK8963 initialized for active data mode....");
    if (true)
    {
      //  Serial.println("Calibration values: ");
      Serial.print("X-Axis sensitivity adjustment value ");
      Serial.println(myIMU.magCalibration[0], 2);
      Serial.print("Y-Axis sensitivity adjustment value ");
      Serial.println(myIMU.magCalibration[1], 2);
      Serial.print("Z-Axis sensitivity adjustment value ");
      Serial.println(myIMU.magCalibration[2], 2);
    }
  } // if (c == 0x71)
  else
  {
    Serial.print("Could not connect to MPU9250: 0x");
    Serial.println(c, HEX);
    while(1) ; // Loop forever if communication doesn't happen
}
myIMU.getGres();
myIMU.getAres();
}

void gyro_signalen(){
  //Read the L3G4200D or L3GD20H
  unsigned long startTime = micros();

  myIMU.readAccelData(myIMU.accelCount);  // Read the x/y/z adc values
  // Now we'll calculate the accleration value into actual g's
  // This depends on scale being set
  acc_x = (float)myIMU.accelCount[0]*myIMU.aRes; // - accelBias[0];
  acc_y = (float)myIMU.accelCount[1]*myIMU.aRes; // - accelBias[1];
  acc_z = (float)myIMU.accelCount[2]*myIMU.aRes; // - accelBias[2];

  myIMU.readGyroData(myIMU.gyroCount);
  gyro_roll = (float)myIMU.gyroCount[0]*myIMU.gRes;                                 // Takes the Informations and gives it as deg/s
  gyro_pitch = (float)myIMU.gyroCount[1]*myIMU.gRes;                                // X and Y are switched, because of the mounting on the board
  gyro_yaw = (float)myIMU.gyroCount[2]*myIMU.gRes;

  gyro_roll = -gyro_roll;
  gyro_pitch = -gyro_pitch;

  myIMU.readMagData(myIMU.magCount);  // Read the x/y/z adc values
  myIMU.getMres();
  // User environmental x-axis correction in milliGauss, should be
  // automatically calculated
  myIMU.magbias[0] = +470.;
  // User environmental x-axis correction in milliGauss TODO axis??
  myIMU.magbias[1] = +120.;
  // User environmental x-axis correction in milliGauss
  myIMU.magbias[2] = +125.;

  // Calculate the magnetometer values in milliGauss
  // Include factory calibration per data sheet and user environmental
  // corrections
  // Get actual magnetometer value, this depends on scale being set
  mag_x = (float)myIMU.magCount[0]*myIMU.mRes*myIMU.magCalibration[0] -
             myIMU.magbias[0];
  mag_y = (float)myIMU.magCount[1]*myIMU.mRes*myIMU.magCalibration[1] -
             myIMU.magbias[1];
  mag_z = (float)myIMU.magCount[2]*myIMU.mRes*myIMU.magCalibration[2] -
            myIMU.magbias[2];

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
          int voltage = analogRead(v)*voltageScale;
          datenplatzhalter[v*2] = (byte)(voltage &0x00FF);
          datenplatzhalter[v*2+1] =(byte)(voltage >> 8);
      }

      voltageRead = (micros()-startTime)-mpuRead;

      //Bis index 7
      //float altitude = bmp.readAltitude(startLuftdruck);
      /*float altitude = bmp.readAltitude();
      int alt = (int)(altitude * 100);
      datenplatzhalter[8] = (byte)alt;
      datenplatzhalter[9] = (byte)(alt >> 8);*/
      preassureRead = 0;

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
    Serial.print("Gyro: "); Serial.print(gyro_roll); Serial.print(" ");Serial.print(gyro_pitch); Serial.print(" "); Serial.println(gyro_yaw);
    Serial.print("Acc: "); Serial.print(acc_x); Serial.print(" ");Serial.print(acc_y); Serial.print(" "); Serial.println(acc_z);
    Serial.print("Mag: "); Serial.print(mag_x); Serial.print(" ");Serial.print(mag_y); Serial.print(" "); Serial.println(mag_z);
}
