# IEC104_microgrid
iec104协议主站客户端程序，属于微电网管理系统一部分
## 一 IEC104协议介绍
### 1 IEC104简要说明
IEC104是一种基于TCP/IP的电力行业通信协议，主要用于数据远程监控等功能。通信有一般有主要发送数据、接收命令的从站服务端和接收数据、发送命令的主站客户端构成。采用应答式数据传输，一般上行数据为遥信、遥测，下行信息为遥控、遥调。
### 2 IEC104帧格式
IEC104的通用帧格式如图  
![IEC104帧格式](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/IEC104%E5%B8%A7%E6%A0%BC%E5%BC%8F.png)   
其中APCI为控制信息部分，ASDU为存储数据单元，APDU为长度等于APCI+ASDU-2,即减去起始字节和APDU长度字节。  
IEC104有3种帧格式，分别为U帧即控制报文帧、S帧即监视帧和I帧即信息传输帧。  
1)U帧：只包括APCI部分，主要有启动帧、停止帧、测试帧。U帧具体格式如图  
![U帧格式](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/U%E5%B8%A7%E6%A0%BC%E5%BC%8F.png)  
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
## 三 IEC104主站数据库相关程序设计
### 1 设备数据库模型类实现
IEC104数据库与Web管理系统共用一个数据库，IEC104需建立的数据库模型与web模型的属性、字段都相同，在此基础建立数据库映射操作库model中的映射实现类如图  
![数据设备模型](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E5%A4%87%E6%A8%A1%E5%9E%8B.png)  
其中DevControl为存储所有设备信息及其控制部分的映射实现类，PVAnalogQuantotyData1, PVAnalogQuantotyData1,PVDigtialQuantityData为具体设备(光伏逆变器)类的数字信息和模拟量信息映射实现类。  
### 2 IEC104设备数据库操作类实现
model每个类文件会对应数据库操作dao库中一个类文件，dao的类中会记录数据库操作所需要执行的增删改查操作。目录结构如图：
![数据库设备操作类](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E5%A4%87%E6%93%8D%E4%BD%9C%E7%B1%BB.png)  
以DevControlDao实现类为例简要说明:  
添加设备实现函数核心sql操作语句  
```
Strning sql = "insert into microgrids_devcontrol (num, dev_type) values(?,?)";
```
更新设备类型号码实现函数核心sql操作语句  
```
Strning sql = "update microgrids_devcontrol set dev_type=? where num=?";
```
删除设备类实现函数核心sql操作语句  
```
String sql="delete from microgrid_devcontrol where num=?"
```
查询设备是否存在函数的核心sql操作语句  
```
String sql="select num from microgrid_devcontrol where num=?"
```
### 3 IEC104数据库连接初始化
数据库为考虑大量数据写入时的性能要求，使用了C3P0数据库连接池的方法来增强数据库操作的性能。其中C3P0连接池初始化获得mysql操作对象的类为jdbc包的C3P0Utils类  
```
public class C3P0Utils {
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource("mysql");
	public static DataSource getDataSource() {
		return dataSource;
	}
	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
```
其中，系统会调用主目录下的c3p0-config.xml文件进行数据库初始化配置，包括连接数据库地址，数据库用户和密码。
## 四 主站核心程序设计
### 1 主站设备信息初始化
首先，程序会在初始化完成时加载配置文件dev.json，dev.json数据格式为设备编号，设备名，设备类型编号。（设备类型对应编号在Django model中进行定义，这里写入是为便于Web管理操作）
```
{
  "PVI0101":{"name":"光伏1区光伏逆变器1号", "DEV_TYPE":1},
}
```
根据配置文件的设备信息会去更新数据库DevControl中的设备信息，对配置信息存在数据库却不存在的设备使用dao库DevControl类下的addDev方法进行添加，对配置信息中不存在设备数据库却存在的设备使用delDev方法进行删除。根据设备类型编号去创建对应设备遥信，遥测需要的设备信息结构体，以便在后面获取数据存入对应结构体存储。
### 2 主站遥信、遥测程序设计实现
首先，程序会在初始化加载remote_signal.json、remote_measure.json、遥信、遥测配置文件，配置信息格式主要为信息体地址，对应数据库字段名，对应数据库表单名，对应数据库设备编号。
```
{
  "14":{"field":"status_down","num":"PVI0101","table_name":"pvdigitalquantitydata","descript":"西科逆变器1_停机","注释":""},
}
```
然后，使用多线程Runable类定义遥信、遥测数据写入数据库的方法即将设备对应数据信息结构体（在设备信息初始化中产生）使用对应sql方法写入数据库。使用ScheduledExecutorService类初始化遥信、遥测的多线程定时器得到对象service，再使用service下的scheduleAtFixedRate方法定时执行信息结构体数据写入数据库方法。  
程序在发送完总召唤命令后会一直使用socket的方法去读取从站发送过来的帧数据并进行解析，首先会使用Apdu类下的Apdu方法进行解析，获得帧的启动帧，APDU长度，发送序号，接收序列号，控制域信息，根据控制域信息会进行判断帧类型，即为I帧，S帧，还是U帧。如果是S帧，或U帧获取后基本不做处理，主要会进行I帧解析。再判断是I帧后，会使用Asdu类下的Asdu进行解析，获取帧的类型标识，传输原因，公共地址，可变结构体等信息，然后使用InformationObject类的InformationObject方法去根据帧的类型标识去把数据信息存入到InformationElement，再获取存在信息体地址和数据的InformationObject对象。最后，再使用Client类中handleData方法，根据信息体地址从遥信、遥测配置文件获取其在数据库的表单名，对应字段，对应设备，把信息体对应的数据写入到对应的设备信息结构体中。  
使用PMA软件模拟从站进行遥信测试如下：  
首先启动程序建立链接，并发送总召唤命令，如图   
![主从站建立链路图（PMA）](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/%E4%B8%BB%E4%BB%8E%E7%AB%99%E5%BB%BA%E7%AB%8B%E9%93%BE%E8%B7%AF%E5%9B%BE%EF%BC%88PMA%EF%BC%89.png)  
从站PMA地址发送地址为14，值为0的遥信数据（逆变器PV0101停机信号），如图  
![PMA遥信图](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/PMA%E9%81%A5%E4%BF%A1%E5%9B%BE.png)  
![PMA遥信数据详情](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/PMA%E9%81%A5%E4%BF%A1%E6%95%B0%E6%8D%AE%E8%AF%A6%E6%83%85.png)  
主站接收遥信帧并进行解析，打印解析信息如图  
![主站遥信解析图](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/%E4%B8%BB%E7%AB%99%E9%81%A5%E4%BF%A1%E8%A7%A3%E6%9E%90%E5%9B%BE.png)  
数据库对应更新遥调信息所需要修改数据库数据如图  
![遥信数据库对应数据更新](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/%E9%81%A5%E4%BF%A1%E6%95%B0%E6%8D%AE%E5%BA%93%E5%AF%B9%E5%BA%94%E6%9B%B4%E6%96%B0.png)  
web管理系统界面PVI0101对应数据信息改变如图  
![Web对应遥信显示](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/Web%E5%AF%B9%E5%BA%94%E9%81%A5%E4%BF%A1%E6%98%BE%E7%A4%BA.png)  
### 3 主站遥控、遥调程序设计实现
首先，程序会在初始化加载remote_control.json、remote_adjust.json、遥控、遥调配置文件，配置信息格式主要为信息体地址，对应数据库字段名，对应数据库设备编号。  
 ```
 {
  "25089":{"field":"active_power","num":"PVI0101","descript":"西科逆变器1_有功功率遥调值","注释":""},
}
 ```
