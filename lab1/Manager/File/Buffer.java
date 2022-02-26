package lab1.Manager.File;

import java.util.Arrays;

public class Buffer {
    private byte[] buffer;
    private int size = 0;

    public Buffer(){
        buffer = new byte[0];
    }

    public Buffer(int size){
        buffer = new byte[size];
        this.size = size;
    }

    public Buffer(byte[] buffer, int size){
        this.buffer = buffer;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public Buffer changeSize(int newSize){
        byte[] data = Arrays.copyOf(buffer, newSize);
        return new Buffer(data, newSize);
    }

    public void copy(byte[] data, int srcPos, int dstPos, int length){
        System.arraycopy(data, srcPos, buffer, dstPos, length);
    }
}
