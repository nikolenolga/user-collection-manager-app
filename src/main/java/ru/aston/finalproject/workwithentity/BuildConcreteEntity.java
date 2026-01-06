package ru.aston.finalproject.workwithentity;

public class BuildConcreteEntity {

    public Bus buildBus(String model, String mileageInKilometers, int number) {
        return Bus.builder().
                setField("String", "model", model).
                setField("String","mileageInKilometers", mileageInKilometers).
                setField("int", "number", number).build();
    }

    public User buildUser(String name, String email, int age) {
        return User.builder().setName(name).setEmail(email).setAge(age).build();
    }
}
