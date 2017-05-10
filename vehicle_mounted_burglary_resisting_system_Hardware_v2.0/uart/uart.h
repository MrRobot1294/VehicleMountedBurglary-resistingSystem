#ifndef _UART_H_
#define _UART_H_

extern void com_init();

extern void com_send_byte(unsigned char s);

extern void com_send_str(unsigned char m, unsigned char *s);

extern unsigned char com_receive_byte();

extern void com_receive_str(unsigned char *s5);

extern void com_send_str2(unsigned char *s);

extern void com_send_str3(unsigned char m, unsigned char *s);

extern void com_send_str4(unsigned char m, unsigned char n, unsigned char *s);

extern void com_send_str5(unsigned char *s);

#endif