package ru.aston.finalproject.entity.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.aston.finalproject.entity.validator.Validate;

@Getter
@EqualsAndHashCode
public class User implements Comparable<User> {

    private final String name;
    private final String email;
    private final int age;

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
    }

    public static Builder builder() {
        return new Builder();
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

    @Getter
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

        public User build(Validate<Builder> validator) {
            validator.validate(this);
            return new User(this);
        }

    }
}