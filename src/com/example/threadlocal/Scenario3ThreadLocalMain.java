package com.example.threadlocal;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.threadlocal.UserContext.USER_ID;

public class Scenario3ThreadLocalMain {

    static void main() throws InterruptedException, ExecutionException {
        UserController controller = new UserController(new UserService(new UserRepository(false)));

        System.out.println("--- Part A: the framework never calls remove() ---");
        ExecutorService executor1 = Executors.newSingleThreadExecutor();
        System.out.println("task1:");
        executor1.submit(() -> {
            USER_ID.set("Alice");
            controller.handle();
        }).get();
        System.out.println("task2:");
        executor1.submit(controller::handle).get(); // SECURITY BUG: the anonymous request ran as Alice
        executor1.close();

        System.out.println();
        System.out.println("--- Part B: remove() exists, but not in a finally block, and the first request throws ---");
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        System.out.println("task1:");
        try {
            executor2.submit(() -> {
                USER_ID.set("Alice");
                controller.handleAndFail();
                USER_ID.remove(); // Not in a finally block: never reached because the controller throws.
            }).get();
        } catch (ExecutionException e) {
            System.out.println("[Framework] request failed: " + e.getCause());
        }
        System.out.println("task2:");
        executor2.submit(controller::handle).get(); // SECURITY BUG: the exception skipped remove(), the anonymous request ran as Alice
        executor2.close();
    }
}
