int dataPointer = 0;
char receive[10];
char transmit[10];
char last_byte = '\0';

unsigned long CommunicationDuration;

void getRaspberryInfo(){
  if (Serial1.available()){
    unsigned long st = micros();
    while (dataPointer < 10){
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
      CommunicationDuration = micros() - st;
      //showReceived();
      convertToReceiver();
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

