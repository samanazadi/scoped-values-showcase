package com.example.scopedvalue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.scopedvalue.UserContext.USER_ID;

public class Scenario3ScopedValueMain {

    static void main() throws InterruptedException, ExecutionException {
        UserController controller = new UserController(new UserService(new UserRepository(false)));

        System.out.println("--- Part A: two normal requests ---");
        ExecutorService executor1 = Executors.newSingleThreadExecutor();
        System.out.println("task1:");
        executor1.submit(() -> ScopedValue.where(USER_ID, "Alice").run(controller::handle)).get();
        System.out.println("task2:");
        executor1.submit(controller::handle).get();
        executor1.close();

        System.out.println();
        System.out.println("--- Part B: the first request throws inside run() ---");
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        System.out.println("task1:");
        try {
            executor2.submit(() -> ScopedValue.where(USER_ID, "Alice").run(controller::handleAndFail)).get();
        } catch (ExecutionException e) {
            System.out.println("[Framework] request failed: " + e.getCause());
        }
        System.out.println("task2:");
        executor2.submit(controller::handle).get();
        executor2.close();
    }
}
