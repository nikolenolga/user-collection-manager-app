package ru.aston.finalproject.entity.user;

import lombok.AllArgsConstructor;
import ru.aston.finalproject.entity.validator.Validate;

import static ru.aston.finalproject.util.ConstantFields.SPACE;
import static ru.aston.finalproject.util.ConstantMethods.checkedStringOnEmpty;

@AllArgsConstructor
public class BuildUser {
    private final Validate<User.Builder> validator;

    public User capitalizeNameAndNormalizedEmail(String name, String email, int age) {
        String normalizedEmail = email.toLowerCase();
        String capitalizedName = capitalize(name);
        return User.builder().setName(capitalizedName).setEmail(normalizedEmail).setAge(age).build(validator);
    }

    private String capitalize(String name) {

        checkedStringOnEmpty(name, "name");

        if (name.split(SPACE).length > 1) {
            String[] compoundName = name.split(SPACE);
            StringBuilder userName = new StringBuilder();
            for (String s : compoundName) {
                userName.append(capitalize(s)).append(" ");
            }
            return userName.toString().trim();
        }

        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
