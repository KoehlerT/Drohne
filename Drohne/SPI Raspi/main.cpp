#include "SPI.h"
#include <iostream>
#include <ctime>

unsigned char hello[] = {'H','e','l','l','o',' ',
                           'A','r','d','u','i','n','o'};
unsigned char result;


int main (void)
{

    init(100000);

    std::cout << "Initialized" << std::endl;

    while (1)
    {

        clock_t begin = clock();
       shakeHands(10);
       for (int i = 0; i < 10; i++)
       {
          result = spiTxRx((char)i);
          std::cout << (int)result;
          usleep (10);
       }

       clock_t end = clock();
  double elapsed_secs = double(end - begin) / CLOCKS_PER_SEC;
        std::cout << "Time: " << elapsed_secs << "s" << std::endl;
    }


}
