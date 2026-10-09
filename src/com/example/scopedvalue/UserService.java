package com.example.scopedvalue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

import static com.example.scopedvalue.UserContext.getCurrentUserId;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void process() {
        System.out.println("    [Service] before repository.find() user=" + getCurrentUserId());
        repository.find();
        System.out.println("    [Service] after  repository.find() user=" + getCurrentUserId());
    }

    public long processInParallel(int threadCount) throws InterruptedException, ExecutionException {
        CountDownLatch started = new CountDownLatch(threadCount);
        CountDownLatch release = new CountDownLatch(1);

        try (var scope = StructuredTaskScope.<Void>open()) {
            for (int i = 0; i < threadCount; i++) {
                scope.fork(() -> {
                    repository.doNothing();
                    started.countDown();
                    release.await();
                    return null;
                });
            }

            started.await();
            System.gc();
            Runtime runtime = Runtime.getRuntime();
            long usedHeap = runtime.totalMemory() - runtime.freeMemory();

            release.countDown();
            scope.join();
            return usedHeap;
        }
    }
}
