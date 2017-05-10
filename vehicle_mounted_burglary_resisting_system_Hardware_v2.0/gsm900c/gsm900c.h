#ifndef _GSM900C_H_
#define _GSM900C_H_

extern unsigned char xdata GPGGA_Buffer[68];
//extern unsigned char xdata GPRMC_Buffer[68];

extern unsigned char SEC_TEMP[2];

extern int SEC1;


extern unsigned char a[4];

extern unsigned char xdata Control[2][20];

extern unsigned char xdata C_Word[8][40];

extern unsigned char xdata Send[3];

extern unsigned char xdata C_Words[250];

extern unsigned char xdata Rewrite_Close[7];

//extern unsigned char xdata E_Control[2][20];

//extern unsigned char xdata Unread_Message[25];
//	
//extern unsigned char xdata Read_Message_Clean[15];

extern unsigned char xdata Message_Received[50];

extern unsigned char xdata CarNumber[20];

extern void DEC_change_to_HEX(unsigned char b);

extern void MIN_change_to_SEC();

extern void full_C_Word();

extern void message_find();

//extern void message_receive();

extern void tCPClose();

extern void tCPSend(unsigned char *s);

extern void tCPLinks2();
extern void carLocationSend(unsigned char stolen);

#endif