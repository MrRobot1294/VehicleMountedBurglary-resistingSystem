# VehicleMountedBurglary-resistingSystem V2.0
stc12c5a60s2  gtm900c gps Spring Hibernate MySQL Android 高德地图API

具有被盗消息短信通知，用户注册登录，用户车辆增删，用户车辆实时位置状态查询，用户车辆历史位置状态与轨迹查询功能。

Hardware:
1.将gsm900c.c文件第23行XXXXXXXXXXFX替换为需要短信通知的手机号码。
  例如：手机号123456789AB 填为 21436587A9FB
2.将gsm900c.c文件第485、486行XXX.XXX.XXX.XXX替换为服务器IP地址。
3.将gsm900c.c文件第487、488行XXXX替换为服务器端口号。
4.将gsm900c.c文件第38行XXXXXXXX替换为车辆编号的16进制ASCII。
  例如：10000 填为 3130303030
  
Server：
1.将TCPServerImpl.java文件第23、24行XXXX替换为服务器端口号。
2.将applicationContext.xml文件第34行XXXX替换为MySQL数据库名。
3.将applicationContext.xml文件第35行XXXX替换为MySQL数据库用户名。
4.将applicationContext.xml文件第36行XXXX替换为MySQL数据库用户密码。

APP:
1.将AndroidManifest.xml文件第46行XXXX替换为高德地图Key码。
2.将MyService.java文件第195行XXX.XXX.XXX.XXX替换为服务器IP地址。
3.将MyService.java文件第35行XXXX替换为服务器端口号。

PCB：
1.GPS引脚接gps模块串口。
2.GSM引脚接GTM900c模块串口。