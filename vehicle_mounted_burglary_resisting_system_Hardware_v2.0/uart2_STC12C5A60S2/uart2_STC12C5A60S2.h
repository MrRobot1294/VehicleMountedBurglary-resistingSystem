#ifndef _UART2_STC12C5A60S2_H_
#define _UART2_STC12C5A60S2_H_

#define S2RI 0x01	
#define S2TI 0x02

extern void uart2_init();

extern void uart2_sendonebyte(unsigned char c);

extern void uart2_send_str1(unsigned char m, unsigned char *s);

extern void uart2_send_str2(unsigned char *s);

extern unsigned char uart2_receive_byte();

extern void uart2_receive_str(unsigned char *s5);

extern void uart2_send_str3(unsigned char m, unsigned char *s);

extern void uart2_send_str4(unsigned char m, unsigned char n, unsigned char *s);

extern void uart2_send_str5(unsigned char *s);

#endif