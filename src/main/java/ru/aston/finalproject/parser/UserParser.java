package ru.aston.finalproject.parser;

import lombok.AllArgsConstructor;
import ru.aston.finalproject.entity.user.BuildUser;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.AppException;

import static ru.aston.finalproject.util.ConstantMethods.checkedStringOnEmpty;
import static ru.aston.finalproject.util.Message.USER_CANNOT_BE_NULL;

@AllArgsConstructor
public class UserParser extends AbstractParser<User> {

    private static final String AGE = "age";
    private static final String DELIMITER = " : ";
    public static final String USER_FORMAT = String.format("name%semail%sage", DELIMITER, DELIMITER);
    private static final int LENGTH_PARAMETER = 3;
    private final Validate<User.Builder> validator;

    @Override
    public String parseToString(User user) {
        if (user == null) {
            throw new AppException(USER_CANNOT_BE_NULL);
        }
        return exampleEntity(user.getName(), user.getEmail(), user.getAge());
    }

    private String exampleEntity(String fieldOne, String fieldTwo, int fieldInt) {
        return String.format(fieldOne + DELIMITER + fieldTwo + DELIMITER + fieldInt);
    }

    @Override
    public User parse(String data) {
        return parse(data, DELIMITER);
    }

    @Override
    public User parse(String data, String delimiter) {

        checkedStringOnEmpty(data, "data in parser");

        BuildUser buildConcreteEntity = new BuildUser(validator);
        String[] dataArray = preparingForParsing(data, delimiter, LENGTH_PARAMETER);

        String name = dataArray[0].trim();
        String email = dataArray[1].trim();
        int age = createdDigitFromFirstInteger(dataArray[2].trim(), AGE);

        return buildConcreteEntity.capitalizeNameAndNormalizedEmail(name, email, age);
    }

}
