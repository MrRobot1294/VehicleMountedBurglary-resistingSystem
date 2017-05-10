#include "STC12C5A60S2.H"
#include "uart2_STC12C5A60S2.h"
#include "Delay.h"
#include "gps.h"
#include "uart.h"

bit Flag_GPS_OK = 0;

unsigned char xdata RX_Buffer[68] = {0};
unsigned char xdata RX_Count = 0;

void gps_init()
{
	SCON = 0x50;
	REN  = 1;
	TMOD = 0x20 | TMOD;
	TH1 = 0xFD;
	TR1 = 1;
// 	EA   = 1;     
// 	ES   = 1;
}

void gps_receive() 		
{ 
	unsigned char temp = 0;
	
	ES = 0;
	
	temp = SBUF;
	
	if(temp == '$')
	{
		RX_Count = 0;
		Flag_GPS_OK = 0;		
	}

	RX_Buffer[RX_Count++] = temp;
	
// 	uart2_sendonebyte('1');
	
	com_send_byte('1');
	
// 	Delay_s(1);

	if(RX_Count == 50)
	{
		if(RX_Buffer[4] == 'G' && RX_Buffer[6] == ',' && RX_Buffer[17] != ',')
		{
			Flag_GPS_OK = 1;
// 			uart2_send_str1(68, RX_Buffer);			
		}
        		
	}

	RI = 0;
}

