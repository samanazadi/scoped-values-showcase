package com.example.scopedvalue;

import static com.example.scopedvalue.UserContext.USER_ID;

public class Scenario1ScopedValueMain {

    static void main() {
        UserController controller = new UserController(new UserService(new UserRepository(false)));
        ScopedValue.where(USER_ID, "Alice").run(controller::handle);
    }
}
