int dataPointer = 0;
char receive[10];
char transmit[10] = {1,1,1,1,1,1,1,1,1,1};
char last_byte = '\0';

unsigned long CommunicationDuration;

int16_t read2(uint8_t index){
  return receive[index] | (receive[index+1]<<8);
}

void getRaspberryInfo(){
  //return;
  unsigned int st = micros();
  if (true/*!digitalRead(SPI2_NSS_PIN)*/){
    char msg = SPI_2.transfer('A');//Handshake
    dataPointer = 0;
    if (msg == 'R'){
      for (int i = 0; i < 10; i++){
        receive[i] = SPI_2.transfer(transmit[i]);
      }

      CommunicationDuration = micros() - st;
      //showReceived();
        convertToReceiver();
        dataPointer = 0;
        Serial.print(CommunicationDuration);
        Serial.println("us");
    }
      
      
    }

}



void convertToReceiver(){
  channel_3 = read2(1); //Throttle
  channel_1 = read2(3); //Roll
  channel_2 = read2(5); //Pitch
  channel_4 = read2(7); //Yaw
}

void showReceived(void){
  for (int i = 0; i < 10; i++){
      Serial.print(receive[i],HEX);
      Serial.print(", ");
    }
    //Serial.print("Dur: ");
    //Serial.print(CommunicationDuration);
    //Serial.println("us");
    Serial.println();
}

