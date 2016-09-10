#include <reg52.h>
#include "NRF24L01.h"
#include "Delay.h"
#include "uart.h"
#include "ESP8266.h"

sbit LED = P0^0;

unsigned char x = 0;

unsigned char wifi[13] = {0};

void Int0_Init()
{
    EA = 1;
    IT0 = 0;
    EX0 = 1;
}

void main()
{
   unsigned char TxDate[5]={1, 2, 3, 4};
   unsigned char RxDate[5]={0};

   com_init();
   NRF24L01Int();
   esp8266_init();

LED = 1;

   while(1)
   {
	   com_receive_str(wifi);
	   
// 	   NRFSetTxMode(TxDate);//·¢ËÍÎÂ¶È
//            if(!CheckACK())    //¼ì²âÊÇ·ñ·¢ËÍÍê±Ï
//            { 
//                delay_ms(200);
//            }
	   
	   if(wifi[9] == '1' && wifi[10] == '9' && wifi[11] == '9' && wifi[12] == '4')
	   {
		   LED = 0;
		   
		   NRFSetTxMode(TxDate);//·¢ËÍÎÂ¶È
           if(!CheckACK())    //¼ì²âÊÇ·ñ·¢ËÍÍê±Ï
           { 
               delay_ms(200);
           }
		   
		   wifi[9] = '0';
		   delay_ms(200);
	   }
	   LED = 1;
   
   }
}

void Int0_ISR() interrupt 0
{
    ;
}

// void uart_receive() interrupt 4
// {
// 	unsigned char temp = 0;
// 	
//     ES = 0;
// 	
// 	if(RI)
// 	{
// 		temp = com_receive_byte();
// 		
// 		if(temp == '+')
// 		{
// 			x = 0;
// 		}
// 		wifi[x] = temp;
// 		x++;
// 	    RI = 0;
// 	}
// 	
// 	ES = 1;
// }
