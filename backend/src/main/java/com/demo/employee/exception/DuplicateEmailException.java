package com.demo.employee.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Employee with email '" + email + "' already exists");
    }
}
