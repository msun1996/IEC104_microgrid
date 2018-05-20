package iec104;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import dao.DevControlDao;
import iec104.IeShortFloat;
import iec104.IeSinglePointWithQuality;

import iec104.util.ChangeUtils;


public class Client {
	public static void main(String[] args) {
		try {
            //系统初始化
            Init.start();
			// 建立对服务端主机的Socket连接
			Socket socket = new Socket("127.0.0.1", 2404);
			// 由Socket对象得到输出流，并构造PrintWriter对象
			OutputStream os = socket.getOutputStream();
			// 启动链路
			os.write(ChangeUtils.hexStringToBytes("680407000000"));
			// 发送总召唤命令
			os.write(ChangeUtils.hexStringToBytes("680E0000000064010600010000000014"));
			// 由Socket对象得到输入流，构造相应的BufferedReader对象
			InputStream is = socket.getInputStream();
			while(true){
                try {
                    Apdu apdu = new Apdu(new DataInputStream(is));
                    if (apdu.getApciType() == Apdu.ApciType.I_FORMAT) {
                        Asdu asdu = apdu.getAsdu();
                        //  处理I命令
                        handleData(asdu.getTypeId(),asdu.getInformationObjects());
                        //  返回S确认命令
                        int receiveSeqNum = apdu.getSendSeqNumber() + 1;
                        byte[] recNum = new byte[2];
                        recNum[0] = (byte) (receiveSeqNum << 1);
                        recNum[1] = (byte) (receiveSeqNum >> 7);
                        String recStr = ChangeUtils.toHexString(recNum);
                        os.write(ChangeUtils.hexStringToBytes("68040100" + recStr));
                        System.out.println("确认消息，S类型，下一条的接受序号：" + recStr);
                    }else if (apdu.getApciType() == Apdu.ApciType.STARTDT_ACT) {
                        os.write(ChangeUtils.hexStringToBytes("68040B000000"));
                        System.out.println("确认启动消息，U类型");
                    }else if (apdu.getApciType() == Apdu.ApciType.STOPDT_ACT) {
                        os.write(ChangeUtils.hexStringToBytes("680423000000"));
                        System.out.println("确认停止消息，U类型");
                    }else if (apdu.getApciType() == Apdu.ApciType.TESTFR_ACT) {
                        os.write(ChangeUtils.hexStringToBytes("680483000000"));
                        System.out.println("确认测试消息，U类型");
                    }else{
                    	System.out.println("其它报文：" + apdu.getApciType());
                    }
                }catch (Exception e){
                	System.out.println("异常错误,"+ e);
                    break;
                }
			}
			os.close(); // 关闭Socket输出流
			is.close(); // 关闭Socket输入流
			socket.close(); // 关闭Socket
		} catch (Exception e) {
			System.out.println("异常错误,"+ e);
		}
	}
    public static void handleData(int typeId,InformationObject[] infoObjs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    for (int i = 0; i < infoObjs.length; i++) {
                        if(typeId == 1){   //单点遥信处理
                            int firstAddress = infoObjs[i].getInformationObjectAddress();  // 信息地址起始位
                            int len = infoObjs[i].getInformationElements().length;   // 所有信息值长度
                            for (int j = 0; j < len; j++) {
                                int address = firstAddress + j;
                                if(address >500 || address < 0){
                                	System.out.println("遥信信息超出定义范围：" + address);
                                }
                                System.out.println("信息地址："+address+"信息值："+ ((IeSinglePointWithQuality) infoObjs[i].getInformationElements()[j][0]).isOn());
                            }
                        }
                        else if(typeId == 13) {
                        	int firstAddress = infoObjs[i].getInformationObjectAddress();  // 信息地址起始位
                        	int len = infoObjs[i].getInformationElements().length;
                        	for (int j=0; j<len; j++) {
                        		int address = firstAddress + j;
                                if (address < 16385 || address > 17000){
                                    System.out.println("遥测信息超出定义范围：" + address);
                                }
                                System.out.println("信息地址："+address+"信息值："+ ((IeShortFloat) infoObjs[i].getInformationElements()[j][0]).getValue());
                        	}
                        }else {
                        	System.out.println("类型标识:" + typeId + "未处理");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
