package com.example.threadlocal;

import static com.example.threadlocal.UserContext.USER_ID;
import static com.example.threadlocal.UserContext.getCurrentUserId;

public class Scenario2ThreadLocalMain {

    static void main() {
        UserController controller = new UserController(new UserService(new UserRepository(true)));

        System.out.println("[Framework] incoming request, user=Alice");
        USER_ID.set("Alice");
        try {
            controller.handle();
            System.out.println("[Framework] after controller.handle() user=" + getCurrentUserId());
        } finally {
            USER_ID.remove();
        }
    }
}
