#define HWire WIRE2
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//In this part the various registers of the MPU-6050 are set.
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void gyro_setup(void){
  HWire.beginTransmission(gyro_address);                        //Start communication with the MPU-6050.
  HWire.write(0x6B);                                            //We want to write to the PWR_MGMT_1 register (6B hex).
  HWire.write(0x00);                                            //Set the register bits as 00000000 to activate the gyro.
  HWire.endTransmission();                                      //End the transmission with the gyro.

  HWire.beginTransmission(gyro_address);                        //Start communication with the MPU-6050.
  HWire.write(0x1B);                                            //We want to write to the GYRO_CONFIG register (1B hex).
  HWire.write(0x08);                                            //Set the register bits as 00001000 (500dps full scale).
  HWire.endTransmission();                                      //End the transmission with the gyro.

  HWire.beginTransmission(gyro_address);                        //Start communication with the MPU-6050.
  HWire.write(0x1C);                                            //We want to write to the ACCEL_CONFIG register (1A hex).
  HWire.write(0x10);                                            //Set the register bits as 00010000 (+/- 8g full scale range).
  HWire.endTransmission();                                      //End the transmission with the gyro.

  HWire.beginTransmission(gyro_address);                        //Start communication with the MPU-6050.
  HWire.write(0x1A);                                            //We want to write to the CONFIG register (1A hex).
  HWire.write(0x03);                                            //Set the register bits as 00000011 (Set Digital Low Pass Filter to ~43Hz).
  HWire.endTransmission();                                      //End the transmission with the gyro.
}
