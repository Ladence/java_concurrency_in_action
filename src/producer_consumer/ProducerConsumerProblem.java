package producer_consumer;

import java.util.concurrent.Semaphore;

public class ProducerConsumerProblem {
    private static Semaphore producerSemaphore = new Semaphore(10);
    private static Semaphore consumerSemaphore = new Semaphore(0);
    private static int[] buffer = new int[10];
    private static int currentInd = 0;

    static void put(int value) {
        try {
            producerSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        buffer[currentInd++] = value;
        System.out.println("Putted : " + value);
        consumerSemaphore.release();
    }

    static void get(int ind) {
        try {
            consumerSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Get it : " + buffer[ind]);
        producerSemaphore.release();
    }

    private static class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                put(i);
            }
        }
    }

    private static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                get(i);
            }
        }
    }

    public static void main(String[] args) {
        Thread producerThread = new Thread(new Producer());
        Thread consumerThread = new Thread(new Consumer());

        producerThread.start();
        consumerThread.start();
    }
}
