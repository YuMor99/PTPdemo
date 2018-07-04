# PTPdemo

##javacvTest
利用javacv库完成获取电脑摄像头的视频流，然后将视频流推送至流媒体服务器（rtmp://ip:port/live），ip为流媒体服务器的ip地址，端口为1935
##nginx
搭建rtmp流媒体服务器
输入命令 nginx.exe -c conf\nginx.conf 启动nginx服务器，
启动成功后，地址栏输入 127.0.0.1:9090 可以看到欢迎页面 
##AndroidClient
android端app代码，源自某个学弟..
输入正确的url,如：rtmp://ip:port/live可获取视频流。