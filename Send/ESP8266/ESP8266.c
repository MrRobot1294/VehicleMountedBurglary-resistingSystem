#include <reg52.h>
#include "uart.h"
#include "Delay.h"
#include "ESP8266.h"



char *cmd[] = { "AT\r\n",																					
								"ATE0\r\n",																					//关闭回显	
								"AT+CWMODE=2\r\n",																	//WiFi模式，AP模式
								"AT+CWSAP=\"WiFiCar\",\"0123456789\",1,4\r\n",			    //AP模式下参数设置，ssid，pwd，通道号
								"AT+RST\r\n",																				//重启模块使设置生效
								"ATE0\r\n",																					//关闭回显
								"AT+CIPMUX=1\r\n",																	//设启动多连接
								"AT+CIPSERVER=1,8086\r\n",													//配置TCP服务器，开启server模式，监听端口号
								"AT+CIPSTO=0\r\n"};																	//设置连接超时，0为永远不会超时

void esp8266_init()
{
	unsigned char i;
	
	for(i = 0; i < 9; i++)
	{
		com_send_str(cmd[i]);
		delay_ms(250);
		delay_ms(250);
		if(4 == i)																											//等待模块重启
		{
			delay_ms(250);
			delay_ms(250);
		}
	}
}