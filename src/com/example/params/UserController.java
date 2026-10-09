package com.example.params;

public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public void handle(String userId) {
        service.process(userId);
    }
}
