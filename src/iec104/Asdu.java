package iec104;

import java.io.DataInputStream;

import iec104.util.ChangeUtils;


/* ASDU数据单元解析 */
public class Asdu {
	// 功能类型
	private final int typeId;
	// 元素序列
	private final boolean isSequenceOfElements;
	private int vsq;
	private final int addressNum;
	// 传输原因
	private final int causeOfTransmission;
	private boolean test;
	private boolean negativeConfirm;
	private int originatorAddress;
	private final int commonAddress;
	private InformationObject[] informationObjects;
	private byte[] privateInformation;
	
	public Asdu(int typeId,boolean isSequenceOfElements, int causeOfTransmission, boolean test, boolean negativeConfirm, int originatorAddress, int commonAddress, InformationObject[] informationObjects) {
		this.typeId = typeId;
		this.isSequenceOfElements = isSequenceOfElements;
		this.causeOfTransmission = causeOfTransmission;
		this.test = test;
		this.negativeConfirm = negativeConfirm;
		this.commonAddress = commonAddress;
		this.originatorAddress = originatorAddress;
		this.informationObjects = informationObjects;
		this.privateInformation = null;
		
		if (isSequenceOfElements) {
			this.addressNum = informationObjects[0].getInformationElements().length;
		}else {
			this.addressNum = informationObjects.length;
		}	
	}
	
    public Asdu(DataInputStream dataInputStream) throws Exception {
        //获取类型表示配置文件
    	this.typeId = dataInputStream.readByte() & 0xff;
        System.out.println("类型标识："+ typeId);
        if (Init.typeIdProp.getProperty(String.valueOf(typeId))== null || "".equals(Init.typeIdProp.getProperty(String.valueOf(typeId)))){
        	System.out.println("无效的类型标识："+typeId);
        }else{
        	System.out.println("类型标识：" + Init.typeIdProp.getProperty(String.valueOf(typeId)));
        }

        int vsqNum = dataInputStream.readByte() & 0xff;
        String vsqFormat = String.format("%08d",Integer.parseInt(Integer.toBinaryString(vsqNum)));
        //可变结构限定词，转为二进制后获取第8位(信息体是否连续标志)
        vsq = Integer.parseInt(vsqFormat.substring(0,1));
        //可变结构限定词，获取第1-7位，代表信息数据数目
        addressNum = Integer.parseInt(vsqFormat.substring(1,8),2);
        if (vsq == 1) {
            isSequenceOfElements = true;
            System.out.println("信息体地址连续：" +isSequenceOfElements+"，信息数据条数：" + addressNum);
        } else {
            isSequenceOfElements = false;
            System.out.println("信息体地址连续：" +isSequenceOfElements+"，信息数据条数：" + addressNum);
        }
        int numberOfSequenceElements;
        int numberOfInformationObjects;
        //根据是否连续来确定信息对象数目、信息元素数目
        if (isSequenceOfElements) {
            numberOfSequenceElements = addressNum;   // 信息数据数目
            numberOfInformationObjects = 1; // 信息元素数目为1(地址)
        }else {
        	numberOfSequenceElements = 1;  // 信息数据数目为1
            numberOfInformationObjects = addressNum;
        }
        byte[] cot = new byte[2];
        dataInputStream.readFully(cot);
        //传送原因
        causeOfTransmission = Integer.parseInt(ChangeUtils.byteAppend(cot),10);
        System.out.println("传送原因：" + Init.causeProp.getProperty(String.valueOf(causeOfTransmission)));
        //公共地址
        byte[] commAddress = new byte[2];
        dataInputStream.readFully(commAddress);
        commonAddress = Integer.parseInt(ChangeUtils.byteAppend(commAddress));
        System.out.println("公共地址：" + commonAddress);
        //信息体
        if (typeId < 128) {
            informationObjects = new InformationObject[numberOfInformationObjects];
            for (int i = 0; i < numberOfInformationObjects; i++) {
                informationObjects[i] = new InformationObject(dataInputStream, typeId, numberOfSequenceElements);
            }
            privateInformation = null;
        }else{
            System.out.println(" ");
        }

    }

    public int getTypeId() {
        return typeId;
    }

    public boolean isSequenceOfElements() {
        return isSequenceOfElements;
    }

    public int getSequenceLength() {
        return addressNum;
    }

    public int getCauseOfTransmission() {
        return causeOfTransmission;
    }

    public boolean isTestFrame() {
        return test;
    }

    public boolean isNegativeConfirm() {
        return negativeConfirm;
    }

    public Integer getOriginatorAddress() {
        return originatorAddress;
    }

    public int getCommonAddress() {
        return commonAddress;
    }

    public InformationObject[] getInformationObjects() {
        return informationObjects;
    }

    public byte[] getPrivateInformation() {
        return privateInformation;
    }

    int encode(byte[] buffer, int i) {

        int origi = i;

        buffer[i++] = (byte) typeId;
        if (isSequenceOfElements) {
            buffer[i++] = (byte) (addressNum | 0x80);
        }else {
            buffer[i++] = (byte) addressNum;
        }

        if (test) {
            if (negativeConfirm) {
                buffer[i++] = (byte) (causeOfTransmission | 0xC0);
            }else {
                buffer[i++] = (byte) (causeOfTransmission | 0x80);
            }
        }else {
            if (negativeConfirm) {
                buffer[i++] = (byte) (causeOfTransmission | 0x40);
            }else {
                buffer[i++] = (byte) causeOfTransmission;
            }
        }

        buffer[i++] = (byte) originatorAddress;

        buffer[i++] = (byte) commonAddress;

        buffer[i++] = (byte) (commonAddress >> 8);

        if (informationObjects != null) {
            for (InformationObject informationObject : informationObjects) {
                i += informationObject.encode(buffer, i);
            }
        }else {
            System.arraycopy(privateInformation, 0, buffer, i, privateInformation.length);
            i += privateInformation.length;
        }
        return i - origi;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        if (informationObjects != null) {
            for (InformationObject informationObject : informationObjects) {
                builder.append(informationObject.toString());
                builder.append("\n");
            }
        }else {
            builder.append("\nPrivate Information:\n");
            int l = 1;
            for (byte b : privateInformation) {
                if ((l != 1) && ((l - 1) % 8 == 0)) {
                    builder.append(' ');
                }
                if ((l != 1) && ((l - 1) % 16 == 0)) {
                    builder.append('\n');
                }
                l++;
                builder.append("0x");
                String hexString = Integer.toHexString(b & 0xff);
                if (hexString.length() == 1) {
                    builder.append(0);
                }
                builder.append(hexString + " ");
            }
        }

        return builder.toString();

    }
}
