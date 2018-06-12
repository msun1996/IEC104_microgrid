# IEC104_microgrid
iec104协议主站客户端程序，属于微电网管理系统一部分
## 一 IEC104协议介绍
### 1 IEC104简要说明
IEC104是一种基于TCP/IP的电力行业通信协议，主要用于数据远程监控等功能。通信有一般有主要发送数据、接收命令的从站服务端和接收数据、发送命令的主站客户端构成。采用应答式数据传输，一般上行数据为遥信、遥测，下行信息为遥控、遥调。
### 2 IEC104帧格式
IEC104的通用帧格式如图  
！[IEC104帧格式](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/IEC104%E5%B8%A7%E6%A0%BC%E5%BC%8F.png)   
其中APCI为控制信息部分，ASDU为存储数据单元，APDU为长度等于APCI+ASDU-2,即减去起始字节和APDU长度字节。  
IEC104有3种帧格式，分别为U帧即控制报文帧、S帧即监视帧和I帧即信息传输帧。  
1)U帧：只包括APCI部分，主要有启动帧、停止帧、测试帧。U帧具体格式如图  
！[U帧格式](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/U%E5%B8%A7%E6%A0%BC%E5%BC%8F.png)  
2)S帧：只包含APCI部分。S帧格式如图
![S帧格式](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/S%E5%B8%A7%E6%A0%BC%E5%BC%8F.png)  
3)I帧：包含APCI+APDU部分。I帧格式如图
![I帧格式](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/I%E5%B8%A7%E6%A0%BC%E5%BC%8F.png)  
发送序号和接收序列号是保证数据完整性的条件。  
类型标识定义发送数据的格式。  
可变结构体定义发送数据信息是有序还是无序，有序即一个信息体地址，元素的对应地址会在此信息地址基础上依次加1。无序即1个地址对应一个元素。  
传输原因定义记录传送的原因，用以对传输数据进行归类。  

### 3 IEC104规约流程
1.由客户端向服务器建立连接，同时，发送链路启动帧。  
2.服务端在收到链路启动帧后，向客户端发送启动确认帧。  
3.客户端收到启动确认帧后，发送总召唤命令数据请求帧。  
4.服务端收到总召唤命令数据请求后，发送总召唤命令数据响应帧，然后继续发送总召唤命令数据。总召唤命令数据发送完成后，发送总召唤命令数据结束帧。  
5.客户端在收到总召唤命令数据结束帧后，发送对时请求帧。  
6.服务器收到对时请求帧后，发送对时响应帧。  
7.由服务器主动向客户端发送变化数据帧。同时，收到客户端发送的控制类命令，回复相应的操作结果。  
8.客户端等到下一个数据总召唤命令数据周期，重复第4步之后的流程。  
## 二 IEC104通信主站程序总体设计实现
根据项目需求和IEC104通信规约设计，主要包括遥信、遥测的解析程序功能，遥控、遥调的组合程序功能，数据库并发操作。涉及技术Java面对对象编程，多线程，socket编程，JDBC数据库操作映射API，C3P0高并发数据库连接池。  
首先设计如图的目录结构：  
![主目录结构](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/IEC104%E4%B8%BB%E7%9B%AE%E5%BD%95%E7%BB%93%E6%9E%84.png)  
其中jdbc 库文件存储数据库连接相关配置的类文件，model库文件存储着数据库的对应模型的类结构文件，dao库文件存储着数据库操作的类函数文件，iec104.util存储着程序处理中所用到的工具类，iec104为程序逻辑主体，存储着程序的主要处理类函数文件。  
设计如图的配置文件目录  
![配置文件目录](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/IEC104%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E7%9B%AE%E5%BD%95.png)  
其中typeId.properties存储着功能类型的数字对应信息汉字关系，cause.properties存储着原因类型数字对应信息汉字关系，用于帧格式解析。remote_signal、remote_measure、remote_control、remote_adjust是存储信息体地址和对应存储数据库字段关系文件，用于将设备状态信息存储到数据库对应数据模型或从对应数据进行数据提取。  
设计主体程序有3部分构成，主程序包括配置信息初始化，Socket端口连接初始化，数据库连接配置初始化，线程定时器初始化，启动链路，接收遥信、遥测帧信息解析缓存到对应对象结构体；定时将缓存在遥信、遥测对象结构体的数据写入数据库；遥控、遥调定时器程序包括根据读取数据库对应数据信息与缓存字典比较，发现变化信息组合帧信息并发送。设计总体流程图如图  
![IEC104主站程序流程图](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/IEC%E4%B8%BB%E7%AB%99%E7%A8%8B%E5%BA%8F%E6%B5%81%E7%A8%8B%E5%9B%BE.png)  
## 三 
