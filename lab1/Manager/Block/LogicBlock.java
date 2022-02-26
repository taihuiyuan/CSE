package lab1.Manager.Block;

import lab1.Exception.ErrorCode;
import lab1.Manager.Block.Block;
import lab1.Manager.Block.BlockManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LogicBlock implements Serializable {
    private static final long serialVersionUID = 42L;
    private static final int size = 4;
    private ArrayList<Block> blocks = new ArrayList<>();
    private int number = 2;

    public LogicBlock(){}

    public LogicBlock(BlockManager[] blockManagers, byte[] data){
        try {
            for (int i = 0; i < number; i++){
                int index = new Random().nextInt(blockManagers.length);
                blocks.add(blockManagers[index].newBlock(data));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static int getSize() {
        return size;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    //对每个block进行遍历，若有block没有损坏则返回对应数据
    public byte[] read(){
        for (Block block : blocks) {
            byte[] data = block.read();
            if (data != null) {
                return data;
            }
        }
        System.out.println(ErrorCode.getErrorText(ErrorCode.BLOCK_BROKEN));
        return null;
    }

    //随机选择number个blockManagers存入新块
    public void write(BlockManager[] blockManagers, byte[] data) throws Exception {
        List myList = new ArrayList();

        while(myList.size() < number) {
            int num = new Random().nextInt(blockManagers.length);
            if(!myList.contains(num)) {
                myList.add(num);
            }
        }

        for (int i = 0; i < number; i++){
            blocks.add(blockManagers[(int)myList.get(i)].newBlock(data));
        }
    }
}
