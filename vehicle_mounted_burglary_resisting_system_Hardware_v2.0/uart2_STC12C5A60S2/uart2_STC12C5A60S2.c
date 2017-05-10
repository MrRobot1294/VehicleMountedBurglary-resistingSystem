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

void uart2_send_str3(unsigned char m, unsigned char *s)
{
	unsigned char x = m;
	
	while(s[x] != '\r')
	{
		uart2_sendonebyte(s[x]);
		x++;
	}
}

void uart2_send_str4(unsigned char m, unsigned char n, unsigned char *s)
{
	unsigned char x = 0;
	while(s[x] != m)
	{
		x++;
	}
	x++;
	
	while(s[x] != n)
	{
		uart2_sendonebyte(s[x]);
		x++;
	}
}

void uart2_send_str5(unsigned char *s)
{
	unsigned char x = 0;
	
	while(s[x] != '\n')
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
	
	while(i < 50 && flag_over)
	{
		if(S2CON&S2RI)
		{
		    s5[i] = uart2_receive_byte();
			
			if(s5[i] == 'K' || s5[i] == 'E' || s5[i] == 'T')
			{
				flag_over = 0;
			}
			
		    i++;
	        S2CON&=~S2RI;
		}
	}
	
//	if(uart2_receive_byte() == '\n')
//	{
//		uart2_receive_byte();
//		uart2_receive_byte();
//		uart2_receive_byte();
//	}
//	i = 10;
//	while(i)
//	{
//		uart2_receive_byte();
//		i--;
//	}
}