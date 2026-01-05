package ru.aston.finalproject.workwithentity;

import lombok.Getter;
import ru.aston.finalproject.validators.UserValidator;
import ru.aston.finalproject.validators.Validator;

@Getter
public class BuilderForUser implements Builder<User> {

    private String name;
    private String email;
    private int age;

    BuilderForUser() {
    }

    public BuilderForUser setName(String name) {
        this.name = name;
        return this;
    }

    public BuilderForUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public BuilderForUser setAge(int age) {
        this.age = age;
        return this;
    }

    @Override
    public User build() {
        Validator<User> validator = new UserValidator();
        validator.validate(name, email, age);
        return new User(this);
    }
}
