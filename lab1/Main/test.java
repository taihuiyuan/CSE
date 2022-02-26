package lab1.Main;

import lab1.Manager.File.File;

import java.util.Arrays;

public class test {
    public static void main(String[] args){
        /*
         * initialize your file system here
         * for example, initialize FileManagers and BlockManagers
         * and offer all the required interfaces
         * */
        FileSystem system = new FileSystem();
        // 检查基础功能
        File file = system.getFileManagers()[0].newFile("f0"); // id为1的⼀个file
        file.write("FileSystem".getBytes());
        System.out.println(new String(file.readAll()));
        file.move(0,File.MOVE_HEAD);
        file.write("Smart".getBytes());
        System.out.println(new String(file.readAll()));
        file.setSize(100);
        System.out.println(Arrays.toString(file.readAll()));
        file.move(0, File.MOVE_HEAD);
        file.setSize(10);
        System.out.println(new String(file.readAll()));
        file.close();
        SmartTool.smart_ls();


        //here we will destroy a block, and you should handler this exception
        // 检查持久性
        File file1 = system.getFileManagers()[0].getFile(0);
        System.out.println(new String(file1.read(file1.getSize())));
        SmartTool.smart_ls();

        //检查smart_copy
        File file2 = SmartTool.smart_copy(0,"f0",1,"f1_copy");
        System.out.println(Arrays.toString(file2.read(file2.getSize())));
        file2.close();
        SmartTool.smart_ls();


        //检查smart_hex\smart_cat\smart_write
        SmartTool.smart_hex(0, 5);
        SmartTool.smart_cat(0, "f0");
        SmartTool.smart_write(0, "f0", 2);
        System.out.println(new String(file1.readAll()));
     }
}
