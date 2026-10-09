package com.example.threadlocal;

import static com.example.threadlocal.UserContext.getCurrentUserId;

public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public void handle() {
        System.out.println("  [Controller] before service.process() user=" + getCurrentUserId());
        service.process();
        System.out.println("  [Controller] after  service.process() user=" + getCurrentUserId());
    }

    public void handleAndFail() {
        handle();
        System.out.println("  [Controller] throwing an exception!");
        throw new IllegalStateException("simulated failure while handling the request");
    }
}
