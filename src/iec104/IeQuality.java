package iec104;

import java.io.DataInputStream;
import java.io.IOException;

/* 品质描述 */
public class IeQuality extends IeAbstractQuality {

    IeQuality(DataInputStream is) throws IOException {
        super(is);
    }

    public int isOverflow() {
        return value >> 0 & 0x01;
    }

    @Override
    public String toString() {
        return "品质描述, 溢出: " + isOverflow() + ", " + super.toString();
    }
}
