package iec104;

import java.io.DataInputStream;
import java.io.IOException;

/* 品质描述 */
abstract class IeAbstractQuality extends InformationElement {
    protected int value;  
    public IeAbstractQuality(DataInputStream is) throws IOException {
        value = (is.readByte() & 0xff);
    }
    @Override
    public int encode(byte[] buffer, int i) {
        buffer[i] = (byte) value;
        return 1;
    }

    public int isBlocked() {
        return (value >> 4) & 0x01;
    }

    public int isSubstituted() {
        return (value >> 5) & 0x01;
    }

    public int isNotTopical() {
        return (value >> 6) & 0x01;
    }

    public int isInvalid() {
        return (value >> 7) & 0x01;
    }

    @Override
    public String toString() {
        return "被封锁: " + isBlocked() + ", 被取代: " + isSubstituted() + ", 非当前值: " + isNotTopical() + ", 是否有效: " + isInvalid();
    }
}
