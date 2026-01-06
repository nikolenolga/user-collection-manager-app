package ru.aston.finalproject.workwithentity;

import lombok.Getter;
import ru.aston.finalproject.validators.UserValidator;
import ru.aston.finalproject.validators.Validator;

import java.lang.reflect.Field;
import static ru.aston.finalproject.constants.ConstantMethods.getAllFields;

@Getter
public class BuilderForUser implements Builder<User> {

    private String name;
    private String email;
    private int age;

    BuilderForUser() {
    }

    @Override
    public BuilderForUser setField(String fieldName, Object value) {

        for (Field field : getAllFields(Bus.class)) {
            if (field.getName().equals(fieldName)) {
                switch (fieldName) {
                    case "name" -> {
                        this.name = (String) value;
                        return this;
                    }
                    case "email" -> {
                        this.email = (String) value;
                        return this;
                    }
                    case "age" -> {
                        this.age = (int) value;
                        return this;
                    }
                }
            }
        }
        throw new IllegalArgumentException("It's not possible to set this field");
    }

    @Override
    public User build() {
        Validator<User> validator = new UserValidator();
        validator.validate(name, email, age);
        return new User(this);
    }

    public static void main(String[] args) {

        BuildConcreteEntity buildConcreteEntity = new BuildConcreteEntity();
        Bus bus = buildConcreteEntity.buildBus("Mercedes", "234243km.", 3453);
        System.out.println(bus);

    }
}
