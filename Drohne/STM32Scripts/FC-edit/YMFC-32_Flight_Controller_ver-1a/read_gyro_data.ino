#define HWire WIRE2
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//This part reads the raw gyro and accelerometer data from the MPU-6050
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void gyro_signalen(void) {
  HWire.beginTransmission(gyro_address);                       //Start communication with the gyro.
  HWire.write(0x3B);                                           //Start reading @ register 43h and auto increment with every read.
  HWire.endTransmission();                                     //End the transmission.
  HWire.requestFrom(gyro_address, 14);                         //Request 14 bytes from the MPU 6050.
  acc_y = HWire.read() << 8 | HWire.read();                    //Add the low and high byte to the acc_x variable.
  acc_x = HWire.read() << 8 | HWire.read();                    //Add the low and high byte to the acc_y variable.
  acc_z = HWire.read() << 8 | HWire.read();                    //Add the low and high byte to the acc_z variable.
  temperature = HWire.read() << 8 | HWire.read();              //Add the low and high byte to the temperature variable.
  gyro_roll = HWire.read() << 8 | HWire.read();                //Read high and low part of the angular data.
  gyro_pitch = HWire.read() << 8 | HWire.read();               //Read high and low part of the angular data.
  gyro_yaw = HWire.read() << 8 | HWire.read();                 //Read high and low part of the angular data.
  gyro_pitch *= -1;                                            //Invert the direction of the axis.
  gyro_yaw *= -1;                                              //Invert the direction of the axis.

  acc_y -= manual_acc_pitch_cal_value;                         //Subtact the manual accelerometer pitch calibration value.
  acc_x -= manual_acc_roll_cal_value;                          //Subtact the manual accelerometer roll calibration value.
  gyro_roll -= manual_gyro_roll_cal_value;                     //Subtact the manual gyro roll calibration value.
  gyro_pitch -= manual_gyro_pitch_cal_value;                   //Subtact the manual gyro pitch calibration value.
  gyro_yaw -= manual_gyro_yaw_cal_value;                       //Subtact the manual gyro yaw calibration value.
}
