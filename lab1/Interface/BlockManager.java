package lab1.Interface;

import java.io.IOException;

public interface BlockManager {
    Block getBlock(int index);
    Block newBlock(byte[] b) throws Exception;
    Block newEmptyBlock(int blockSize);
}
