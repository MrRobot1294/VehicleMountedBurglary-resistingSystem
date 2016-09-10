#include <reg52.h>

void delay_us(unsigned char t) 
{
		while(--t);
}

void delay_ms(unsigned char t) 
{
		while(t--)
		{
				delay_us(245);
			  delay_us(245);
		}
}

void delay_s(unsigned char t)
{
		unsigned char i,j;
	  for(j = 0; j < t; j++)
	  {
			for(i = 0; i < 5; i++)
			{
					delay_ms(200);
			}
		}
}