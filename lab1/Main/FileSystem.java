package lab1.Main;

import lab1.Exception.ErrorCode;
import lab1.Manager.Block.Block;
import lab1.Manager.Block.BlockManager;
import lab1.Manager.File.Buffer;
import lab1.Manager.File.FileManager;
import lab1.Util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileSystem {
    private static int fmNumber=3;
    private static int bmNumber=3;

    public static FileManager[] fileManagers = new FileManager[fmNumber];
    public static BlockManager[] blockManagers = new BlockManager[bmNumber];

    FileSystem() {
        for (int i=0;i < fmNumber;i++){
            fileManagers[i] = new FileManager(i);
        }
        for (int i=0;i < bmNumber;i++){
            blockManagers[i] = new BlockManager(i);
        }

        // 复原文件系统
        recoverMeta();
    }

    private void recoverMeta(){
        //复原file meta
        for (int i=0;i < fmNumber;i++){
            File[] files = new File("../CSElab/src/lab1/Data/FileManager/fm-" + i).listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    HashMap<String, Object> map = IOUtil.readMeta("../CSElab/src/lab1/Data/FileManager/fm-" + i + "/" + fileName);
                    if (map != null){
                        FileManager fileManager = fileManagers[(int) map.get("fileManager")];
                        lab1.Manager.File.File f = new lab1.Manager.File.File((String) map.get("fileName"), fileManager);
                        f.setFileId((int) map.get("fileId"));
                        f.setSize((int) map.get("size"));
                        f.setBuffer(new Buffer(f.getSize()));
                        f.setLogicBlocks((ArrayList)(map.get("blocks")));
                        f.readData(f.getBuffer(), f.getLogicBlocks());
                        fileManagers[i].addFiles(f);
                    }
                }
            }
        }
        //复原block meta
        for (int i=0;i < bmNumber;i++){
            File[] files = new File("../CSElab/src/lab1/Data/BlockManager/bm-" + i + "/meta/").listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.substring(fileName.lastIndexOf(".") + 1).equals("meta")) {
                        HashMap<String, Object> map = IOUtil.readMeta("../CSElab/src/lab1/Data/BlockManager/bm-" + i + "/meta/" + fileName);
                        if (map != null){
                            BlockManager blockManager = blockManagers[(int) map.get("blockManager")];
                            Block b = new Block((int) map.get("blockId"), blockManager);
                            b.setSize((int) map.get("size"));
                            b.setCheckSum((String) map.get("checksum"));
                            blockManagers[i].addBlocks(b);
                        }
                    }
                }
            }
        }
    }

    public BlockManager[] getBlockManagers() {
        return blockManagers;
    }

    public FileManager[] getFileManagers() {
        return fileManagers;
    }

}
