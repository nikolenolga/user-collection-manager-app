package ru.aston.finalproject.service;

import net.datafaker.Faker;
import ru.aston.finalproject.workwithentity.Builder;
import ru.aston.finalproject.workwithentity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.aston.finalproject.constants.ConstantFields.MAX_AGE;
import static ru.aston.finalproject.constants.ConstantFields.MIN_AGE;

public class RandomUserDataLoader implements DataLoader<User> {

    private final Faker faker = new Faker();

    public RandomUserDataLoader() {

    }

    @Override
    public List<User> loadEntityList(Integer size) {
        return Stream.generate(User::builder)
                .map(builder -> builder.setValueForField("name", faker.name().firstName()))
                .map(builder -> builder.setValueForField("email", faker.internet().emailAddress()))
                .map(builder -> builder.setValueForField("age", faker.number().numberBetween(MIN_AGE, MAX_AGE)))
                .map(Builder::build)
                .limit(size)
                .collect(Collectors.toList());
    }
}
