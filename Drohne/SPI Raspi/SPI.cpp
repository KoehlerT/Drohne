/**********************************************************
 SPI_Hello_Arduino
   Configures an Raspberry Pi as an SPI master and
   demonstrates bidirectional communication with an
   Arduino Slave by repeatedly sending the text
   "Hello Arduino" and receiving a response

Compile String:
g++ -o SPI_Hello_Arduino SPI_Hello_Arduino.cpp
***********************************************************/
#include "SPI.h"

#include <sys/ioctl.h>
#include <linux/spi/spidev.h>
#include <fcntl.h>
#include <cstring>



/**********************************************************
Declare Global Variables
***********************************************************/

int fd;
unsigned int speed;

/**********************************************************
Main
  Setup SPI
    Open file spidev0.0 (chip enable 0) for read/write
      access with the file descriptor "fd"
    Configure transfer speed (1MkHz)
  Start an endless loop that repeatedly sends the characters
    in the hello[] array to the Ardiuno and displays
    the returned bytes
***********************************************************/

int init(unsigned int spd){
    speed = spd;
    fd = open("/dev/spidev0.0", O_RDWR);

    ioctl (fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed);
    return fd;
}


/**********************************************************
spiTxRx
 Transmits one byte via the SPI device, and returns one byte
 as the result.

 Establishes a data structure, spi_ioc_transfer as defined
 by spidev.h and loads the various members to pass the data
 and configuration parameters to the SPI device via IOCTL

 Local variables txDat and rxDat are defined and passed by
 reference.
***********************************************************/

int spiTxRx(unsigned char txDat)
{

  unsigned char rxDat;

  struct spi_ioc_transfer spi;

  memset (&spi, 0, sizeof (spi));

  spi.tx_buf        = (unsigned long)&txDat;
  spi.rx_buf        = (unsigned long)&rxDat;
  spi.len           = 1;

  ioctl (fd, SPI_IOC_MESSAGE(1), &spi);

  return rxDat;
}

void shakeHands(int nanodelay){
    while (spiTxRx('R') != 'A'){
        usleep(nanodelay);
    }
}
