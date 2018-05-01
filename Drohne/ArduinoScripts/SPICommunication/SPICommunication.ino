//Danke an robotics.hobbizine.com/raspiduino.html
//Import

//Global Variables
int s = 0;
byte datenplatzhalter[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
byte ci[8];
int throttle;
int pitch;
int roll;
int yaw;

//Setup
void setup() {
  //Set up SPI
  pinMode(MISO,OUTPUT);
  SPCR |= _BV(SPE);
  //Set up Serial Connection
  Serial.begin(57600);
  Serial.println("Gestartet");
  Serial.print("Größen: "); Serial.print(sizeof(char)); Serial.print(", "), Serial.println(sizeof(int));
}

void loop() {
  // put your main code here, to run repeatedly
  //TEURER CODE, z.B. Sensoren auslesen
  delay(500);
  receive();
  constructInputs();
  Serial.print("Throttle: "); Serial.println(throttle);
  Serial.print("Pitch: "); Serial.println(pitch);
  Serial.print("Roll: "); Serial.println(roll);
  Serial.print("Yaw: "); Serial.println(yaw);
}

void receive(){
  if ((SPSR & (1<<SPIF)) != 0){ //Hat sich der Register verändert?
    if (SPDR == (byte)'R'){
      SPDR = (byte)'A';
      s = 0;
    }else{
      return;//Sollte nicht eintreffen, aber ohne Handshake keine Datenübermittlung
    }
    //Alle Daten werden verschickt
    //50 bytes ca. 200ms
    while (s < sizeof(datenplatzhalter)){
      if ((SPSR & (1<<SPIF)) != 0){
        SPDR = datenplatzhalter[s];
        //Erstes Byte Nutzlos, da noch R von Raspberry
        if (s > 0){
          ci[s-1] = SPDR;
        }
        s++;
        //Empfangen = SPDR
      }
    }
    //Fertig mit übermittlung
  }
}

void constructInputs(){
  throttle = ci[1];
  throttle = (throttle << 8) | ci[0];

  pitch = ci[3];
  pitch = (pitch << 8) | ci[2];

  roll = ci[5];
  roll = (roll << 8) | ci[4];

  yaw = ci[7];
  yaw = (yaw << 8) | ci[6];
}


