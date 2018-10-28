/**
    SPI_1 and SPI_2 port example code

    Description:
    This sketch sends one byte with value 0x55 over the SPI_1 or SPI_2 port.
    The received byte (the answer from the SPI slave device) is stored to the <data> variable.

    The sketch as it is, works with SPI_1 port. For using the SPI_2 port, just
    un-comment all the nessesary code lines marked with <SPI_2> word.

    Created on 10 Jun 2015 by Vassilis Serasidis
    email:  avrsite@yahoo.gr

    Using the first SPI port (SPI_1)
    SS    <-->  PA4 <-->  BOARD_SPI1_NSS_PIN
    SCK   <-->  PA5 <-->  BOARD_SPI1_SCK_PIN
    MISO  <-->  PA6 <-->  BOARD_SPI1_MISO_PIN
    MOSI  <-->  PA7 <-->  BOARD_SPI1_MOSI_PIN

    Using the second SPI port (SPI_2)
    SS    <-->  PB12 <-->  BOARD_SPI2_NSS_PIN
    SCK   <-->  PB13 <-->  BOARD_SPI2_SCK_PIN
    MISO  <-->  PB14 <-->  BOARD_SPI2_MISO_PIN
    MOSI  <-->  PB15 <-->  BOARD_SPI2_MOSI_PIN
*/


#include <SPI.h>

#define SPI2_NSS_PIN PB12   //SPI_2 Chip Select pin is PB12. You can change it to the STM32 pin you want.

SPIClass SPI_2(2); //Create an instance of the SPI Class called SPI_2 that uses the 2nd SPI Port
byte receive[10];
byte transmit[10];

void setup() {

  SPI_2.beginTransactionSlave(SPISettings(18000000, MSBFIRST, SPI_MODE0, DATA_SIZE_8BIT));
  
  pinMode(SPI2_NSS_PIN, INPUT);

  Serial.begin(230400);
  delay(300);
  Serial.println("Hello World");

  for (int i = 0; i < 10; i++){
    transmit[i] = (byte)10-i;
  }


}
unsigned long startTime;
void loop() {
   sendSPI2();

  delay(5);
}

void communicate(){
  //Send Request
  digitalWrite(PA9,HIGH);
  
}


void sendSPI2()
{
  //Handshake PA9
  startTime = micros();
  char msg = SPI_2.transfer('A');
  if (msg == 'R'){
    for (int i = 0; i < 10; i++){
      receive[i] = SPI_2.transfer(transmit[i]);
    }
  }

  int tm = (micros()-startTime);
  for (int i = 0; i < 10; i++){
    Serial.print("Received = 0b");
  Serial.print(receive[i], BIN);
  Serial.print(", 0x");
  Serial.print(receive[i], HEX);
  Serial.print(", ");
  Serial.println(receive[i]);
  }
  Serial.print(tm);
  Serial.println("us");
  
}
