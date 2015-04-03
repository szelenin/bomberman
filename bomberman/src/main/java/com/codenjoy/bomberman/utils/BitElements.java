package com.codenjoy.bomberman.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    public void setBit(int x, int y) {
        setBit(y * size + x);
    }

    public void setBit(int bitNo) {
        if (bitNo > size - 1) {
            throw new IllegalArgumentException("Bit out of range " + bitNo);
        }
        bytes[position(bitNo)] |= 0x80 >> bitNo % 8;
    }

    private int position(int bitNo) {
        return bitNo / 8;
    }

    public boolean getBit(int bitNo) {
        return (bytes[position(bitNo)] & 0x80 >> bitNo % 8) > 0;
    }

    public boolean getBit(int x, int y) {
        return getBit(y * size + x);
    }

    public int getSize() {
        return size;
    }

    public BitElements getCopy() {
        BitElements bitElements = new BitElements(size);
        System.arraycopy(this.bytes, 0, bitElements.bytes, 0, bitElements.bytes.length);
        return bitElements;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BitElements)) {
            return false;
        }
        BitElements other = (BitElements) obj;
        return new EqualsBuilder().append(bytes, other.bytes).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bytes).toHashCode();
    }
}
