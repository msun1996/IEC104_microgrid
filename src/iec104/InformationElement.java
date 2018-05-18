package iec104;

public abstract class InformationElement {
    public abstract int encode(byte[] buffer, int i);
}