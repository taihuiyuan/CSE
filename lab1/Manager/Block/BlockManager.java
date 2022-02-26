package lab1.Manager.Block;

import lab1.Exception.ErrorCode;
import lab1.Manager.Block.Block;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class BlockManager implements lab1.Interface.BlockManager, Serializable {
    private int id = 0;
    public static int blockNumber = -1;
    private ArrayList<Block> blocks = new ArrayList<>();

    public BlockManager(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //根据blockid获取block
    @Override
    public Block getBlock(int index) {
        for (Block block : blocks) {
            if (block.getIndex() == index) {
                return block;
            }
        }
        System.out.println(ErrorCode.getErrorText(ErrorCode.BLOCK_NOT_FOUND));
        return null;
    }

    //创建block
    @Override
    public Block newBlock(byte[] b) throws Exception {
        Block block = null;
        blockNumber++;
        block = new Block(blockNumber, this);
        block.write(b);
        blocks.add(block);

        return block;
    }

    @Override
    public Block newEmptyBlock(int blockSize) {
        blockNumber++;
        Block block = null;
        block = new Block(blockNumber, this);
        block.setSize(blockSize);
        block.writeMeta();
        blocks.add(block);
        return block;
    }

    public void addBlocks(Block block){
        blocks.add(block);
        blockNumber++;
    }

    public void listBlocks(){
        System.out.println("BlockManager" + id + ": ");
        if (blocks.size() != 0) {
            for (Block block : blocks) {
                System.out.println("block id: " + block.getIndex() + ", size: " + block.getSize());
            }
        }
    }
}
