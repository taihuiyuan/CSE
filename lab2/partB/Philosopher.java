package lab2.partB;

public class Philosopher implements Runnable {
    private int id;
    private final Object leftFork;
    private final Object rightFork;

    Philosopher(int id, Object left, Object right) {
        this.id = id;
        this.leftFork = left;
        this.rightFork = right;
    }

    private void doAction(String action) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " " + action);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while(true){
                doAction("Philosopher " + id + " " + System.nanoTime() + ": Thinking"); // thinking
                ((Fork) leftFork).pick_up_fork(id);
                ((Fork) rightFork).pick_up_fork(id);
                doAction("Philosopher " + id + " " + System.nanoTime() + " : Eating"); // eating
                ((Fork) leftFork).put_down_fork(id);
                ((Fork) rightFork).put_down_fork(id);
            }
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
