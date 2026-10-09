package com.example.threadlocal;

import static com.example.threadlocal.UserContext.USER_ID;

public class Scenario1ThreadLocalMain {

    static void main() {
        UserController controller = new UserController(new UserService(new UserRepository(false)));
        USER_ID.set("Alice");
        try {
            controller.handle();
        } finally {
            USER_ID.remove();
        }
    }
}
