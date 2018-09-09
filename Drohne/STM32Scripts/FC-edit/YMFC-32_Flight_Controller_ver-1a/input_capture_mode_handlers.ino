int dataPointer = 0;
char receive[10];
char transmit[10];
char last_byte = '\0';
unsigned long lastComm = 0;
int channel_1_temp, channel_2_temp, channel_3_temp, channel_4_temp;

unsigned long CommunicationDuration;

void setTransmitUp(){
  for (int i = 0; i < 10; i++){
    transmit[i] = (char)i;
  }
}

void getRaspberryInfo(){
  setLooptime();
  setIntegers();
  
  if (Serial1.available()){
    unsigned long st = micros();
    while (dataPointer < 10 && ((micros() - st) < 2000)){
      if (Serial1.available()){
        char rd = (char)Serial1.read();
        if (rd == 'R'){ //Handshake inidicator 1
          last_byte = rd;
          Serial1.print('A'); //Handshake Accept
        }else if (rd == 'Q' && last_byte == 'R'){ //Handshake indicator 2
          dataPointer = 0; //Start Transmission
        }else{
          receive[dataPointer] = rd;
          Serial1.print(transmit[dataPointer]);
          dataPointer++;
        }
      }
      if (((micros() - st) <= 2000)){
        convertToReceiver();
      }
      #ifdef debug
      
      else { Serial.println("Abbruch");}
      #endif
      lastComm = micros();
      
    }
    dataPointer = 0;
  }

  //Not-Aus, wenn übertragung für 2sec abbricht
  if (lastComm != 0 && micros() - lastComm > 2000000){
    start = 0;
    error = 3;
  }
}

void convertToReceiver(){
  channel_3_temp = (receive[1] << 8) | receive[0]; //Throttle
  channel_1_temp = (receive[5] << 8) | receive[4]; //Roll
  channel_2_temp = (receive[3] << 8) | receive[2]; //Pitch
  channel_4_temp = (receive[7] << 8) | receive[6]; //Yaw

  if (outOfRange(channel_3_temp)|| outOfRange(channel_1_temp) || outOfRange(channel_2_temp)|| outOfRange(channel_4_temp))
    return;

  channel_3 = channel_3_temp;
  channel_1 = channel_1_temp;
  channel_2 = channel_2_temp;
  channel_4 = channel_4_temp;
  controlByte = receive[8];
}

boolean outOfRange(int v){
  return (v<1000 || v > 2000);
}

void showReceived(void){
  for (int i = 0; i < 10; i++){
      Serial.print((int)receive[i]);
      Serial.print(", ");
    }
    Serial.print("Dur: ");
    Serial.print(CommunicationDuration);
    Serial.println("us");
}

void setLooptime(){
  transmit[8] = (byte)loopDuration;
  transmit[9] = (byte) (loopDuration >> 8);
}

void setIntegers(){
  int16_t test = 60000;
  transmit[0] = (char)((int16_t)(battery_voltage * 1000) & 0x00FF);
  transmit[1] = (char)((int16_t)(battery_voltage * 1000) >> 8)&0x00FF;
  transmit[2] = (char)((int16_t)(used_power*1000) & 0x00FF);
  transmit[3] = (char)((int16_t)(used_power*1000) >> 8);
  transmit[4] = (char)(int16_t)(((altitude_meter)+2)*100) & 0x00FF;
  transmit[5] = (char)((int16_t)(((altitude_meter)+2)*100) >> 8)& 0x00FF;
  transmit[6] = (char)(0x76);
  transmit[7] = (char)(0x9A);
}

