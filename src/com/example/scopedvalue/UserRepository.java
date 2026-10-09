package com.example.scopedvalue;

import static com.example.scopedvalue.UserContext.USER_ID;
import static com.example.scopedvalue.UserContext.getCurrentUserId;

public class UserRepository {

    private final boolean usePrivilegedQuery;

    public UserRepository(boolean usePrivilegedQuery) {
        this.usePrivilegedQuery = usePrivilegedQuery;
    }

    public void find() {
        System.out.println("      [Repository] find() reading data for user=" + getCurrentUserId() + " (USER_ID.isBound() -> " + USER_ID.isBound() + ")");

        if (usePrivilegedQuery) {
            System.out.println("      [Repository] before privilegedQuery() user=" + getCurrentUserId());
            // ScopedValue has no set() method: we cannot overwrite the caller's value. Instead, we open a nested binding that exists only while privilegedQuery() runs.
            ScopedValue.where(USER_ID, "admin").run(this::privilegedQuery);
            System.out.println("      [Repository] after  privilegedQuery() user=" + getCurrentUserId());
        }
    }

    // Deliberately does NOT read the user id: inherited context is pure overhead here.
    public void doNothing() {
    }

    private void privilegedQuery() {
        System.out.println("        [Repository] privilegedQuery() running as user=" + getCurrentUserId());
    }
}
