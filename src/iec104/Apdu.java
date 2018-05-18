package iec104;

import java.io.DataInputStream;
import java.io.IOException;

/* APDU帧处理 */
public class Apdu {
	// 发送接收序列号
	private int sendSeqNum = 0;
	private int receiveSeqNum = 0;
	private ApciType apciType;
	private Asdu asdu = null;
	
	// 枚举，APCI类型，即I帧，S帧，U帧
	public enum ApciType {
		I_FORMAT, // I帧
		S_FORMAT, // S帧
		TESTFR_CON, // U帧，测试确认
		TESTFR_ACT, // U帧，测试命令
		STOPDT_CON, // U帧，停止确认
		STOPDT_ACT, // U帧，停止命令
		STARTDT_CON, // U帧，启动确认
		STARTDT_ACT, // U帧，启动命令
	}
	
	public Apdu() {}
	
	public Apdu(int sendSeqNum, int receiveSeqNum, ApciType apciType, Asdu asdu) {
        this.sendSeqNum = sendSeqNum;
        this.receiveSeqNum = receiveSeqNum;
        this.apciType = apciType;
        this.asdu = asdu;
	}
	public Apdu(DataInputStream dis) throws Exception {
        int start = dis.readByte() & 0xff;
        int len = dis.readByte() & 0xff;
        System.out.println("启动帧：" + Integer.toHexString(start));
        System.out.println("APDU长度：" + len);
        // 控制域存储
        byte[] controlFields = new byte[4];
        if(start != 104 ){
        	System.out.println(new IllegalArgumentException("启动帧错误"));
        }else if(len < 4 || len >253){
        	System.out.println(new IllegalArgumentException("帧长度有误"));
        }else{
            //读4字节控制域
            dis.readFully(controlFields);
            if((controlFields[0] & 0x01)==0){
                //I帧
                this.apciType = ApciType.I_FORMAT;
                //发送序列号
                sendSeqNum = ((controlFields[0] & 0xfe) >> 1) + ((controlFields[1] & 0xff) << 7);
                //接收序列号
                receiveSeqNum = ((controlFields[2] & 0xfe) >> 1) + ((controlFields[3] & 0xff) << 7);
                System.out.println("I帧，发送序列号："+sendSeqNum+"，接收序列号："+receiveSeqNum);
            }else if ((controlFields[0] & 0x03)==1){
                //S帧
                this.apciType = ApciType.S_FORMAT;
                receiveSeqNum = ((controlFields[2] & 0xfe) >> 1) + ((controlFields[3] & 0xff) << 7);
                System.out.println("S帧，接收序列号："+receiveSeqNum);
            }else if ((controlFields[0] & 0x03) == 3){
                //U帧
                if (controlFields[0] == 0x07){
                    this.apciType = ApciType.STARTDT_ACT;
                    System.out.println("U帧，启动命令");
                }else if (controlFields[0] == 0x0B){
                    this.apciType = ApciType.STARTDT_CON;
                    System.out.println("U帧启动确认");
                }else if (controlFields[0] == 0x13){
                    this.apciType = ApciType.STOPDT_ACT;
                    System.out.println("U帧停止命令");
                }else if (controlFields[0] == 0x23){
                    this.apciType = ApciType.STOPDT_CON;
                    System.out.println("U帧停止确认");
                }else if (controlFields[0] == 0x43){
                    this.apciType = ApciType.TESTFR_ACT;
                    System.out.println("U帧测试命令");
                }else if (controlFields[0] == (byte) 0x83){
                    this.apciType = ApciType.TESTFR_CON;
                    System.out.println("U帧测试确认");
                }
            }
        }
        //构建信息体
        if (len > 6) {
            this.asdu = new Asdu(dis);
        }
    }

    public int encode(byte[] buffer) throws IOException {

        buffer[0] = 0x68;

        int length = 4;

        if (apciType == ApciType.I_FORMAT) {
            buffer[2] = (byte) (sendSeqNum << 1);
            buffer[3] = (byte) (sendSeqNum >> 7);
            buffer[4] = (byte) (receiveSeqNum << 1);
            buffer[5] = (byte) (receiveSeqNum >> 7);
            length += asdu.encode(buffer, 6);
        }
        else if (apciType == ApciType.STARTDT_ACT) {
            buffer[2] = 0x07;
            buffer[3] = 0x00;
            buffer[4] = 0x00;
            buffer[5] = 0x00;
        }
        else if (apciType == ApciType.STARTDT_CON) {
            buffer[2] = 0x0b;
            buffer[3] = 0x00;
            buffer[4] = 0x00;
            buffer[5] = 0x00;
        }
        else if (apciType == ApciType.S_FORMAT) {
            buffer[2] = 0x01;
            buffer[3] = 0x00;
            buffer[4] = (byte) (receiveSeqNum << 1);
            buffer[5] = (byte) (receiveSeqNum >> 7);
        }

        buffer[1] = (byte) length;

        return length + 2;
    }

    public ApciType getApciType(){
        return apciType;
    }

    public int getSendSeqNumber() {
        return sendSeqNum;
    }

    public int getReceiveSeqNumber() {
        return receiveSeqNum;
    }

    public Asdu getAsdu() {
        return asdu;
    }
}
