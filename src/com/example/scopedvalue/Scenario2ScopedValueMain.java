package com.example.scopedvalue;

import static com.example.scopedvalue.UserContext.USER_ID;
import static com.example.scopedvalue.UserContext.getCurrentUserId;

public class Scenario2ScopedValueMain {

    static void main() {
        UserController controller = new UserController(new UserService(new UserRepository(true)));

        System.out.println("[Framework] incoming request, user=Alice");
        ScopedValue.where(USER_ID, "Alice").run(() -> {
            controller.handle();
            System.out.println("[Framework] after controller.handle() user=" + getCurrentUserId());
        });
    }
}
