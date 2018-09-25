package com.levdemidov;

/**
 * Hello world!
 *
 */
public class DiningPhilosophers 
{
    public static void main( String[] args ) throws InterruptedException
    {
        Philosopher[] philosophers = new Philosopher[5];

        for (int i = 0; i < 5; ++i)
          philosophers[i] = new Philosopher();
        for (int i = 0; i < 5; ++i) {
          philosophers[i].setLeft(philosophers[(i + 4) % 5]);
          philosophers[i].setRight(philosophers[(i + 1) % 5]);
          philosophers[i].start();
        }
        for (int i = 0; i < 5; ++i)
          philosophers[i].join();

        System.out.println("Done");
    }
}
