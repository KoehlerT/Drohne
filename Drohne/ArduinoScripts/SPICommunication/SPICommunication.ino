//Danke an robotics.hobbizine.com/raspiduino.html
#include<SPI.h>


void setup() {
  // put your setup code here, to run once
  pinMode(MISO,OUTPUT);
  SPCR |= _BV(SPE);
  Serial.begin(9600);
  Serial.println("Gestartet");
  
  //SPI.attachInterrupt();//Interrupt routine
}

void loop() {
  // put your main code here, to run repeatedly
  receive();
}

void receive(){
  if ((SPSR & (1<<SPIF)) != 0){ //Hat sich der Register verändert?
    Serial.println((char)SPDR);
    SPDR = 'Z';
    //delay(2);
  }
}

ISR(SPI_STC_vect){
  Serial.println((char)SPDR);
  SPDR = (char)128;
}

