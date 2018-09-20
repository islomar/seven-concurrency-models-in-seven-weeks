## Review

* Thread Pools

* Consumer-Producer pattern

* Concurrent Maps

### Find
* The documentation for ForkJoinPool—how does a fork/join pool differ from
a thread pool? When might you prefer one, and when the other?

* What is work-stealing and when might it be useful? How would you
implement work-stealing with the facilities provided by java.util.concurrent?

* What is the difference between a CountDownLatch and a CyclicBarrier? When
might you use one, and when the other?
* What is Amdahl’s law? What does it say about the maximum theoretical
speedup we might be able to get for our word-counting algorithm?

### Do

#### Make a PR against /exercises/chapter2-day3 folder

* Rewrite the producer-consumer code to use a separate “end of data” flag
instead of a poison pill. Make sure that your solution correctly handles
the cases where the producer runs faster than the consumer and vice
versa. What will happen if the consumer has already tried to remove
something from the queue when the “end of data” flag is set? Why do you
think that the poison-pill approach is so commonly used?

* Run the different versions of the word-count program on your computer,
as well as any others you can get access to. How do the performance
graphs differ from one computer to another? If you could run it on a
computer with 32 cores, do you think you would see anything close to a
32x speedup?