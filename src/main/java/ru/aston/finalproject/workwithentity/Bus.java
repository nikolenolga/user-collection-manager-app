package ru.aston.finalproject.workwithentity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Bus implements Comparable<Bus> {

    private final String model;
    private final String mileageInKilometers;
    private final int number;

    Bus(BuilderForBus builder){
        this.model = builder.getModel();
        this.mileageInKilometers = builder.getMileageInKilometers();
        this.number = builder.getNumber();
    }

    public static BuilderForBus builder() {
        return new BuilderForBus();
    }

    @Override
    public int compareTo(Bus o) {
        if (!this.model.equals(o.model)) {
            return this.model.compareTo(o.model);
        } else if (!this.mileageInKilometers.equals(o.mileageInKilometers)) {
            return this.mileageInKilometers.compareTo(o.mileageInKilometers);
        } else {
            return Integer.compare(this.number, o.number);
        }
    }

    @Override
    public String toString() {
        return String.format
                ("Bus{model: %s, mileageInKilometers: %s, number: %d}",
                        model, mileageInKilometers, number);
    }
}
