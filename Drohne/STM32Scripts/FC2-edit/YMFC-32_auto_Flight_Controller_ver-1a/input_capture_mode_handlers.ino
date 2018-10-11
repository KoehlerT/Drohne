///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//In this file the timers for reading the receiver pulses and for creating the output ESC pulses are set.
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//More information can be found in these two videos:
//STM32 for Arduino - Connecting an RC receiver via input capture mode: https://youtu.be/JFSFbSg0l2M
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
int dataPointer = 0;
char last_byte = '\0';
int missedTransmissions = 0;

unsigned long CommunicationDuration;

void getRaspberryInfo(){
  if (Serial1.available()){
    CommunicationDuration = 0;
    unsigned long st = micros();
    while (dataPointer < 10 && CommunicationDuration<1000){
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
    }
    if (dataPointer >= 10){
      nextPackage = true;
      missedTransmissions = 0;
      readTransmission();
    }
    dataPointer = 0;
  }else{
    missedTransmissions ++;
    if (missedTransmissions > 400){//Wenn seit 1,6s keine übertragung stattgefunden hat
      error = 8;
      start = 0;
    }
  }
  
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

