package iec104;

import java.io.DataInputStream;
import java.io.IOException;

/* 类型标识，测量值，短浮点数  遥测 */
public class IeShortFloat extends InformationElement {

    private final float value;

    public IeShortFloat(float value) {
        this.value = value;
    }

    public IeShortFloat(DataInputStream is) throws IOException {
        value = Float.intBitsToFloat((is.readByte() & 0xff) | ((is.readByte() & 0xff) << 8) | ((is.readByte() & 0xff) << 16) | ((is.readByte() & 0xff) << 24));
    }

    @Override
    public int encode(byte[] buffer, int i) {

        int tempVal = Float.floatToIntBits(value);
        buffer[i++] = (byte) tempVal;
        buffer[i++] = (byte) (tempVal >> 8);
        buffer[i++] = (byte) (tempVal >> 16);
        buffer[i] = (byte) (tempVal >> 24);

        return 4;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "短浮点数值: " + value;
    }
}

