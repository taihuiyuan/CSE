package lab2;

import lab2.partA.MyLockInterface;

public class Lock implements MyLockInterface {
    private volatile long id = -1;

    @Override
    public void lock() {
        Thread current = Thread.currentThread();
        long threadId = current.getId();
        while (id != Thread.currentThread().getId()){
            while (true){
                long time = System.nanoTime();
                while (id >= 0){
                    Thread.yield();
                    time = System.nanoTime();
                }
                id = Thread.currentThread().getId();
                if (System.nanoTime() - time < 100){
                    break;
                }else {
                    id = -1;
                }
            }

            yield(500000);
        }
    }

    private void yield(long timeout){
        long start = System.nanoTime();
        while (System.nanoTime() - start < timeout){
            Thread.yield();
        }
    }

    @Override
    public void unLock() {
        id = -1;
    }
}
