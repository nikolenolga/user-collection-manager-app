package ru.aston.finalproject.workwithentity;

import lombok.Getter;
import ru.aston.finalproject.validators.BusValidator;
import ru.aston.finalproject.validators.Validator;

import java.lang.reflect.Field;

import static ru.aston.finalproject.constants.ConstantMethods.getAllFields;

@Getter
public class BuilderForBus implements Builder<Bus> {

    private String model;
    private String mileageInKilometers;
    private int number;

    BuilderForBus() {
    }

    @Override
    public BuilderForBus setValueForField(String fieldName, Object value) {

        for (Field field : getAllFields(Bus.class)) {
            if (field.getName().equals(fieldName)) {
                return switchFieldAndSetValue(fieldName, value);
            }
        }
        throw new IllegalArgumentException("It's not possible to set this field");
    }

    private BuilderForBus switchFieldAndSetValue(String fieldName, Object value) {
        switch (fieldName) {
            case "model" -> {
                this.model = (String) value;
                return this;
            }
            case "mileageInKilometers" -> {
                this.mileageInKilometers = (String) value;
                return this;
            }
            case "number" -> {
                this.number = (int) value;
                return this;
            }
            default -> throw new IllegalArgumentException("It's not possible to set this field");
        }
    }

    @Override
    public Bus build() {
        Validator<Bus> validator = new BusValidator();
        validator.validate(model, mileageInKilometers, number);
        return new Bus(this);
    }
}