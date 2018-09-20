If you do any of the programming exercises here, feel free to make a PR against this repo.  We will review these PR's and merge them.

## Review

* ReentrantLock

* Condition Variable

* Atomic Variables

* Volatile fields

## Discussion
  
### Find
* ReentrantLock supports a fairness parameter. What does it mean for a lock
to be “fair”? Why might you choose to use a fair lock? Why might you not?
    http://www.devinline.com/2015/10/Lock-Vs-synchronized-in-java.html

* What is ReentrantReadWriteLock? How does it differ from ReentrantLock? When
might you use it?

    https://stackoverflow.com/questions/18354339/reentrantreadwritelock-whats-the-difference-between-readlock-and-writelock

*  What is a “spurious wakeup”? When can one happen and why doesn’t a
well-written program care if one does?
    https://en.wikipedia.org/wiki/Spurious_wakeup


*  What is AtomicIntegerFieldUpdater? How does it differ from AtomicInteger? When
might you use it?
    https://www.javamex.com/tutorials/synchronization_concurrency_7_atomic_updaters.shtml

### Do

* What would happen if the loop within the “dining philosophers” implemen-
tation that uses condition variables was replaced with a simple if statement?

*  What failure modes might you see? What would happen if the call to signal()
was replaced by signalAll()? What problems (if any) would this cause?
* Just as intrinsic locks are more limited than ReentrantLock, they also support
a more limited type of condition variable. Rewrite the dining philosophers
to use an intrinsic lock plus the wait() and notify() or notifyAll() methods. Why
is it less efficient than using ReentrantLock?
* Write a version of ConcurrentSortedList that uses a single lock instead of
hand-over-hand locking. Benchmark it against the other version. Does
hand-over-hand locking provide any performance advantage? When might
it be a good choice? When might it not?