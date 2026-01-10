package ru.aston.finalproject.service.loader;

import net.datafaker.Faker;
import ru.aston.finalproject.config.AppRequest;
import ru.aston.finalproject.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.aston.finalproject.util.ConstantFields.MAX_AGE;
import static ru.aston.finalproject.util.ConstantFields.MIN_AGE;

public class RandomUserDataLoader implements DataLoader<User> {
    private final Faker dataFaker;

    public RandomUserDataLoader(Faker dataFaker) {
        this.dataFaker = dataFaker;
    }

    @Override
    public List<User> loadEntityList(Integer size, AppRequest request) {

        return Stream.generate(User::builder)
                .map(builder -> builder.setName(dataFaker.name().firstName()))
                .map(builder -> builder.setEmail(dataFaker.internet().emailAddress()))
                .map(builder -> builder.setAge(dataFaker.number().numberBetween(MIN_AGE, MAX_AGE + 1)))
                .map(User.Builder::build)
                .limit(size)
                .collect(Collectors.toList());
    }
}
