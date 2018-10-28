
int pointer = 0;
int receivePointer = 0;
int counter = 0;
char receive[100];
char transmit[10];

int throttle,roll,pitch,yaw;

unsigned long dur;

void setup() {
  // put your setup code here, to run once:

  Serial.begin(230400);
  Serial1.begin(9600);

  for (int i = 0; i < 10; i++){
    transmit[i] = (char)i;
  }
  
  delay(300);
  Serial.println("Hallo Welt");
  Serial1.println("Hallo Raspberry");
}
char lb = '0';

void loop() {
  // put your main code here, to run repeatedly:

    while (receivePointer < 1){
        char rcv = Serial1.read();
        if (rcv != 0xFF){

          Serial1.print((char)42);
          //if (rcv != 0xFF){
            receive[receivePointer] = rcv;
            
            receivePointer++;
          //}
          pointer ++;
          pointer %= 10;
        }
        counter++;
    }

    if (receivePointer > 0){
      Serial.println("Angekommen:");
      for (int i = 0; i < receivePointer; i++){
        Serial.print(receive[i],HEX);
        Serial.print(" ");
      }
      Serial.println();
      receivePointer = 0;
      pointer = 0;
      counter = 0;
    }
  
}


void convertToReceiver(){
  throttle = (receive[1] << 8) | receive[0];
  roll = (receive[5] << 8) | receive[4];
  pitch = (receive[3] << 8) | receive[2];
  yaw = (receive[7] << 8) | receive[6];

  Serial.print(throttle);
  Serial.print(" ");
  Serial.print(roll);
  Serial.print(" ");
  Serial.print(pitch);
  Serial.print(" ");
  Serial.print(yaw);
  Serial.println(" ");
}

