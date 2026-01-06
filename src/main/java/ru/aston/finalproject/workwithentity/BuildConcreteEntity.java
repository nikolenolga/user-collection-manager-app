package ru.aston.finalproject.workwithentity;

public class BuildConcreteEntity {

    public Bus buildBus(String model, String mileageInKilometers, int number) {
        return Bus.builder().
                setValueForField("model", model).
                setValueForField("mileageInKilometers", mileageInKilometers).
                setValueForField("number", number).build();
    }

    public User buildUser(String name, String email, int age) {
        return User.builder().
                setValueForField("name", name).
                setValueForField("email", email).
                setValueForField("age", age).build();
    }
}
