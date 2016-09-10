#ifndef _LCD_H_
#define _LCD_H_

sbit RS = P2^4;
sbit RW = P2^5;
sbit E = P2^6;

extern void LCD_init();

extern void write_com(unsigned char con);

extern void write_data(unsigned char dat);

extern void write_data_char(unsigned char x, unsigned char y, unsigned char s);

extern void write_data_str(unsigned char x, unsigned char y, unsigned char *s);

#endif