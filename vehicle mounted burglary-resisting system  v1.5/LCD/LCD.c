#include "STC12C5A60S2.H"
#include "LCD.h"
#include "Delay.h"

void LCD_init()
{
	unsigned char t = 16;
	
	E = 0;
	
	Delay_ms(15);
	write_com(0x38);
	Delay_ms(5);
	write_com(0x38);
	Delay_ms(5);
	write_com(0x38);
	Delay_ms(5);
	
	write_com(0x38);
	write_com(0x0F);
	write_com(0x06);
	
	
	write_com(0x01);
	write_com(0x80);
}

void write_com(unsigned char con)
{
	RS = 0;
	RW = 0;
	P0 = con;
	Delay_ms(5);
	
	E = 1;
	Delay_ms(5);
	E = 0;
}

void write_data(unsigned char dat)
{
	RS = 1;
	RW = 0;
	P0 = dat;
	Delay_ms(5);
	
	E = 1;
	Delay_ms(5);
	E = 0;
}

void write_data_char(unsigned char x, unsigned char y, unsigned char s)
{
	if(x == 1)
	{
		write_com(0x80 + 0x00 + y);
	}
	else
	{
		write_com(0x80 + 0x40 + y);
	}

    write_data(s);
}

void write_data_str(unsigned char x, unsigned char y, unsigned char *s)
{
	if(x == 1)
	{
		write_com(0x80 + 0x00 + y);
	}
	else
	{
		write_com(0x80 + 0x40 + y);
	}
	
	while(*s)
	{
		write_data(*s);
		s++;
	}
}