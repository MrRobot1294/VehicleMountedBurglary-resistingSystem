#include <reg52.h>
#include "uart.h"
#include "Delay.h"
#include "ESP8266.h"



char *cmd[] = { "AT\r\n",																					
								"ATE0\r\n",																					//�رջ���	
								"AT+CWMODE=2\r\n",																	//WiFiģʽ��APģʽ
								"AT+CWSAP=\"WiFiCar\",\"0123456789\",1,4\r\n",			    //APģʽ�²������ã�ssid��pwd��ͨ����
								"AT+RST\r\n",																				//����ģ��ʹ������Ч
								"ATE0\r\n",																					//�رջ���
								"AT+CIPMUX=1\r\n",																	//������������
								"AT+CIPSERVER=1,8086\r\n",													//����TCP������������serverģʽ�������˿ں�
								"AT+CIPSTO=0\r\n"};																	//�������ӳ�ʱ��0Ϊ��Զ���ᳬʱ

void esp8266_init()
{
	unsigned char i;
	
	for(i = 0; i < 9; i++)
	{
		com_send_str(cmd[i]);
		delay_ms(250);
		delay_ms(250);
		if(4 == i)																											//�ȴ�ģ������
		{
			delay_ms(250);
			delay_ms(250);
		}
	}
}