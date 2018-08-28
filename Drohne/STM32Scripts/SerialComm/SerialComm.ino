
int pointer = 0;
char receive[10];
char transmit[10];

int throttle,roll,pitch,yaw;

unsigned long dur;

void setup() {
  // put your setup code here, to run once:

  Serial.begin(230400);
  Serial1.begin(230400);

  for (int i = 0; i < 10; i++){
    transmit[i] = (char)i;
  }
  
  delay(200);
  Serial.println("Hallo Welt");
}
char lb = '0';
void loop() {
  // put your main code here, to run repeatedly:

  if (Serial1.available()){
    unsigned long st = micros();
    while (pointer < 10){
      if (Serial1.available()){
        char rd = (char)Serial1.read();
        if (rd == 'R'){
          lb = rd;
          Serial1.print('A');
        }else if (rd == 'Q' && lb == 'R'){
          
          pointer = 0;
        }else{
          receive[(pointer++)] = rd;
          Serial1.print(transmit[pointer]);
        }
      }
      dur = micros() - st;
    }
    pointer = 0;
    
    //Anz
    for (int i = 0; i < 10; i++){
      Serial.print((int)receive[i]);
      Serial.print(", ");
    }
    Serial.print("Dur: ");
    Serial.print(dur);
    Serial.println("us");
    convertToReceiver();
  }
  delay(4);
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

