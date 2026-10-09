package com.example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class ScopedValueDemo {
    private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();
    private static final ScopedValue<String> TX_ID = ScopedValue.newInstance();

    static void main() {
        System.out.println("Before binding, bound? " + USER_ID.isBound());
        ScopedValue.where(USER_ID, "Alice").run(ScopedValueDemo::handleRequest);
        System.out.println("After scope ends, bound? " + USER_ID.isBound());

        System.out.println();
        String result = ScopedValue.where(USER_ID, "Bob").call(() -> {
            System.out.println("[callable] user = " + USER_ID.get());
            return "done";
        });
        System.out.println("[callable] result = " + result);

        System.out.println();
        ScopedValue.where(USER_ID, "Mark").where(TX_ID, "tx1").run(() -> {
            System.out.println("[multiple wheres] USER = " + USER_ID.get());
            System.out.println("[multiple wheres] TX = " + TX_ID.get());
        });
    }

    static void handleRequest() {
        System.out.println("[handleRequest] user = " + USER_ID.get());
        service();
        childThreads();
        plainThread();
        rebinding();
    }

    static void service() {
        repository();
    }

    static void repository() {
        System.out.println("[repository] user = " + USER_ID.get());
    }

    static void childThreads() {
        try (var scope = StructuredTaskScope.open()) {
            StructuredTaskScope.Subtask<String> t1 = scope.fork(() -> "[child-1 " + Thread.currentThread() + "] user = " + USER_ID.get());
            StructuredTaskScope.Subtask<String> t2 = scope.fork(() -> "[child-2 " + Thread.currentThread() + "] user = " + USER_ID.get());
            scope.join();
            System.out.println(t1.get());
            System.out.println(t2.get());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void plainThread() {
        Thread t = Thread.ofVirtual().start(() -> System.out.println("[plain thread] bound? " + USER_ID.isBound()));
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void rebinding() {
        System.out.println("[before nested] user = " + USER_ID.get());
        ScopedValue.where(USER_ID, "admin").run(() -> System.out.println("[nested scope] user = " + USER_ID.get()));
        System.out.println("[after nested] user = " + USER_ID.get());
    }
}