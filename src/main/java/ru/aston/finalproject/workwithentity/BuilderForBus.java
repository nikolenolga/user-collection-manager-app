package ru.aston.finalproject.workwithentity;

import lombok.Getter;
import ru.aston.finalproject.validators.BusValidator;
import ru.aston.finalproject.validators.Validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BuilderForBus implements Builder<Bus> {

    private String model;
    private String mileageInKilometers;
    private int number;

    BuilderForBus() {
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz == null || clazz == Object.class) {
            return fields;
        }
        fields.addAll(List.of(clazz.getDeclaredFields()));
        return fields;
    }

    @Override
    public BuilderForBus setField(String fieldType, String fieldName, Object value) {

        for (Field field : getAllFields(Bus.class)) {
            if (field.getType().getSimpleName().equals(fieldType)) {
                if (fieldType.equals("String")) {
                    if (field.getName().equals(fieldName) && fieldName.equals("model")) {
                        this.model = (String) value;
                        return this;
                    }
                    if (field.getName().equals(fieldName) && fieldName.equals("mileageInKilometers")) {
                        this.mileageInKilometers = (String) value;
                        return this;
                    }
                }
                if (fieldType.equals("int")) {
                    if (fieldName.equals("number")) {
                        this.number = (int) value;
                        return this;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Bus build() {
        Validator<Bus> validator = new BusValidator();
        validator.validate(model, mileageInKilometers, number);
        return new Bus(this);
    }
}
