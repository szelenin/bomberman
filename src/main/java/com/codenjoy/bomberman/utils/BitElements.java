package com.codenjoy.bomberman.utils;

/**
 * Created by szelenin on 2/18/14.
 */
public class BitElements {
    private byte bytes[];
    private int size;

    public BitElements(int size) {
        this.size = size;
        bytes = new byte[size / 8 + 1];
    }

    public void setBit(int bitNo) {
        if (bitNo > size - 1) {
            throw new IllegalArgumentException("Bit out of range " + bitNo);
        }
        bytes[position(bitNo)] |= 0x81 >> bitNo % 8;
    }

    private int position(int bitNo) {
        return bitNo / 8;
    }

    public boolean getBit(int bitNo) {
        return (bytes[position(bitNo)] & 0x81 >> bitNo % 8) > 0;
    }
}
