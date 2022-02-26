package lab1.Interface;

public interface File {
    int MOVE_CURR = 0; //只是光标的三个枚举值，具体数值⽆实际意义
    int MOVE_HEAD = 1;
    int MOVE_TAIL = 2;
    int getFileId();
    FileManager getFileManager();
    byte[] read(int length);
    void write(byte[] b);
    default int pos() {
        return move(0, MOVE_CURR);
    }
    int move(int offset, int where);//把⽂件光标移到距离where offset个byte的位置，并返回⽂件光标所在位置
    int getSize();
    void setSize(int newSize);
    //使⽤buffer的同学需要实现
    void close();
}
