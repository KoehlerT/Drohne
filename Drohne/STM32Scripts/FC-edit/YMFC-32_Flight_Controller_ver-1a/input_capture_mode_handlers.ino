int dataPointer = 0;
char receive[10];
char transmit[10];
char last_byte = '\0';

unsigned long CommunicationDuration;

void setTransmitUp(){
  for (int i = 0; i < 10; i++){
    transmit[i] = (char)i;
  }
}

void getRaspberryInfo(){
  setLooptime();
  
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
    }
    dataPointer = 0;
  }
  
}

void convertToReceiver(){
  channel_3 = (receive[1] << 8) | receive[0]; //Throttle
  channel_1 = (receive[5] << 8) | receive[4]; //Roll
  channel_2 = (receive[3] << 8) | receive[2]; //Pitch
  channel_4 = (receive[7] << 8) | receive[6]; //Yaw
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
  transmit[0] = (char)((int)(battery_voltage*1000) & 0x00FF);
  transmit[1] = (char)((int)(battery_voltage*1000) >> 8);
  transmit[2] = (char)((int)(used_power*1000) & 0x00FF);
  transmit[3] = (char)((int)(used_power*1000) >> 8);
  /*transmit[4] = (char)(battery_voltage & 0x00FF);
  transmit[5] = (char)(battery_voltage >> 8);
  transmit[6] = (char)(battery_voltage & 0x00FF);
  transmit[7] = (char)(battery_voltage >> 8);*/
}

