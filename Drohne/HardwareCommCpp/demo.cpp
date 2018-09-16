#include <iostream>
#include <cstring>
#include <fcntl.h>
#include <linux/spi/spidev.h>
#include <sys/ioctl.h>

using namespace std;

int fd;
unsigned char result;

int spiTxRx(unsigned char dat);

int main(void){
  cout << "Hello CPP\n";
  fd = open("/dev/spidev0.0",O_RDWR);
  unsigned int speed = 1000000;

  ioctl(fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed);

  while (1){
    for (unsigned char i = 0; i < 5; i++){
      result = spiTxRx(i);
      cout << (short)result;
      cout << '\n';
      usleep (10);
    }
  }
  return 0;
}

int spiTxRx(unsigned char dat){
  unsigned char rxDat;
  struct spi_ioc_transfer spi

  memset (&spi, 0, sizeof(spi));

  spi.tx_buf = (unsigned long)&txDat;
  spi.rx_buf = (unsigned long)&rxDat;
  spi.len = 1;

  ioctl (fd, SPI_IOC_MESSAGE(1), &spi);

  return rxDat;
}
