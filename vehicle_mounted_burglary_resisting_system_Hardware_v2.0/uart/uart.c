#include "STC12C5A60S2.H"
#include "uart.h"
#include "Delay.h"

void com_init()
{
	SCON = 0x50;
	TMOD = 0x20 | TMOD;
	TH1 = 0xFD;
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

void com_send_str(unsigned char m, unsigned char *s)
{
	unsigned char x = 0;
	
	while(x < m)
	{
		com_send_byte(s[x]);
		x++;
	}
}

void com_send_str2(unsigned char *s)
{
	unsigned char x = 0;
	
	while(s[x] != '\0')
	{
		com_send_byte(s[x]);
		x++;
	}
}

void com_send_str3(unsigned char m, unsigned char *s)
{
	unsigned char x = m;
	
	while(s[x] != '\r')
	{
		com_send_byte(s[x]);
		x++;
	}
}

void com_send_str4(unsigned char m, unsigned char n, unsigned char *s)
{
	unsigned char x = 0;
	while(s[x] != m)
	{
		x++;
	}
	
	while(s[x] != n)
	{
		com_send_byte(s[x]);
		x++;
	}
}

void com_send_str5(unsigned char *s)
{
	unsigned char x = 0;
	
	while(s[x] != '\n')
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
	
	while(i < 10)
	{
		if(RI)
		{
		 s5[i] = com_receive_byte();
		 i++;
	     RI = 0;
		}
	}
}