package lab1.Main;

import lab1.Manager.Block.Block;
import lab1.Manager.Block.BlockManager;
import lab1.Manager.File.File;
import lab1.Manager.File.FileManager;

import java.util.Arrays;
import java.util.Scanner;

class SmartTool {

    //read the bytes of the file
    static void smart_cat(int fmNumber, String fileName){
        FileManager fileManager = FileSystem.fileManagers[fmNumber];
        File file = fileManager.getFile(fileName);
        if (file != null){
            byte[] bytes = file.readAll();
            System.out.println(Arrays.toString(bytes));
        }
    }

    //change bytes of a block to hex
    static void smart_hex(int blockManagerIndex, int blockIndex){
        BlockManager blockManager = FileSystem.blockManagers[blockManagerIndex];
        Block block = blockManager.getBlock(blockIndex);
        if (block != null){
            byte[] bytes = block.read();
            System.out.println(byte2Hex(bytes));
        }else {
            System.out.println("Fail to change bytes to hex");
        }
    }

    //change bytes to hex
    private static String byte2Hex(byte[] bytes) {
        if (bytes == null){
            return null;
        }
        StringBuilder b = new StringBuilder();
        String temp = null;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                b.append("0");
            }
            b.append(temp);
        }
        return b.toString();
    }

    //write into the file in the position index
    static void smart_write(int fmNumber, String fileName, int index){
        FileManager fileManager = FileSystem.fileManagers[fmNumber];
        File file = fileManager.getFile(fileName);
        if (file == null){
            file = fileManager.newFile(fileName);
            System.out.println("New file has been created");
        }
        if (file.move(index, File.MOVE_HEAD) != -1) {
            System.out.println("Please enter the content:");
            Scanner scanner = new Scanner(System.in);
            byte[] bytes = scanner.nextLine().getBytes();
            file.write(bytes);
            System.out.println("write successfully!");
        }
    }

    //copy src file to dst file
    static File smart_copy(int srcFmIndex, String src, int dstFmIndex, String dst){
        FileManager srcFileManager = FileSystem.fileManagers[srcFmIndex];
        FileManager dstFileManager = FileSystem.fileManagers[dstFmIndex];
        File srcFile = srcFileManager.getFile(src);
        File dstFile = null;
        if (srcFile != null) {
            java.io.File file = new java.io.File("../CSElab/src/lab1/Data/FileManager/fm-" + dstFileManager.getId() + "/" + dst + ".meta");
            if (!file.exists()) {
                dstFile = dstFileManager.newFile(dst);
                System.out.println("dst file has been created");
            }else {
                dstFile = dstFileManager.getFile(dst);
            }
            byte[] bytes = srcFile.readAll();
            if (dstFile != null) {
                dstFile.setSize(0);
                dstFile.write(bytes);
                dstFile.move(0, File.MOVE_HEAD);
                System.out.println("copy successfully");
            }
        }

        return dstFile;
    }

    //show the structure of the system
    static void smart_ls(){
        for (BlockManager blockManager : FileSystem.blockManagers) {
            blockManager.listBlocks();
        }
        for (FileManager fileManager : FileSystem.fileManagers) {
            fileManager.listFiles();
        }
    }
}
