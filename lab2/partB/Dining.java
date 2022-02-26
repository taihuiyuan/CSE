package lab2.partB;

public class Dining{
    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Fork(i+1);
        }
        for (int i = 0; i < philosophers.length; i++) {
            Object left = forks[(i+1)%philosophers.length];
            Object right = forks[i];
            if (i == philosophers.length - 1){
                philosophers[i] = new Philosopher(i+1, right, left);
            }else {
                philosophers[i] = new Philosopher(i+1, left, right);
            }
        }
        for (Philosopher philosopher: philosophers){
            new Thread(philosopher).start();
        }
    }
}
