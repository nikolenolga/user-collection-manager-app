package ru.aston.finalproject.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.aston.finalproject.appEnviroment.AppException;

import static ru.aston.finalproject.staticTools.ConstantFields.DIGITS;
import static ru.aston.finalproject.staticTools.ConstantFields.MAX_AGE;
import static ru.aston.finalproject.staticTools.ConstantFields.MIN_AGE;
import static ru.aston.finalproject.staticTools.Message.AGE_SHOULD_BETWEEN_X_X_X;
import static ru.aston.finalproject.staticTools.Message.X_IS_NOT_A_VALID_X;
import static ru.aston.finalproject.staticTools.ConstantMethods.checkedStringOnEmpty;

@Getter
@EqualsAndHashCode
public class User implements Comparable<User> {

    private final static String EMAIL_FORM = "^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$";
    private final static String DIGITS_REGS = "\\d+";
    private final String name;
    private final String email;
    private final int age;

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
    }

    @Override
    public int compareTo(User o) {
        if (!this.name.equals(o.name)) {
            return this.name.compareTo(o.name);
        } else if (!this.email.equals(o.email)) {
            return this.email.compareTo(o.email);
        } else {
            return Integer.compare(this.age, o.age);
        }
    }

    @Override
    public String toString() {
        return String.format("User{name: %s, email: %s, age: %d}", this.name, this.email, this.age);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String email;
        private int age;

        private Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public User build() {
            validate();
            return new User(this);
        }

        private void validate() {
            checkedName(name);
            checkedEmail(email);
            checkedAge(age);
        }

        private void checkedName(String name) {
            String NAME = "Name";
            checkedStringOnEmpty(name, NAME);
            if (!name.equals(cleanStringFromDigit(name))) {
                throw new AppException(String.format(X_IS_NOT_A_VALID_X, name, NAME));
            }
        }

        private void checkedEmail(String email) {
            String EMAIL = "email";
            checkedStringOnEmpty(email, EMAIL);
            if (!email.matches(EMAIL_FORM)) {
                throw new AppException(String.format(X_IS_NOT_A_VALID_X, email, EMAIL));
            }
        }

        private void checkedAge(int age) {
            if (age < MIN_AGE || age > MAX_AGE) {
                throw new AppException(String.format(AGE_SHOULD_BETWEEN_X_X_X, MIN_AGE, MAX_AGE, age));
            }
        }

        private static String cleanStringFromDigit(String string) {
            string = string.replaceAll(DIGITS_REGS, "").trim();
            checkedStringOnEmpty(string, DIGITS);
            return string;
        }

        public static boolean isName(String name) {
            String NAME = "Name";
            checkedStringOnEmpty(name, NAME);
            return name.equals(cleanStringFromDigit(name));
        }

        public static boolean isEmail(String email) {
            String EMAIL = "email";
            checkedStringOnEmpty(email, EMAIL);
            return email.toLowerCase().matches(EMAIL_FORM);
        }

        public static boolean isAge(int age) {
            return (age >= MIN_AGE && age <= MAX_AGE);
        }
    }
}