package com.example.threadlocal;

import static com.example.threadlocal.UserContext.USER_ID;
import static com.example.threadlocal.UserContext.getCurrentUserId;

public class UserRepository {

    private final boolean usePrivilegedQuery;

    public UserRepository(boolean usePrivilegedQuery) {
        this.usePrivilegedQuery = usePrivilegedQuery;
    }

    public void find() {
        System.out.println("      [Repository] find() reading data for user=" + getCurrentUserId());

        if (usePrivilegedQuery) {
            System.out.println("      [Repository] before privilegedQuery() user=" + getCurrentUserId());
            USER_ID.set("admin");
            privilegedQuery();
            // BUG: the old value is never restored. Every caller up the stack now sees "admin".
            System.out.println("      [Repository] after  privilegedQuery() user=" + getCurrentUserId());
        }
    }

    public void doNothing() {
    }

    private void privilegedQuery() {
        System.out.println("        [Repository] privilegedQuery() running as user=" + getCurrentUserId());
    }
}
