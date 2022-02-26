package lab2.partA;

import java.util.ArrayList;

public class MyLock implements MyLockInterface {
    private volatile int state = 0;//记录锁的状态
    private volatile ArrayList<Thread> queue = new ArrayList<>();
    private int turn = 1;//获得锁的顺序
    private int queueNum=0;
    private volatile long id = -1;

    @Override
    public void lock() {
        Thread current = Thread.currentThread();
        int num = addThread(current);//将当前线程添加进序列
        id = -1;//添加线程结束

        if (tryAcquire(1)!=-1 && num!=1){//尝试获取第一个线程的锁，若获取到了则其它线程循环等待锁
            if (acquireQueue(num)){
                System.out.println("the thread "+queue.get(num-1)+" is interrupted");
            }
        }
    }

    //添加线程到队列
    private int addThread(Thread thread){
        //id=-1时说明当前没有线程添加进队列，否则id=当前添加的线程id
        while (id != Thread.currentThread().getId()){
            while (id >= 0){
                Thread.yield();
            }
            id = Thread.currentThread().getId();
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
        queue.add(thread);
        queueNum++;
        return queueNum;
    }

    //尝试获取锁
    private int tryAcquire(int arg){
        if (arg != turn){
            return -1;
        }
        if (state == 0){//锁还没被获取到
            state = arg;
            return 1;
        }else if (state == arg){//锁就是被当前进程占用的，可以重入
            return 0;
        }
        return -1;
    }

    //等待锁
    private boolean acquireQueue(int arg){
        boolean interrupted = false;
        //若轮到当前线程获取锁并获取到了锁则跳出循环
        while (!(arg == turn && tryAcquire(arg)!=-1)){
            Thread.yield();
            if (Thread.currentThread().isInterrupted()){
                interrupted = true;
            }
        }
        return interrupted;
    }

    @Override
    public void unLock() {
        Thread current = Thread.currentThread();
        if (state != 0 && current == queue.get(state-1)){
            state = 0;
            turn++;
        }
    }
}
