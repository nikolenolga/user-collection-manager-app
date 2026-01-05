package ru.aston.finalproject.workwithentity;

import lombok.Getter;
import ru.aston.finalproject.validators.BusValidator;
import ru.aston.finalproject.validators.Validator;

@Getter
public class BuilderForBus implements Builder<Bus> {

    private String model;
    private String mileageInKilometers;
    private int number;

    BuilderForBus() {
    }

    public BuilderForBus setModel(String model) {
        this.model = model;
        return this;
    }

    public BuilderForBus setMileageInKilometers(String mileageInKilometers) {
        this.mileageInKilometers = mileageInKilometers;
        return this;
    }

    public BuilderForBus setNumber(int number) {
        this.number = number;
        return this;
    }

    @Override
    public Bus build() {
        Validator<Bus> validator = new BusValidator();
        validator.validate(model, mileageInKilometers, number);
        return new Bus(this);
    }
}
