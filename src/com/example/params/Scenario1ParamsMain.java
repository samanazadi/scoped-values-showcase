package com.example.params;

public class Scenario1ParamsMain {

    static void main() {
        UserController controller = new UserController(new UserService(new UserRepository()));
        controller.handle("Alice");
    }
}
