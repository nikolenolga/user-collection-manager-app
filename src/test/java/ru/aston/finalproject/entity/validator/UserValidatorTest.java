package ru.aston.finalproject.entity.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.environment.AppException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidatorTest {

    UserBuilderValidator userValidator;
    String name;
    String email;
    int age;

    @BeforeEach
    void setUp() {
        userValidator = new UserBuilderValidator();
        name = "Ivan";
        email = "m@m.ru";
        age = 20;
    }

    @Test
    public void givenNameAndAge_whenValidate_thenThrowsException() {
        User.Builder builder = User.builder().setName(name).setAge(age);
        AppException exception = assertThrows(AppException.class, () -> userValidator.validate(builder));
        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    public void givenEmailAndAge_whenValidate_thenThrowsException() {
        User.Builder builder = User.builder().setEmail(email).setAge(age);
        AppException exception = assertThrows(AppException.class, () -> userValidator.validate(builder));
        assertTrue(exception.getMessage().contains("name"));
    }

    @Test
    public void givenOnlyName_whenValidate_thenThrowsException() {
        User.Builder builder = User.builder().setName(name);
        AppException exception = assertThrows(AppException.class, () -> userValidator.validate(builder));
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    public void givenNameAndEmail_whenValidate_thenThrowsException() {
        User.Builder builder = User.builder().setName(name).setEmail(email);
        AppException exception = assertThrows(AppException.class, () -> userValidator.validate(builder));
        assertTrue(exception.getMessage().contains("age should be between"));
    }
}
