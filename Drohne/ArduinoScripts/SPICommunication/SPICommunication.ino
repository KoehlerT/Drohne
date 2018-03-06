//Danke an robotics.hobbizine.com/raspiduino.html
//Import

//Global Variables
int s = 0;
byte datenplatzhalter[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

//Setup
void setup() {
  //Set up SPI
  pinMode(MISO,OUTPUT);
  SPCR |= _BV(SPE);
  //Set up Serial Connection
  Serial.begin(9600);
  Serial.println("Gestartet");
  Serial.print("Größen: "); Serial.print(sizeof(char)); Serial.print(", "), Serial.println(sizeof(int));
}

void loop() {
  // put your main code here, to run repeatedly
  //TEURER CODE, z.B. Sensoren auslesen
  delay(1000);
  receive();
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
        s++;
        //Empfangen = SPDR
      }
    }
    //Fertig mit übermittlung
  }
}


