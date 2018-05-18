package iec104;

import java.io.DataInputStream;
import java.io.IOException;

public class IeQualifierOfInterrogation extends InformationElement {

    private final int value;

    public IeQualifierOfInterrogation(int value) {
        this.value = value;
    }

    public IeQualifierOfInterrogation(DataInputStream is) throws IOException {
        value = (is.readByte() & 0xff);
    }

    @Override
    public int encode(byte[] buffer, int i) {
        buffer[i] = (byte) value;
        return 1;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Qualifier of interrogation: " + value;
    }
}
