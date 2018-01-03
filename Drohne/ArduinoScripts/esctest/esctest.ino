#include <Servo.h>

#define MAX_SIGNAL 2000
#define MIN_SIGNAL 1000
#define MOTOR_PIN 8

Servo motor;
int speed1 = 1050;

void setup() {
  Serial.begin(9600);
  
  Serial.println("Power the ESC now and send any key when ready");

  motor.attach(MOTOR_PIN);
  motor.writeMicroseconds(MAX_SIGNAL);

  // Wait for input
  while (!Serial.available());
  Serial.read();
  motor.writeMicroseconds(MIN_SIGNAL);
  delay(4000);

  motor.writeMicroseconds(speed1); //start changing motor speed

  Serial.println("Start loop...");
}


void loop() { 
  if(Serial.available() > 0) {
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
  }
}
