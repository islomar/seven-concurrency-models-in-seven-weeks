package com.islomar;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceExample {

    public static void main(String... args) {
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(
                () -> System.out.println(LocalTime.now()),
                0, 1, TimeUnit.SECONDS);

        timer.scheduleAtFixedRate(
                () -> System.out.println("Hello world"),
                0, 200, TimeUnit.MILLISECONDS);

        System.out.println("Main: I'm done, outta here!!");
    }

}