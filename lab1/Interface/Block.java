package lab1.Interface;

public interface Block {
    int getIndex();
    BlockManager getBlockManager();
    byte[] read();
    int getSize();
}
