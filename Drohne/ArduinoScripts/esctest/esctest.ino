#include <Servo.h>

#define MAX_SIGNAL 2000
#define MIN_SIGNAL 1000
#define MOTOR_PIN 3

Servo motor;
int speed1 = 1050;
int cal_int = 0;

void setup() {
  Serial.begin(9600);

  Serial.println("Calibrate");
  for (cal_int = 0; cal_int < 1250 ; cal_int ++){              //Wait 5 seconds before continuing.
    PORTD |= B01101000;                                        //Set digital poort 3,5,6 HIGH
    PORTB |= B00000010;                                        //Set digital port 9 High
    delayMicroseconds(1000);                                   //Wait 1000us.
    PORTD &= B10010111;                                        //Set digital poort 3,5,6 low.
    PORTB &= B11111101;                                        // Set digital port 9 low.
    delayMicroseconds(3000);                                   //Wait 3000us.
    digitalWrite(8, !digitalRead(8));
  }

  digitalWrite(8, LOW);

  Serial.println("Motor pin 3");
  for (int i = 0; i < 500; i++){
      PORTD |= B00001000;                                        //Set digital poort 3,5,6 HIGH
      PORTB |= B00000000;                                        //Set digital port 9 High
      delayMicroseconds(1000);                                   //Wait 1000us.
      PORTD &= B11110111;                                        //Set digital poort 3,5,6 low.
      PORTB &= B11111111;
      delayMicroseconds(3000);                                   //Wait 3000us.
  }
  delay(1000);
  Serial.println("Motor pin 5");
  for (int i = 0; i < 500; i++){
      PORTD |= B00100000;                                        //Set digital poort 3,5,6 HIGH
      PORTB |= B00000000;                                        //Set digital port 9 High
      delayMicroseconds(1000);                                   //Wait 1000us.
      PORTD &= B11011111;                                        //Set digital poort 3,5,6 low.
      PORTB &= B11111111;
      delayMicroseconds(3000);                                   //Wait 3000us.
  }
  delay(1000);
  Serial.println("Motor pin 6");
  for (int i = 0; i < 500; i++){
      PORTD |= B01000000;                                        //Set digital poort 3,5,6 HIGH
      PORTB |= B00000000;                                        //Set digital port 9 High
      delayMicroseconds(1000);                                   //Wait 1000us.
      PORTD &= B10111111;                                        //Set digital poort 3,5,6 low.
      PORTB &= B11111111;
      delayMicroseconds(3000);                                   //Wait 3000us.
  }
  delay(1000);
  Serial.println("Motor pin 9");
  for (int i = 0; i < 500; i++){
      PORTD |= B00000000;                                        //Set digital poort 3,5,6 HIGH
      PORTB |= B00000010;                                        //Set digital port 9 High
      delayMicroseconds(1000);                                   //Wait 1000us.
      PORTD &= B11111111;                                        //Set digital poort 3,5,6 low.
      PORTB &= B11111101;
      delayMicroseconds(3000);                                   //Wait 3000us.
  }

}


void loop() {
  /*if(Serial.available() > 0) {
    int incomingByte = Serial.read();
    if(incomingByte == 43)      // sends the character '+'
      speed1+=50;
    else if(incomingByte == 45) // sends the character '-'
      speed1-=50;
    else if(incomingByte == 48) // sends the character '0'
      speed1 = MIN_SIGNAL;


    Serial.print("Set speed to: ");
    Serial.println(speed1);

    motor.writeMicroseconds(speed1);
}*/
}
