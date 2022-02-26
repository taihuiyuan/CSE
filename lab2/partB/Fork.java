package lab2.partB;

import lab2.partA.MyLock;

class Fork {
    private int id;
    private boolean using;
    private MyLock lock;

    Fork(int id){
        this.id = id;
        using = false;
        lock = new MyLock();
    }

    void pick_up_fork(int id){
        lock.lock();
        while (using){
            Thread.yield();
        }
        using = true;
        System.out.println(Thread.currentThread().getName() + " Philosopher " + id + " " + System.nanoTime() + ": pick up fork "+ this.id);

    }

    void put_down_fork(int id){
        using = false;
        System.out.println(Thread.currentThread().getName() + " Philosopher " + id + " " + System.nanoTime() + ": put down fork "+this.id);
        lock.unLock();
    }
}
