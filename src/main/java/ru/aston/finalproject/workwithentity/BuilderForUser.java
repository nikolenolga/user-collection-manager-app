package ru.aston.finalproject.workwithentity;

import lombok.Getter;
import ru.aston.finalproject.validators.UserValidator;
import ru.aston.finalproject.validators.Validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BuilderForUser implements Builder<User> {

    private String name;
    private String email;
    private int age;

    BuilderForUser() {
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz == null || clazz == Object.class) {
            return fields;
        }
        fields.addAll(List.of(clazz.getDeclaredFields()));
        return fields;
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
    public BuilderForUser setField(String fieldType, String fieldName, Object value) {
        if (fieldType.equals("String")) {
            if (fieldName.equals("name")) {
                value = this.name;
                return this;
            }
            if (fieldName.equals("email")) {
                value = this.email;
                return this;
            }
        }
        if (fieldType.equals("int")) {
            if (fieldName.equals("age")) {
                value = this.age;
                return this;
            }
        }
        return null;
    }

    @Override
    public User build() {
        Validator<User> validator = new UserValidator();
        validator.validate(name, email, age);
        return new User(this);
    }

    public static void main(String[] args) {
//        BuilderForUser builder = new BuilderForUser();
//        for (Field field : builder.getAllFields(Bus.class)) {
//            System.out.println(field.getName() + " " +  field.getType().getSimpleName());
//        }

        BuildConcreteEntity buildConcreteEntity = new BuildConcreteEntity();
        Bus bus = buildConcreteEntity.buildBus("Mercedes", "234243km.", 3453);
        System.out.println(bus);

    }
}
