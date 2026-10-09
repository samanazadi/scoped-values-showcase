package com.example.threadlocal;

public final class UserContext {

    public static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    private UserContext() {
    }

    public static String getCurrentUserId() {
        String userId = USER_ID.get();
        return userId == null ? "<none>" : userId;
    }
}
