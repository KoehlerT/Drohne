void readTransmission(){
  convertToReceiver();  
}

void convertToReceiver(){
  channel_3 = (receive[1] << 8) | receive[0]; //Throttle
  channel_1 = (receive[5] << 8) | receive[4]; //Roll
  channel_2 = (receive[3] << 8) | receive[2]; //Pitch
  channel_4 = (receive[7] << 8) | receive[6]; //Yaw
}

