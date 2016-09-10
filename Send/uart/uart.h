#ifndef _UART_H_
#define _UART_H_

extern void com_init();

extern void com_send_byte(unsigned char s);

extern void com_send_str(unsigned char *s);

extern unsigned char com_receive_byte();

extern void com_receive_str(unsigned char *s5);

#endif