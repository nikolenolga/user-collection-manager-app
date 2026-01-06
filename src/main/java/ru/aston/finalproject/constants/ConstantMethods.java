package ru.aston.finalproject.constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static ru.aston.finalproject.constants.ConstantFields.DELIMITER;
import static ru.aston.finalproject.constants.ConstantFields.EMAIL_FORM;
import static ru.aston.finalproject.constants.ConstantFields.KM;
import static ru.aston.finalproject.constants.ConstantFields.LENGTH_PARAMETER;
import static ru.aston.finalproject.constants.ConstantFields.MAX_AGE;
import static ru.aston.finalproject.constants.ConstantFields.MIN_AGE;
import static ru.aston.finalproject.constants.ConstantFields.ZERO;

public class ConstantMethods {

    public static void checkedStringOnEmpty(String string, String field){
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format("%s cannot be empty", field));
        }
    }

    public static void checkedName(String name){
        checkedStringOnEmpty(name, "name");
        if (!name.equals(cleanStringFromDigit(name))){
            throw new IllegalArgumentException(String.format("%s is not a valid name", name));
        }
    }

    public static void checkedEmail(String email) {
        checkedStringOnEmpty(email,  "email");
        if (!email.matches(EMAIL_FORM)) {
            throw new IllegalArgumentException(String.format("Invalid email %s", email));
        }
    }

    public static void checkedAge(int age) {
        if (age <= MIN_AGE) {
            throw new IllegalArgumentException(String.format("Age cannot be below %d", MIN_AGE));
        }
        if (age >= MAX_AGE) {
            throw new IllegalArgumentException(String.format("Age cannot be above %d", MAX_AGE));
        }
    }

    public static void checkedZero(int intValue) {
        if (intValue == ZERO) {
            throw new IllegalArgumentException(String.format("%s cannot be zero", intValue));
        }
    }

    public static void checkedMileageInKilometers(String mileageInKilometers){
        checkedStringOnEmpty(mileageInKilometers, "mileageInKilometers");
        String km = cleanStringFromDigit(mileageInKilometers);
        if (!km.equals(KM)) {
            throw new IllegalArgumentException(
                    String.format("Invalid args, mileageInKilometers need content %s", KM));
        }
        createdStringOnlyDigits(mileageInKilometers);
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

    public static String exampleEntity (String fieldOne, String fieldTwo, int fieldInt) {
        return String.format(fieldOne + DELIMITER +  fieldTwo + DELIMITER + fieldInt);
    }

    public static String[] preparingForParsing(String data, String delimiter) {
        String[] dataArray = data.split(delimiter);
        if (dataArray.length != LENGTH_PARAMETER) {
            throw new IllegalArgumentException(String.format("Invalid data %s", data));
        }
        for (int i = 0; i < LENGTH_PARAMETER; i++) {
            checkedStringOnEmpty(dataArray[i], String.format("data at index %d", i));
        }
        return dataArray;
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz == null || clazz == Object.class) {
            return fields;
        }
        fields.addAll(List.of(clazz.getDeclaredFields()));
        return fields;
    }
}