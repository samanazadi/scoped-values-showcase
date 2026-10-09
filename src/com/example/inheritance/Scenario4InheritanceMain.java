package com.example.inheritance;

import java.util.List;
import java.util.stream.IntStream;

public class Scenario4InheritanceMain {

    private static final int CONTEXT_SIZE = 100;
    private static final int[] THREAD_COUNTS = {10, 100, 1_000, 10_000, 100_000, 1_000_000};
    private static final double MB = 1024.0 * 1024.0;
    private static final List<InheritableThreadLocal<String>> INHERITABLE_THREAD_LOCALS = IntStream.range(0, CONTEXT_SIZE).mapToObj(i -> new InheritableThreadLocal<String>()).toList();
    private static final List<ScopedValue<String>> SCOPED_VALUES = IntStream.range(0, CONTEXT_SIZE).mapToObj(i -> ScopedValue.<String>newInstance()).toList();
    private static final com.example.threadlocal.UserService THREAD_LOCAL_SERVICE = new com.example.threadlocal.UserService(new com.example.threadlocal.UserRepository(false));
    private static final com.example.scopedvalue.UserService SCOPED_VALUE_SERVICE = new com.example.scopedvalue.UserService(new com.example.scopedvalue.UserRepository(false));

    static void main() throws Exception {
        for (Mode mode : List.of(Mode.SV, Mode.ITL)) {
            System.out.println("Mode: " + mode);
            System.out.printf("%12s | %14s\n", "threads", "extra heap");

            for (int threadCount : THREAD_COUNTS) {
                long extraHeap = measure(mode, threadCount);
                System.out.printf("%,12d | %,11.1f MB\n", threadCount, extraHeap / MB);
            }
        }
    }

    private static long measure(Mode mode, int threadCount) throws Exception {
        System.gc();
        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // rough measurement
        long during = switch (mode) {
            case ITL -> {
                long result;
                for (int i = 0; i < CONTEXT_SIZE; i++) {
                    INHERITABLE_THREAD_LOCALS.get(i).set("value-" + i);
                }
                try {
                    result = THREAD_LOCAL_SERVICE.processInParallel(threadCount);
                } finally {
                    INHERITABLE_THREAD_LOCALS.forEach(ThreadLocal::remove);
                }
                yield result;
            }
            case SV -> {
                ScopedValue.Carrier carrier = ScopedValue.where(SCOPED_VALUES.getFirst(), "value-0");
                for (int i = 1; i < CONTEXT_SIZE; i++) {
                    carrier = carrier.where(SCOPED_VALUES.get(i), "value-" + i);
                }
                yield carrier.call(() -> SCOPED_VALUE_SERVICE.processInParallel(threadCount));
            }
        };
        return during - before;
    }

    enum Mode {ITL, SV}
}
