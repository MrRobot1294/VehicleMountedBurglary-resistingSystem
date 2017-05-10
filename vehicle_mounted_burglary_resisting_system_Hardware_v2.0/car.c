#include "STC12C5A60S2.H"
#include "uart2_STC12C5A60S2.h"
#include "NRF24L01.h"
#include "gsm900c.h"
#include "gps.h"
#include "Delay.h"
#include "uart.h"
#include "car.h"

sbit LED = P0^0;
sbit BEEP = P0^1;

sbit BE_STOLEN = P1^0;

//bit Flag_OV = 0;
bit Flag_GPGGA_OK = 0;
bit Flag_GPRMC_OK = 0;
bit Flag_SWITCH = 1;
bit Flag_BE_STOLEN = 0;

unsigned char Send_Timer = 0;

int Timer = 0;

void interrupt_init()
{
	EA = 1;           
	ET0 = 1;

    IT0 = 0;
    EX0 = 1;   	
	
	TMOD = 0x01 | TMOD;      
	
	TR0 = 0;
	
	TH0 = (65536 - 50000) / 256;     
	TL0 = (65536 - 50000) % 256;
}

void timer_interrupt1() interrupt 1
{			
	TH0 = (65536 - 50000) / 256;
	TL0 = (65536 - 50000) % 256;
	
	Timer++;
	if(Timer % 10 == 0)
	{
	    BEEP = ~BEEP;
		//BEEP = 0;
	}
	
// 	uart2_sendonebyte('1');
// 	com_send_byte('2');
	
	if(Timer == 1200)
	{
		Timer = 0;
		
			com_send_byte('2');
		
		Send_Timer--;
		
	}
}

void Int0_ISR() interrupt 0
{
    unsigned char RxDate[5]={0};

    if( NRFRevDate(RxDate) )
    {
        if(RxDate[0]==1&&RxDate[2]==3&&RxDate[1]==2&&RxDate[3]==4)
        { 
//            com_send_str(4, RxDate);
            Delay_ms(200);
			
			if(Flag_SWITCH)
			{
				Flag_SWITCH = 0;
				LED = 1;
			}
			else
			{
				Flag_SWITCH = 1;
				LED = 0;
			}
        } 
    }
    NRFSetRXMode();
}


void main()
{
	unsigned char i = 0;
	
	LED = 0;
	BEEP = 1;
	
	BE_STOLEN = 1;
	
	uart2_init();
	
	com_init();
	
	interrupt_init();
	
	tCPLinks2();
	Delay_s(40);
	
//	NRF24L01Int();
//	NRFSetRXMode(); 
		
	while(1)
	{
		if(Flag_SWITCH == 1)
		{
			gps_init();
			
			TR0 = 1;
			if((BE_STOLEN == 1) && (Flag_BE_STOLEN == 0))
			{
				Send_Timer = 0;
			
				GPGGA_Buffer[17] = 'A';
				
				Flag_BE_STOLEN = 1;
				
			}
			if(BE_STOLEN == 0)
			{
				Flag_BE_STOLEN = 0;
			}
			while((Flag_GPS_OK != 1) && (Send_Timer <= 0))
			{
				//TR0 = 0;
				
				Send_Timer = 0;
				
				gps_receive();
			}

			if((Send_Timer == 0) && 
				(Flag_GPS_OK == 1) && 
				((GPGGA_Buffer[17] != RX_Buffer[17] || GPGGA_Buffer[18] != RX_Buffer[18] || GPGGA_Buffer[19] != RX_Buffer[19] || GPGGA_Buffer[20] != RX_Buffer[20] || GPGGA_Buffer[22] != RX_Buffer[22] || GPGGA_Buffer[23] != RX_Buffer[23] || GPGGA_Buffer[24] != RX_Buffer[24]) ||
			    (GPGGA_Buffer[30] != RX_Buffer[30] || GPGGA_Buffer[31] != RX_Buffer[31] || GPGGA_Buffer[32] != RX_Buffer[32] || GPGGA_Buffer[33] != RX_Buffer[33] || GPGGA_Buffer[34] != RX_Buffer[34] || GPGGA_Buffer[36] != RX_Buffer[36] || GPGGA_Buffer[37] != RX_Buffer[37] || GPGGA_Buffer[38] != RX_Buffer[38]))
			  )
			{
				
				for(i = 0; i < 68; i++)
				{
					GPGGA_Buffer[i] = RX_Buffer[i];
				}
				
				Flag_GPGGA_OK = 1;
				
				if(BE_STOLEN == 1)
				{
					carLocationSend('1');
					
				} else {
					carLocationSend('0');
				}
			}
			
			if(Flag_GPGGA_OK == 1 && BE_STOLEN == 1)
			{
				full_C_Word();
				
				Flag_GPGGA_OK = 0;
			}
			Flag_GPGGA_OK = 0;
			
			Flag_GPS_OK = 0;
			
			Send_Timer = 3;
			
			TR0 = 1;
		}
		else
		{
			interrupt_init();
			
			BEEP = 1;
			
			Send_Timer = 0;
			
			GPGGA_Buffer[17] = 'A';
		}
		
//		message_receive();
		
		if(Flag_SWITCH)
	    {
			LED = 0;
		}
		else
		{
		    LED = 1;
		}
		
// // 		com_send_byte('1');
// 		uart2_send_str2(Control[0]);
	}

//	tCPClose();
	
//	while(1)
//	{
//	}
}







