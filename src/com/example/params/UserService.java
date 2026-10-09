package com.example.params;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void process(String userId) {
        repository.find(userId);
    }
}
