int32_t read4(uint8_t index){
  return receive[index] | receive[index+1] << 8 | (receive[index+2] << 16) | (receive[index+3] << 24);
}
int16_t read2(uint8_t index){
  return receive[index] | (receive[index+1]<<8);
}

void readTransmission(){
  getControllword();
}

void getControllword(void){
  byte control = receive[0];
  switch(control){
    case 0x00: break;
    case 0x01: getControllerInputs(); break;
    case 0x10: CalComp(); break;  //Calibrations
    case 0x11: CalLevel(); break;
    case 0x20: variable_1_to_adjust = read4(1)-1500; DEBUGb("set1",variable_1_to_adjust) break;  //Settubgs
    case 0x21: variable_2_to_adjust = read4(1); break;
    case 0x22: variable_3_to_adjust = read4(1); break;
    case 0x30: if (receive[1] == 1) heading_lock = 1; else heading_lock = 0; break; //Heading lock on/off
    case 0x31: if (receive[1] == 1) flight_mode = 2; else flight_mode = 1; break;  //Altitude hold on/off
    case 0x32: if (receive[1] == 1) flight_mode = 3; else flight_mode = 2; break; //GPS hold on/off
    case 0x40: start = 0; break;
    case 0x41: start = 2; break;
    case 0x42: break; //Reserved takeoff
    case 0x43: break; //reserved land
    case 0x50: setHeight();break;
    case 0x51: setWaypoint(); break;
  }

  #ifdef deb
    if (control != 1){
      DEBUGb("Control Word: ", control)
      DEBUGb("Data: ", (int)receive[1])
      DEBUGb("Flight Mode: ", (int)flight_mode)
    DEBUGb("Thorttle: ", channel_3)
    }
    
  #endif
}

void getControllerInputs(void){
  channel_3 = read2(1); //Throttle
  channel_1 = read2(3); //Roll
  channel_2 = read2(5); //Pitch
  channel_4 = read2(7); //Yaw
}
void setHeight(void){
  int32_t inputHeight = read4(1) - 100; //Input -100: Desired change in cm
  float changePressure = (float)inputHeight / 8.42;
  pid_altitude_setpoint += changePressure;
}
void setWaypoint(void){
  l_lat_waypoint = read4(1);
  l_lon_waypoint = read4(5);
}

void CalComp(){
  calibrate_compass();
}
void CalLevel(){
  receive[0] = 0;
  if (level_calibration_on == 0){
    calibrate_level();
  }
  
}/*void convertToReceiver(){
}
  channel_3 = (receive[1] << 8) | receive[0]; //Throttle
  channel_1 = (receive[5] << 8) | receive[4]; //Roll
  channel_2 = (receive[3] << 8) | receive[2]; //Pitch
  channel_4 = (receive[7] << 8) | receive[6]; //Yaw
}*/
