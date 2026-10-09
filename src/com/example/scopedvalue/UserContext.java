package com.example.scopedvalue;

public final class UserContext {

    public static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

    private UserContext() {
    }

    public static String getCurrentUserId() {
        return USER_ID.isBound() ? USER_ID.get() : "<unbound>";
    }
}
