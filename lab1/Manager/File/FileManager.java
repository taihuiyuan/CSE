package lab1.Manager.File;

import lab1.Exception.ErrorCode;
import lab1.Manager.Block.LogicBlock;
import lab1.Manager.Block.Block;

import java.io.Serializable;
import java.util.ArrayList;

public class FileManager implements lab1.Interface.FileManager, Serializable {
    private int id;
    public static int fileNumber = -1;
    private ArrayList<File> files = new ArrayList<>();

    public FileManager(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //根据文件id获取文件
    @Override
    public File getFile(int fileId) {
        for (File file : files) {
            int fileId2 = file.getFileId();
            if (fileId == fileId2)
                return file;
        }
        System.out.println(ErrorCode.getErrorText(ErrorCode.FILE_NOT_FOUND));
        return null;
    }

    //根据文件名获取文件
    public File getFile(String fileName) {
        for (File file : files) {
            String fileName2 = file.getFileName();
            if (fileName.equals(fileName2))
                return file;
        }
        System.out.println(ErrorCode.getErrorText(ErrorCode.FILE_NOT_FOUND));
        return null;
    }

    //根据文件id新建文件
    @Override
    public File newFile(int fileId) {
        for (File file : files) {
            int fileId2 = file.getFileId();
            if (fileId == fileId2){
                System.out.println(ErrorCode.getErrorText(ErrorCode.FILE_ALREADY_EXISTS));
                return null;
            }
        }

        FileManager.fileNumber++;
        File file = new File(fileId, this);
        files.add(file);
        file.writeMeta();

        return file;
    }

    //根据文件名新建文件
    public File newFile(String fileName) {
        for (File file : files) {
            String fileName2 = file.getFileName();
            if (fileName.equals(fileName2)){
                System.out.println(ErrorCode.getErrorText(ErrorCode.FILE_ALREADY_EXISTS));
                return null;
            }
        }

        FileManager.fileNumber++;
        File file = new File(fileName, this);
        file.writeMeta();
        files.add(file);

        return file;
    }

    public void addFiles(File file){
        files.add(file);
        fileNumber++;
    }

    public void listFiles(){
        System.out.println("FileManager" + id + ": ");
        if (files.size() != 0) {
            for (File file : files) {
                System.out.print("file name: " + file.getFileName() + ", ");
                System.out.print("file id: " + file.getFileId() + ", ");
                System.out.print("size: " + file.getSize());
                ArrayList<LogicBlock> logicBlocks = file.getLogicBlocks();
                if (logicBlocks.size() != 0) {
                    System.out.print(", blocks: ");
                    for (LogicBlock logicBlock : logicBlocks) {
                        ArrayList<Block> blocks = logicBlock.getBlocks();
                        for (Block block : blocks) {
                            System.out.print("[BM" + block.getBlockManager().getId() + ".b" + block.getIndex() + "]");
                        }
                        System.out.print(";");
                    }
                    System.out.println();
                }else {
                    System.out.println();
                }
            }
        }else {
            System.out.println("There is no file in this file manager.");
        }
    }
}
