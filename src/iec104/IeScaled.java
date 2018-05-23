package iec104;

import java.io.DataInputStream;
import java.io.IOException;

public class IeScaled extends InformationElement {
	private final Integer value;

    public IeScaled(Integer value) {
        this.value = value;
    }

    public IeScaled(DataInputStream is) throws IOException {
        value = ((is.readByte() & 0xff) | ((is.readByte() & 0xff) << 8));
    }

    @Override
    public int encode(byte[] buffer, int i) {

        int tempVal = Float.floatToIntBits(value);
        buffer[i++] = (byte) tempVal;
        buffer[i++] = (byte) (tempVal >> 8);
        return 2;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "标度化值: " + value;
    }
}
