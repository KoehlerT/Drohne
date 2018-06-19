unsigned long looptime;
int pul = 2000;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(57600);
  Serial.println("HI");
  DDRD |= B11101000;       //Configure digital poort 3,5,6 Output

  //Serial.println("Cal Fertig");
  
  looptime = micros();
}

void loop() {
  // put your main code here, to run repeatedly:
  if (Serial.available()){
    char c = Serial.read();
    if (c == 'a')
      pul = 1000;
    if (c == 'm')
      pul = 2000;
    if (c == 'u')
      pul += 50;
    if (c == 'd')
      pul -= 50;

    Serial.print("Pulse: ");
    Serial.println(pul);
  }
  
  
  while(micros() - looptime < 4000);
  looptime = micros();
  pulse(pul);
  delayMicroseconds(2000);
}


void pulse(int del){
    PORTD |= B11101000;                                        //Set digital poort 3,5,6,7 HIGH
    delayMicroseconds(del);                                   //Wait del us.
    PORTD &= B00010111;                                        //Set digital poort 3,5,6,7 low.
}
