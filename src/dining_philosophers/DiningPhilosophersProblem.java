package dining_philosophers;

import java.util.concurrent.Semaphore;


public class DiningPhilosophersProblem {
    private static final int NUMBER = 5;

    public static void main(String[] args) throws InterruptedException {
        Fork[] forks = new Fork[NUMBER];
        Philosopher[] philosophers = new Philosopher[NUMBER];

        for (int i = 0; i < NUMBER; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < NUMBER; i++) {
            if (i != NUMBER - 1) {
                philosophers[i] = new Philosopher(forks[i], forks[i+1], i);
            } else {
                philosophers[i] = new Philosopher(forks[0], forks[i], i);
            }

            new Thread(philosophers[i]).start();
        }

    }


    private static class Fork {
        private Semaphore forkSem;

        private Fork() {
            forkSem = new Semaphore(1);
        }

        private boolean take() {
            return forkSem.tryAcquire();
        }

        private void put() {
            forkSem.release();
        }
    }

    private static class Philosopher implements Runnable {
        private Fork leftFork;
        private Fork rightFork;
        private int ind;

        private Philosopher(Fork leftFork, Fork rightFork, int ind) {
            this.leftFork = leftFork;
            this.rightFork = rightFork;
            this.ind = ind;
        }

        @Override
        public void run() {
            try {
                System.out.println("Philosopher #" + ind + "is thinking now");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            while (true) {
                eat();
            }
        }

        private void eat() {
            if (leftFork.take()) {
                if (rightFork.take()) {
                    System.out.println("Philosopher #" + ind + " is eating now");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    leftFork.put();
                    rightFork.put();
                    System.out.println("Philosopher #" + ind + " has been end eating");
                } else {
                    leftFork.put();
                }
            }
        }
    }
}
