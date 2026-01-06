package ru.aston.finalproject.workwithentity;

public class BuildConcreteEntity {

    public Bus buildBus(String model, String mileageInKilometers, int number) {
        return Bus.builder().
                setField("model", model).
                setField("mileageInKilometers", mileageInKilometers).
                setField("number", number).build();
    }

    public User buildUser(String name, String email, int age) {
        return User.builder().
                setField("name", name).
                setField("email", email).
                setField("age", age).build();
    }
}
