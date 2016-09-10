#include <reg52.h>
#include "uart.h"
#include "Delay.h"

void com_init()
{
	SCON = 0x50;
	TMOD = 0x20 | TMOD;
	TH1 = 0xFA;
	TR1 = 1;
}

void com_send_byte(unsigned char s)
{
	SBUF = s;
	
	while(!TI)
	{
	}
	
	TI = 0;
}

void com_send_str(unsigned char *s)
{
	unsigned char x = 0;
	
	while(s[x] != '\0')
	{
		com_send_byte(s[x]);
		x++;
	}
}

unsigned char com_receive_byte()
{
	RI = 0;
	
	return SBUF;
}

void com_receive_str(unsigned char *s5)
{
	unsigned char i = 0;
	unsigned char temp = 0;
	
	while(i < 13)
	{
		if(RI)
		{
			temp = com_receive_byte();
			
			if(temp == '+')
			{
				i = 0;
			}
		    s5[i] = temp;
		    i++;
	        RI = 0;
		}
	}
}