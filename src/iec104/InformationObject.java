package iec104;

import java.io.DataInputStream;
import java.io.IOException;

/* 信息体对象 */
public class InformationObject {
	private int informationObjectAddress;
	private final InformationElement[][] informationElements;
	
	public InformationObject(int informationObjectAddress, InformationElement[][] informationElements) {
		this.informationObjectAddress = informationObjectAddress;
		this.informationElements = informationElements;
	}

	public InformationObject(DataInputStream is, int typeId, int numberOfSequenceElements) throws IOException {
		this.informationObjectAddress = (is.readByte() & 0xff) + ((is.readByte() & 0xff) <<8) + ((is.readByte() & 0xff) << 16);
		switch (typeId) {
        	// 1 单点遥信
        	case 1:
        		informationElements = new InformationElement[numberOfSequenceElements][1];
        		for (int i = 0; i < numberOfSequenceElements; i++) {
        			informationElements[i][0] = new IeSinglePointWithQuality(is);
        		}
        		break;
        	// 13 浮点型遥测
        	case 13:
        		informationElements = new InformationElement[numberOfSequenceElements][2];
        		for (int i = 0; i < numberOfSequenceElements; i++) {
        			informationElements[i][0] = new IeShortFloat(is);
        			informationElements[i][1] = new IeQuality(is);
        		}
        		break;
        	// 45 单点遥控返回确认数据读取
        	case 45:
        		informationElements = new InformationElement[numberOfSequenceElements][1];
        		for (int i = 0; i < numberOfSequenceElements; i++) {
        			informationElements[i][0] = new IeSinglePointWithQuality(is);
        		}
        		break;
        	// 49标度化值返回确认数据获取
        	case 49:
        		informationElements = new InformationElement[numberOfSequenceElements][2];
        		for (int i = 0; i < numberOfSequenceElements; i++) {
        			informationElements[i][0] = new IeScaled(is);
        			informationElements[i][1] = new IeQuality(is);
        		}
        		break;
        	// 100 总召
        	case 100:
        		informationElements = new InformationElement[][] { { new IeQualifierOfInterrogation(is) } };
        		break;

        	default:
        		throw new IOException("无法转换信息对象，由于类型标识未知: " + typeId);
		}
	}
	
    public int encode(byte[] buffer, int i) {
        int origi = i;
        buffer[i++] = (byte) informationObjectAddress;
        buffer[i++] = (byte) (informationObjectAddress >> 8);
        buffer[i++] = (byte) (informationObjectAddress >> 16);
        for (InformationElement[] informationElementCombination : informationElements) {
            for (InformationElement informationElement : informationElementCombination) {
                i += informationElement.encode(buffer, i);
            }
        }

        return i - origi;
    }

    public int getInformationObjectAddress() {
        return informationObjectAddress;
    }
    
    public InformationElement[][] getInformationElements() {
        return informationElements;
    }

    @Override
	public String toString() {
        StringBuilder builder = new StringBuilder("IOA: " + informationObjectAddress);
        if (informationElements.length > 1) {
            int i = 1;
            for (InformationElement[] informationElementSet : informationElements) {
                builder.append("\n信息体元素集 " + i + ":");
                for (InformationElement informationElement : informationElementSet) {
                    builder.append("\n");
                    builder.append(informationElement.toString());
                }
                i++;
            }
        }
        else {
            for (InformationElement[] informationElementSet : informationElements) {
                for (InformationElement informationElement : informationElementSet) {
                    builder.append("\n");
                    builder.append(informationElement.toString());
                }
            }
        }
        return builder.toString();
    }
}
