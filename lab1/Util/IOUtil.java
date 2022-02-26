package lab1.Util;

import lab1.Exception.ErrorCode;

import java.io.*;
import java.util.HashMap;

public class IOUtil {
    public static void writeMeta(HashMap<String, Object> map, String path){
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;

        try {
            File file = new File(path);
            //若文件不存在则新建文件
            if (!file.exists()){
                File parent = new File(file.getParent());
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
            oos = new ObjectOutputStream(bos);

            oos.writeObject(map);

            oos.close();
            bos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println(ErrorCode.getErrorText(ErrorCode.IO_EXCEPTION));
        }
    }

    public static HashMap<String, Object> readMeta(String path){
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);

            HashMap<String, Object> map = (HashMap<String, Object>) ois.readObject();

            fis.close();
            ois.close();

            return map;
        } catch (FileNotFoundException e){
            System.out.println(ErrorCode.getErrorText(ErrorCode.FILE_NOT_FOUND));
        } catch (ClassNotFoundException classNotFoundException) {
            System.out.println(ErrorCode.getErrorText(ErrorCode.CLASS_NOT_FOUND));
        } catch (IOException e) {
            System.out.println(ErrorCode.getErrorText(ErrorCode.IO_EXCEPTION));
        }

        return null;
    }
}
