package lab1.Manager.File;

import lab1.Exception.ErrorCode;
import lab1.Main.FileSystem;
import lab1.Manager.Block.LogicBlock;
import lab1.Manager.Block.Block;
import lab1.Util.IOUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class File implements lab1.Interface.File, Serializable {
    public static final int MOVE_CURR = 0;
    public static final int MOVE_HEAD = 1;
    public static final int MOVE_TAIL = 2;

    private FileManager fileManager;
    private String fileName;
    private ArrayList<LogicBlock> logicBlocks = new ArrayList<>();
    private Buffer buffer;
    private int cursor = 0; // 光标位置
    private int size = 0; // 文件大小
    private int fileId;
    private boolean flag = false;

    public File(int fileId, FileManager fileManager){
        this.fileManager = fileManager;
        this.fileId = FileManager.fileNumber;
        this.fileName = "f" + this.fileId;
        this.buffer = new Buffer();
        this.size = 0;
        this.cursor = 0;
    }

    public File(String fileName, FileManager fileManager){
        this.fileManager = fileManager;
        this.fileId = FileManager.fileNumber;
        this.fileName = fileName;
        this.buffer = new Buffer();
        this.size = 0;
        this.cursor = 0;
    }

    //buffer 读取logicblocks里的数据
    public void readData(Buffer buffer, ArrayList<LogicBlock> logicBlocks){
        int number = 0;
        for (LogicBlock logicBlock: logicBlocks){
            byte[] data = logicBlock.read();
            if (data != null){
                System.arraycopy(data, 0, buffer.getBuffer(), number* Block.getMaxSize(), data.length);
            }
            number++;
        }
    }

    //读取buffer里相应长度的内容
    @Override
    public byte[] read(int length) {
        if (cursor + length > size){
            System.out.println(ErrorCode.getErrorText(ErrorCode.EOF));
            System.out.println("please move the cursor");
            return null;
        }

        //若buffer还未加载，则读取block的内容到buffer
        if (buffer == null){
            buffer = new Buffer(size);
            readData(buffer, logicBlocks);
        }

        byte[] data = new byte[length];
        System.arraycopy(buffer.getBuffer(), cursor, data, 0, length);
        move(length, MOVE_CURR);

        return data;
    }

    public byte[] readAll() {
        int temp = cursor;
        move(0, MOVE_HEAD);
        byte[] data = read(size);
        cursor = temp;
        return data;
    }

    @Override
    public void write(byte[] b) {
        Buffer newBuffer = new Buffer(buffer.getSize() + b.length);

        newBuffer.copy(buffer.getBuffer(), 0, 0, cursor);
        newBuffer.copy(b, 0, cursor, b.length);
        newBuffer.copy(buffer.getBuffer(), cursor, (cursor+b.length), (buffer.getSize()-cursor));

        buffer = newBuffer;
        size = size + b.length;
        flag = true;
        move(b.length, MOVE_CURR);
    }

    @Override
    public int pos() {
        return cursor;
    }

    @Override
    public int move(int offset, int where) {
        if (where != 0 && where != 1 && where != 2){
            System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
            return -1;
        }
        int newCursor = cursor;
        switch (where){
            case MOVE_CURR:
                newCursor += offset;
                break;
            case MOVE_HEAD:
                newCursor = offset;
                break;
            case MOVE_TAIL:
                newCursor = size  - offset;
                break;
        }
        if (newCursor >= 0 && newCursor <= size) {
            cursor = newCursor;
            return cursor;
        }
        System.out.println(ErrorCode.getErrorText(ErrorCode.INVALID_CURSOR));
        return -1;
    }

    @Override
    public void close() {
        if (!flag){ // 没存buffer
            return;
        }else {
            try {
                logicBlocks = new ArrayList<>();
                int size = LogicBlock.getSize();
                int number = buffer.getSize() / size;
                int left = buffer.getSize() % size;
                //整块存block
                for (int i = 0; i < number; i++) {
                    LogicBlock logicBlock = new LogicBlock();
                    byte[] data = new byte[size];
                    System.arraycopy(buffer.getBuffer(), size * i, data, 0, size);
                    logicBlock.write(FileSystem.blockManagers, data);
                    logicBlocks.add(logicBlock);
                }
                //剩下的单独存一个block
                if (left != 0) {
                    LogicBlock logicBlock = new LogicBlock();
                    byte[] data = new byte[left];
                    System.arraycopy(buffer.getBuffer(), size * number, data, 0, left);
                    logicBlock.write(FileSystem.blockManagers, data);
                    logicBlocks.add(logicBlock);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                buffer = null;
                return;
            }
            //若写入失败则直接返回，不会写入meta
            writeMeta();
        }

        buffer = null;
    }

    public void writeMeta(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("fileName", getFileName());
        map.put("fileId", getFileId());
        map.put("fileManager", getFileManager().getId());
        map.put("blocks", getLogicBlocks());
        map.put("size", getSize());

        String path = "../CSElab/src/lab1/Data/FileManager/fm-" + getFileManager().getId() + "/" + getFileName() + ".meta";
        IOUtil.writeMeta(map, path);
    }

    public void showDetails(){
        System.out.println("fileName: " + fileName);
        System.out.println("fileId: " + fileId);
        System.out.println("its fileManager index: " + fileManager.getId());
        System.out.println("size: " + size);
        if (logicBlocks.size() != 0) {
            System.out.print("blocks: ");
            for (LogicBlock logicBlock : logicBlocks) {
                ArrayList<Block> blocks = logicBlock.getBlocks();
                for (Block block : blocks) {
                    System.out.print("[BM" + block.getBlockManager().getId() + ".b" + block.getIndex() + "]");
                }
                System.out.print(";");
            }
            System.out.println();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }


    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int newSize) {
        if (size == 0){
            buffer = new Buffer(newSize);
            size = newSize;
            flag = true;
            return;
        }

        move(0, MOVE_HEAD);

        if (buffer.getBuffer() == null && read(newSize) != null){
            buffer.setBuffer(read(newSize));
        }
        buffer = buffer.changeSize(newSize);

        size = newSize;
        flag = true;
    }

    public ArrayList<LogicBlock> getLogicBlocks() {
        return logicBlocks;
    }

    public void setLogicBlocks(ArrayList<LogicBlock> logicBlocks) {
        this.logicBlocks = logicBlocks;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Override
    public FileManager getFileManager() {
        return fileManager;
    }
}
