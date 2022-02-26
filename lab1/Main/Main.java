package lab1.Main;

import lab1.Exception.ErrorCode;
import lab1.Manager.Block.BlockManager;
import lab1.Manager.File.File;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileSystem system = new FileSystem();
        init();
        Scanner scanner = new Scanner(System.in);
        String command;
        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.print(">>>>> ");
            command = scanner.nextLine();
            operation(command, system);
        }
    }

    private static void init(){
        System.out.println("Welcome to Your File System");
        System.out.println("Now you have " + FileSystem.fileManagers.length + " file managers and " + FileSystem.blockManagers.length + " block managers");
        System.out.println("Here are the operations:");
        System.out.println("'new [FM index] [file name]': create a new file");
        System.out.println("'read [FM index] [file name] [length](option)': read a file, if you don't enter the length, it will read the full file");
        System.out.println("'write [FM index] [file name]': write a file");
        System.out.println("'save [FM index] [file name]': save the changes");
        System.out.println("'cursor [FM index] [file name]': show the cursor position");
        System.out.println("'move [FM index] [file name] [offset] [where]': move the cursor, curr=0, head=1, tail=2");
        System.out.println("'setSize [FM index] [file name] [new size]': reset the file size");
        System.out.println("'detail [FM index] [file name]': show the details of a file");
        System.out.println("'smart_cat [FM index] [file name]': read file data.txt directly");
        System.out.println("'smart_hex [FM index] [block index]': read block data.txt with hex");
        System.out.println("'smart_write [FM index] [file name] [index]': move the cursor and write the data.txt");
        System.out.println("'smart_copy [src FM index] [src file name] [dst FM index] [dst file name]': copy the file");
        System.out.println("'smart_ls': show the file system's structure");
        System.out.println("'quit': quit the file system");
    }

    private static void operation(String command, FileSystem system){
        String[] paras;
        paras = command.split(" ");
        switch (paras[0]){
            case "new":
                if (paras.length == 3){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].newFile(paras[2]);
                        if (file != null){
                            file.showDetails();
                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "read":
                if (paras.length == 4){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].getFile(paras[2]);
                        if (file!=null) {
                            byte[] data = file.read(Integer.parseInt(paras[3]));
                            if (data != null) {
                                System.out.println(new String(data));
                            }
                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else if (paras.length == 3){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].getFile(paras[2]);
                        if (file != null){
                            System.out.println(new String(file.readAll()));
                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "write":
                if (paras.length == 3){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].getFile(paras[2]);
                        if (file!=null){
                            System.out.println("Please enter the content:");
                            Scanner scanner = new Scanner(System.in);
                            byte[] bytes = scanner.nextLine().getBytes();
                            if (bytes.length != 0){
                                file.write(bytes);
                                System.out.println("write successfully!");
                            }else {
                                System.out.println("please enter something");
                            }

                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "save":
                if (paras.length == 3){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].getFile(paras[2]);
                        if (file!=null){
                            file.close();
                            System.out.println("save successfully!");
                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "cursor":
                if (paras.length == 3){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].getFile(paras[2]);
                        if (file != null){
                            System.out.println("Now the file's cursor is in index " + file.pos());
                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "move":
                if (paras.length == 5){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].getFile(paras[2]);
                        if (file != null && file.move(Integer.parseInt(paras[3]), Integer.parseInt(paras[4])) != -1){
                            System.out.println("Move the cursor successfully!");
                            System.out.println("Now the file's cursor is in index " + file.pos());
                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "setSize":
                if (paras.length == 4){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].getFile(paras[2]);
                        if (file != null){
                            file.setSize(Integer.parseInt(paras[3]));
                            System.out.println("set size successfully!");
                            System.out.println("Now the file's size is " + file.getSize());
                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "detail":
                if (paras.length == 3){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        File file = system.getFileManagers()[fmIndex].getFile(paras[2]);
                        if (file != null){
                            file.showDetails();
                        }
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "smart_cat":
                if (paras.length == 3){
                    int fmIndex = Integer.parseInt(paras[1]);
                    if (fmIndex > -1 && fmIndex < system.getFileManagers().length) {
                        SmartTool.smart_cat(fmIndex, paras[2]);
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "smart_hex":
                if (paras.length == 3){
                    int bmIndex = Integer.parseInt(paras[1]);
                    int blockIndex = Integer.parseInt(paras[2]);
                    if (bmIndex > -1 && bmIndex < system.getBlockManagers().length && blockIndex > -1 && blockIndex < BlockManager.blockNumber+1) {
                        SmartTool.smart_hex(bmIndex, blockIndex);
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "smart_write":
                if (paras.length == 4){
                    int fmIndex = Integer.parseInt(paras[1]);
                    int index = Integer.parseInt(paras[3]);
                    if (fmIndex > -1 && fmIndex < system.getBlockManagers().length) {
                        SmartTool.smart_write(fmIndex, paras[2], index);
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "smart_copy":
                if (paras.length == 5){
                    int srcFmIndex = Integer.parseInt(paras[1]);
                    int dstFmIndex = Integer.parseInt(paras[3]);
                    if (srcFmIndex > -1 && srcFmIndex < system.getFileManagers().length && dstFmIndex > -1 && dstFmIndex < system.getFileManagers().length) {
                        SmartTool.smart_copy(srcFmIndex, paras[2], dstFmIndex, paras[4]);
                    }else {
                        System.out.println(ErrorCode.getErrorText(ErrorCode.INDEX_OUT_OF_BOUND));
                    }
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "smart_ls":
                if (paras.length == 1){
                    SmartTool.smart_ls();
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            case "quit":
                if (paras.length == 1){
                    System.out.println("Bye!");
                    System.exit(0);
                }else {
                    System.out.println(ErrorCode.getErrorText(ErrorCode.WRONG_PARAM));
                }
                break;
            default:
                System.out.println("The input is wrong, please read the operations carefully!");
        }
    }
}
