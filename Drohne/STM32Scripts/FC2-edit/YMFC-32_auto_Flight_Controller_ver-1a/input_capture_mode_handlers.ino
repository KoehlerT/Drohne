///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//In this file the timers for reading the receiver pulses and for creating the output ESC pulses are set.
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//More information can be found in these two videos:
//STM32 for Arduino - Connecting an RC receiver via input capture mode: https://youtu.be/JFSFbSg0l2M
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
char last_byte = '\0';
int missedTransmissions = 0;

unsigned long CommunicationDuration;

void getRaspberryInfo(){
  unsigned int st = micros();
  if (true/*!digitalRead(SPI2_NSS_PIN)*/){
    char msg = SPI_2.transfer('A');//Handshake
    if (msg == 'R'){
      for (int i = 0; i < 10; i++){
        receive[i] = SPI_2.transfer(transmit[i]);
      }

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

