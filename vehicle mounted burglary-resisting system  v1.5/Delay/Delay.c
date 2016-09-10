#include "STC12C5A60S2.H"
#include "Delay.h"

void Delay_us(unsigned char t)
{
	while(--t);
}

void Delay_ms(unsigned char t)
{
	while(t--)
	{
		Delay_us(245);
		Delay_us(245);
	}
}

void Delay_s(unsigned char t)
{
	unsigned char i, j;
	for(i = 0; i < t; i++)
	{
		for(j = 0; j < 5; j++)
		{
			Delay_ms(200);
		}
	}
}