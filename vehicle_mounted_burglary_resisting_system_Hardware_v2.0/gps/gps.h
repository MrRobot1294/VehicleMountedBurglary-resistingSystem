#ifndef _GPS_H_
#define _GPS_H_

extern bit Flag_GPS_OK;

extern unsigned char xdata RX_Buffer[68];
extern unsigned char xdata RX_Count;

extern void gps_init();

extern void gps_receive();

#endif