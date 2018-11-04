///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//In this file the timers for reading the receiver pulses and for creating the output ESC pulses are set.
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//More information can be found in these two videos:
//STM32 for Arduino - Connecting an RC receiver via input capture mode: https://youtu.be/JFSFbSg0l2M
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
char last_byte = '\0';
int missedTransmissions = 0;
int dataLoc = 0;

void getRaspberryInfo(){
  unsigned int st = micros();
  if (digitalRead(PA2)){
      char msg = SPI_2.transfer('A');//Handshake
      HSDuration = micros() - st;
      dataLoc = 0;
      if (msg == 'R'){
        
        while ((dataLoc < 10) && ((micros() - st) < 2000)){
          if (digitalRead(PA2)){
            receive[dataLoc] = SPI_2.transfer(transmit[dataLoc]);
            dataLoc ++;
          }
        } 
      }
       
      if (dataLoc >= 10){
        CommunicationDuration = micros() - st;
        nextPackage = true;
        readTransmission();    
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

