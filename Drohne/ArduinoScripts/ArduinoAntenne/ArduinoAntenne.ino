#include <SPI.h>

#define CE 7
#define TXE 9
#define PWR 8
#define CD 4
#define AM 2
#define DR 3
#define CS 10

//Transmit and Receive Information
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial.println("Start");
  //Serial.println(16,BIN);
  SPI.setClockDivider(SPI_CLOCK_DIV128);
  SPI.setBitOrder(MSBFIRST);
  SPI.setDataMode(SPI_MODE0);
  SPI.begin();

  //Pinmodes
  pinMode(TXE,OUTPUT);
  pinMode(CE,OUTPUT);
  pinMode(PWR,OUTPUT);
  pinMode(CS,OUTPUT);

  pinMode(CD,INPUT);
  pinMode(AM,INPUT);
  pinMode(DR,INPUT);

  //SPI Pinmodes
  pinMode(MISO, INPUT);
  pinMode(MOSI, OUTPUT);
  pinMode(13,OUTPUT);

  digitalWrite(CS,HIGH); //nRF disabled

  digitalWrite(TXE, LOW);
  digitalWrite(PWR, LOW);
  digitalWrite(CE, LOW);
  configure();

}
char toSend[100];
int index = 0;
void loop() {
  // put your main code here, to run repeatedly:

  //Eingabe? Senden?
  if (Serial.available()){
    char rd = Serial.read();
    toSend[index] = rd;
    index++;
    if (rd == '\n'){
      sendData();
      //clear buffer
      clearSendBuffer();
    }
  }

  //Empfangen? Ausgeben?
}

//----------------------------------------CONFIGURING-----------------------------------

void configure(){
  //Configure Outputs:
  digitalWrite(PWR,HIGH); //Power down
  digitalWrite(CE,HIGH);
  digitalWrite(TXE,HIGH);
  //No Power neither send nor receive enabled
  //Ready for Programming

  //Frequenz: 864,8MHz (Legal: 863 - 865 MHz (EU-weites Harmonized Frequency Band); https://www.shure.de/support/frequenzen#anmeldefrei
  //CH_NO: 100 (Dec.) 64 (Hex)
  writeConfigRegister(0x0,0x64);
  //Check content
  byte content = readConfigRegister(0x0);
  Serial.print("Content Register 0: ");
  Serial.println(content,BIN);
  //HFREQ_PLL: 1

  //Output Power: -10dBm PA_PWR: 00

  //RX-address width: 100 (4 bytes) RX_AFW: 100

  //TX-address width: 100 (4bytes) TX_AFW: 100

  //RX Payload width: 32 bytes RX_PW: 100000

  //TX Payload width: 32 bytes TX_PW: 100000

  //RX-address 32 bits -> 4bytes: 956BCCB6 (HEX)

  //No External Clock UP_CLK_FREQ: 0
}

void writeConfigRegister(byte registerNum, byte content){
  byte instr = registerNum & 0b00001111; //Instruction = 0000AAAA (AAAA => Address/ registerNum)
  Serial.println(instr,BIN);
  //Enable SPI
  digitalWrite(CS,LOW);
  //Transfer instruction and content
  SPI.transfer(instr);
  SPI.transfer(content);
  //Disable SPI
  digitalWrite(CS,HIGH);
}

byte readConfigRegister(byte registerNum){
  byte instr = registerNum & 0b00001111; //Instruction = 0001AAAA (AAA => Address/ registerNum);
  instr |= 0b00010000;
  Serial.print("instr: ");
  Serial.println(instr,BIN);
  //Enable SPI
  digitalWrite(CS,LOW);
  //delay(1);
  //Transfer Instruction
  SPI.transfer(instr);
  //Gather Information
  byte result = SPI.transfer(0x00);
  //Disable SPI
  digitalWrite(CS,HIGH);
  SPI.end();
  return result;
}

//-----------------------------------------RECEIVING------------------------------------

//----------------------------------------- SENDING ------------------------------------
void sendData(){
  Serial.print("To Send: ");
  Serial.print(toSend);
//PWR_UP => HIGH, Wait

  //StartLoop
  //TX_EN => HIGH, PWR_UP => HIGH, CE => LOW
  //Wait for chip ready

  //SPI programming

  //CE => HIGH
  //Sending package

  //DR HIGH? -> Transmission Completed
  //CE -> LOW

  //LOOP
}

void clearSendBuffer(){
  int i = 0;
  while (i <= index){
    toSend[i] = 0;
    i++;
  }
  index = 0;
}
