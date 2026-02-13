package ru.aston.finalproject.entity.validator;

import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.environment.AppException;

import static ru.aston.finalproject.util.ConstantFields.MAX_AGE;
import static ru.aston.finalproject.util.ConstantFields.MIN_AGE;
import static ru.aston.finalproject.util.ConstantMethods.checkedStringOnEmpty;
import static ru.aston.finalproject.util.Message.AGE_SHOULD_BETWEEN_X_X_X;
import static ru.aston.finalproject.util.Message.X_IS_NOT_A_VALID_X;

public class UserBuilderValidator implements Validate<User.Builder> {

    private static final String EMAIL_FORM = "^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$";
    private static final String DIGITS_REGS = "\\d+";
    private static final String NAME = "Name";
    private static final String EMAIL = "email";

    @Override
    public void validate(User.Builder builder) throws AppException {
        checkedFieldsNotNull(builder);
        checkedName(builder.getName());
        checkedEmail(builder.getEmail());
        checkedAge(builder.getAge());
    }

    private void checkedName(String name) {
        checkedStringOnEmpty(name, NAME);
        if (!name.equals(cleanStringFromDigit(name)) || name.matches(EMAIL_FORM)) {
            throw new AppException(String.format(X_IS_NOT_A_VALID_X, name, NAME));
        }
    }

    private String cleanStringFromDigit(String string) {
        string = string.replaceAll(DIGITS_REGS, "").trim();
        try {
            checkedStringOnEmpty(string, NAME);
        } catch (AppException e) {
            System.out.println(string + " " + e.getMessage());
        }
        return string;
    }

    private void checkedEmail(String email) {
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
}
