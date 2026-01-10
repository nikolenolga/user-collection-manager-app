package ru.aston.finalproject.util;

import ru.aston.finalproject.config.AppException;

import static ru.aston.finalproject.util.ConstantFields.DELIMITER;
import static ru.aston.finalproject.util.ConstantFields.EMAIL_FORM;
import static ru.aston.finalproject.util.ConstantFields.LENGTH_PARAMETER;
import static ru.aston.finalproject.util.ConstantFields.MAX_AGE;
import static ru.aston.finalproject.util.ConstantFields.MIN_AGE;
import static ru.aston.finalproject.util.ConstantFields.ZERO;

public class ConstantMethods {

    public static void checkedStringOnEmpty(String string, String field) {
        if (string == null || string.trim().isEmpty()) {
            throw new AppException(String.format("%s cannot be empty", field));
        }
    }

    public static void checkedName(String name) {
        checkedStringOnEmpty(name, "name");
        if (!name.equals(cleanStringFromDigit(name))) {
            throw new AppException(String.format("%s is not a valid name", name));
        }
    }

    public static void checkedEmail(String email) {
        checkedStringOnEmpty(email, "email");
        if (!email.matches(EMAIL_FORM)) {
            throw new AppException(String.format("Invalid email %s", email));
        }
    }

    public static void checkedAge(int age) {
        if (age < MIN_AGE) {
            throw new AppException(String.format("Age cannot be below %d: %d", MIN_AGE, age));
        }
        if (age > MAX_AGE) {
            throw new AppException(String.format("Age cannot be above %d: %d", MAX_AGE, age));
        }
    }

    public static void checkedZero(int intValue) {
        if (intValue == ZERO) {
            throw new AppException(String.format("%s cannot be zero", intValue));
        }
    }

    private static String createdStringOnlyDigits(String string) {
        checkedStringContainDigitsOnly(string);
        return string;
    }

    private static void checkedStringContainDigitsOnly(String string) {
        string = string.replaceAll("\\D+", " ").trim();
        checkedStringOnEmpty(string, "digits");
    }

    private static String cleanStringFromDigit(String string) {
        string = string.replaceAll("\\d+", " ").trim();
        checkedStringOnEmpty(string, "digits");
        return string;
    }

    public static int createdDigitFromFirstInteger(String string) {
        String numbersOnly = createdStringOnlyDigits(string);
        checkedStringOnEmpty(numbersOnly, "numbers");
        return Integer.parseInt(numbersOnly.split(" ")[ZERO]);
    }

    public static String exampleEntity(String fieldOne, String fieldTwo, int fieldInt) {
        return String.format(fieldOne + DELIMITER + fieldTwo + DELIMITER + fieldInt);
    }

    public static String[] preparingForParsing(String data, String delimiter) {
        String[] dataArray = data.split(delimiter);
        if (dataArray.length != LENGTH_PARAMETER) {
            throw new AppException(String.format("Invalid data %s", data));
        }
        for (int i = 0; i < LENGTH_PARAMETER; i++) {
            checkedStringOnEmpty(dataArray[i], String.format("data at index %d", i));
        }
        return dataArray;
    }

}