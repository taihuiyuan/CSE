package lab3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public static void main(String[] args){
        //测试单个事务
        testSingle();
        //测试多个事务
        testMultiThread();
    }

    public static void testSingle(){
        MyAtomicity atomicity = new MyAtomicity();
        atomicity.update('2');
    }

    private final static ReentrantLock lock = new ReentrantLock();

    public static void testMultiThread(){
        int threadNumber = 10;

        for (int i = 0;i<threadNumber;i++){
            int finalI = i;
            new Thread(()->{
                lock.lock();
                MyAtomicity atomicity = new MyAtomicity();
                atomicity.update(Integer.toString(finalI).charAt(0));
                lock.unlock();
            }).start();
        }
    }
}
