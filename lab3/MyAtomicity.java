package lab3;

import java.io.*;

/**
 * You can alter these APIs if you wish.
 * If you do so, please specify in your docs what you changed and why. */
public class MyAtomicity {
    private volatile long currentId = Thread.currentThread().getId();

    /**
     * Update the text file "database"
     * @param ch The input value for all lines
     */
    public synchronized void update(char ch) {
        System.out.println("当前线程id为"+currentId);
        recover();

        //读取原文件内容
        String original_char = readOriginalFile();

        log("start update "+ch);
        //System.out.println("start update "+ch);
        copy("src/lab3/data.txt","src/lab3/temp.txt");

        for (int i=1;i<=10;i++){
            log("line "+i);
            write(i, ch);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //将temp.txt的内容替代data.txt,若原文件内容不符，则不commit
        String char2 = readOriginalFile();
        if (char2 == null || !char2.equals(original_char)){
            System.out.println("数据冲突，放弃commit");
        }else {
            copy("src/lab3/temp.txt", "src/lab3/data.txt");
            log("commit");
            //System.out.println("commit 成功");
        }
    }

    /**
     * Write a single character to "database"
     * @param index The location of database to be updated
     * @param ch The input value
     */
    private void write(int index, char ch) {
        try {
            File temp_file = new File("src/lab3/temp.txt");
            BufferedReader br = new BufferedReader(new FileReader(temp_file));
            StringBuilder stringBuilder = new StringBuilder();

            String temp = "";
            int num = 0;
            while ( (temp = br.readLine()) != null) {
                num++;
                if (num == index){
                    stringBuilder.append(ch);
                }else {
                    stringBuilder.append(temp);
                }
                stringBuilder.append("\n");
            }
            br.close();

            FileWriter fileWriter = new FileWriter(temp_file);
            fileWriter.write(String.valueOf(stringBuilder));
            fileWriter.flush();

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Log some information
     * @param text Log text
     */
    private synchronized void log(String text) {
        File file = new File("src/lab3/log.txt");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("fail to create log file");
            }
        }

        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file,true));
            writer.write(text);
            writer.write("\n");
            writer.flush();
            System.out.println("写入"+text+"成功");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recover the system from your log.
     * This should be called at the start of each {@code update()} call.
     */
    private synchronized void recover() {
        try {
            File file =new File("src/lab3/log.txt");
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp = "";
            String start_text = "";
            String final_text = "";
            int num = 0;
            while ( (temp = br.readLine()) != null) {
                num++;
                if (num == 1){
                    start_text = temp;
                }else {
                    final_text = temp;
                }
            }
            if (final_text.equals("commit")){
                System.out.println("上次修改成功");
            }else{
                String[] a = start_text.split(" ");
                System.out.println("上次将数据修改为"+a[a.length-1]+"失败，恢复初始数据");
            }

            //恢复数据后清空log内容
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            System.out.println("清空log成功");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * dest.txt复制src.txt的内容
     */
    private synchronized void copy(String srcPath, String destPath){
        try {
            File src_file = new File(srcPath);
            File dest_file = new File(destPath);
            if (!src_file.exists()){
                src_file.createNewFile();
            }
            if (!dest_file.exists()){
                dest_file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(src_file));
            FileWriter fileWriter = new FileWriter(dest_file);
            StringBuilder stringBuilder = new StringBuilder();

            String temp = "";
            while ( (temp = br.readLine()) != null) {
                stringBuilder.append(temp);
                stringBuilder.append("\n");
            }
            fileWriter.write(String.valueOf(stringBuilder));
            fileWriter.flush();
            br.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取原data文件
     * @return 文件内容
     */
    private synchronized String readOriginalFile(){
        String original_char = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/lab3/data.txt")));
            StringBuilder stringBuilder = new StringBuilder();

            String temp = "";
            while ( (temp = br.readLine()) != null) {
                stringBuilder.append(temp);
            }

            original_char = String.valueOf(stringBuilder);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return original_char;
    }
}
