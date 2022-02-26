package lab1.Manager.Block;

import lab1.Exception.ErrorCode;
import lab1.Main.FileSystem;
import lab1.Util.IOUtil;
import lab1.Util.MD5Util;

import java.io.*;
import java.io.File;
import java.util.HashMap;

public class Block implements lab1.Interface.Block, Serializable {
    private static final int maxSize = 4;
    private int blockId;
    private BlockManager blockManager;
    private int size;
    private String checkSum;

    public Block(int blockId, BlockManager blockManager) {
        this.blockId = blockId;
        this.blockManager = blockManager;
        this.size = 0;
    }

    @Override
    public int getIndex() {
        return blockId;
    }

    @Override
    public BlockManager getBlockManager() {
        return blockManager;
    }

    //读取block的data数据
    @Override
    public byte[] read() {
        FileInputStream fis;
        BufferedInputStream bis;
        byte[] bytes;

        try {
            String path = "../CSElab/src/lab1/Data/BlockManager/bm-" + blockManager.getId() + "/data.txt/" + blockId + ".data.txt";
            fis = new FileInputStream(path);
            bis = new BufferedInputStream(fis);

            bytes = new byte[bis.available()];
            bis.read(bytes);

            bis.close();
            fis.close();

            //将bytes加密后的结果与block记录的check进行比对
            if (MD5Util.encode(bytes).equals(checkSum)){
                return bytes;
            }else {
                System.out.println(ErrorCode.getErrorText(ErrorCode.CHECKSUM_CHECK_FAILED));
            }
        } catch (IOException e) {
            System.out.println(ErrorCode.getErrorText(ErrorCode.IO_EXCEPTION));
        }

        return null;
    }

    public void write(byte[] data) throws Exception {
        //若写入的data大于block的size，则抛出异常
        if (data.length > maxSize){
            throw new Exception(ErrorCode.getErrorText(ErrorCode.SYSTEM_ERROR));
        }

        //将内容写入data文件
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            String path = "../CSElab/src/lab1/Data/BlockManager/bm-" + blockManager.getId() + "/data.txt/" + blockId + ".data.txt";
            java.io.File file = new java.io.File(path);
            if (!file.exists()){
                java.io.File parent = new File(file.getParent());
                if(!parent.exists()) {
                    if (!parent.mkdirs()){
                        System.out.println(ErrorCode.getErrorText(ErrorCode.SYSTEM_ERROR));
                    }
                }
                if (!file.createNewFile()){
                    System.out.println(ErrorCode.getErrorText(ErrorCode.SYSTEM_ERROR));
                }
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            bos.write(data);
            size = data.length;

            bos.close();
            fos.close();

            //进行加密得到checksum
            checkSum = MD5Util.encode(data);
        } catch (IOException ex) {
            throw new Exception(ErrorCode.getErrorText(ErrorCode.IO_EXCEPTION));
        }
        writeMeta();
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public static int getMaxSize(){
        return maxSize;
    }

    void writeMeta(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("blockId", getIndex());
        map.put("blockManager", getBlockManager().getId());
        map.put("size", getSize());
        map.put("checksum", checkSum);

        String path = "../CSElab/src/lab1/Data/BlockManager/bm-" + getBlockManager().getId() + "/meta/" + getIndex() + ".meta";
        IOUtil.writeMeta(map, path);
    }
}