然后，定义存储遥控、遥调信息即地址信息和数据信息实时值的字典（记录实时值，是为了只在数据库数据状态改变的情况下，才会去发送遥控或遥信命令）  
```
// 记录存储遥控、遥调实时值
Map<String, Integer> remoteControlValues = new HashMap<String,Integer>();
Map<String, Double> remoteAdjustVlaues = new HashMap<String,Double>();
```
之后，会使用多线程Runable类定义遥控、遥调信息帧组合并发送的runnable_db_send方法。同样使用ScheduledExecutorService类初始化遥信、遥测的多线程定时器得到对象service_A，再使用service_A下的scheduleAtFixedRate方法定时执行。  
Runnable_db_send方法会提取remote_control.json、remote_adjust.json配置文件信息，根据地址信息的字段和编号去数据库对应的表单下去提取数据，对比remoteControlValues、remoteAdjustVlaues字典，如果和字典中信息不同，则更新字典信息并发送遥控、遥调命令。  
使用PMA模拟从站测试如下：  
Web管理界面修改逆变器PV0101的控制信息(会更改对应数据库数据)，如图   
![Web控制信息命令下发图](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/Web%E6%8E%A7%E5%88%B6%E4%BF%A1%E6%81%AF%E5%91%BD%E4%BB%A4%E4%B8%8B%E5%8F%91%E5%9B%BE.png)  
IEC104主站发送相关遥控、遥调命令，并打印信息，如图  
![IEC104主站响应发送遥控、遥调命令图](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/IEC104%E4%B8%BB%E7%AB%99%E5%93%8D%E5%BA%94%E5%8F%91%E9%80%81%E9%81%A5%E6%8E%A7%E3%80%81%E9%81%A5%E8%B0%83%E5%91%BD%E4%BB%A4%E5%9B%BE.png)  
PMA从站会接收到IEC104主站的遥控、遥调信息，如图  
![PMA从站遥控、遥调信息接收图](https://github.com/msun1996/IEC104_microgrid/blob/master/projectInstruction/picture/PMA%E4%BB%8E%E7%AB%99%E9%81%A5%E6%8E%A7%E3%80%81%E9%81%A5%E8%B0%83%E4%BF%A1%E6%81%AF%E6%8E%A5%E6%94%B6%E5%9B%BE.png)  

#### 程序参考学习自 [huarda / IEC104-1](https://github.com/huarda/IEC104-1)
