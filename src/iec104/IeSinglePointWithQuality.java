package iec104;

import java.io.DataInputStream;
import java.io.IOException;

/* 单点信息 */
public class IeSinglePointWithQuality extends IeAbstractQuality {

    public IeSinglePointWithQuality(DataInputStream is) throws IOException {
        super(is);
    }

    public int isOn() {
        return (value >> 0) | 0x01;
    }

    @Override
    public String toString() {
        return "单点, 是否开闸: " + isOn() + ", " + super.toString();
    }
}
