#include "STC12C5A60S2.H"
#include "uart2_STC12C5A60S2.h"

void uart2_init()
{
	S2CON = 0x50;   
    BRT = 0xFD;	   
    AUXR = AUXR | 0x10;    
//     IE2 =0x01;	
}

void uart2_sendonebyte(unsigned char c)
{
    S2BUF = c;
	
    while(!(S2CON&S2TI))
	{
    }
	
    S2CON&=~S2TI;
//     S2CON &= 0xFD;	
}

void uart2_send_str1(unsigned char m, unsigned char *s)
{
	unsigned char x = 0;
	
	while(x < m)
	{
		uart2_sendonebyte(s[x]);
		x++;
	}
}

void uart2_send_str2(unsigned char *s)
{
	unsigned char x = 0;
	
	while(s[x] != '\0')
	{
		uart2_sendonebyte(s[x]);
		x++;
	}
}

unsigned char uart2_receive_byte()
{
	if(S2CON&S2RI)
	{
		S2CON&=~S2RI;
		
		return S2BUF;
	}
	
	return 0;
}

//void uart2_receive_str(unsigned char *s5)
//{
//	unsigned char i = 0;
//	
//	while(i < 10)
//	{
//		if(S2CON&S2RI)
//		{
//		 s5[i] = uart2_receive_byte();
//		 i++;
//	     S2CON&=~S2RI;
//		}
//	}
//}

void uart2_receive_str(unsigned char *s5)
{
	unsigned char i = 0;
	
	unsigned char flag_over = 1;
	
	while(i < 200 && flag_over)
	{
		if(S2CON&S2RI)
		{
		    s5[i] = uart2_receive_byte();
			
			if(s5[i] == 'O')
			{
				flag_over = 0;
			}
			
		    i++;
	        S2CON&=~S2RI;
		}
	}
}