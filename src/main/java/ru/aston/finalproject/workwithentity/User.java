package ru.aston.finalproject.workwithentity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class User implements Comparable<User> {

    private final String name;
    private final String email;
    private final int age;

    User(BuilderForUser builder) {
        this.name = builder.getName();
        this.email = builder.getEmail();
        this.age = builder.getAge();
    }

    public static BuilderForUser builder() {
        return new BuilderForUser();
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
}
