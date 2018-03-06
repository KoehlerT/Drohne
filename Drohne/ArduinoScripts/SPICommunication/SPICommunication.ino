//Danke an robotics.hobbizine.com/raspiduino.html
//Import

//Global Variables
int s = 0;


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
  //TEURER CODE
  //delay(1000);
  receive();
}

void receive(){
  if ((SPSR & (1<<SPIF)) != 0){ //Hat sich der Register verändert?
    byte res = SPDR;
    switch(res){
      case (byte)'R':
        SPDR = (byte) 'A';
        s = 0;
        break;
       case 0:
        break;
      default:
        SPDR = s++;
    }
    //SPDR = 10;
    //delay(2);
  }
}


