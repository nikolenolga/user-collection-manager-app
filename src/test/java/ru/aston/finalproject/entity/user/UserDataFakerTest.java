package ru.aston.finalproject.entity.user;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserDataFakerTest {
    private final static UserDataFaker userDataFaker = new UserDataFaker();

    static Stream<String> RandomNameSource() {
        return Stream.generate(userDataFaker::getRandomUserName)
                .limit(10);
    }

    static Stream<String> RandomEmailSource() {
        return Stream.generate(userDataFaker::getRandomUserEmail)
                .limit(10);
    }

    static Stream<Integer> RandomAgeSource() {
        return Stream.generate(userDataFaker::getRandomUserAge)
                .limit(10);
    }

    @ParameterizedTest
    @MethodSource("RandomNameSource")
    void givenUserDataFaker_whenGetRandomName_thenGetValidUserName(String name) {
        User.Builder builder = User.builder();
        assertDoesNotThrow(() -> builder.setName(name));
    }

    @ParameterizedTest
    @MethodSource("RandomEmailSource")
    void givenUserDataFaker_whenGetRandomEmail_thenGetValidUserEmail(String email) {
        User.Builder builder = User.builder();
        assertDoesNotThrow(() -> builder.setEmail(email));
    }

    @ParameterizedTest
    @MethodSource("RandomAgeSource")
    void givenUserDataFaker_whenGetRandomAge_thenGetValidUserAge(Integer age) {
        User.Builder builder = User.builder();
        assertDoesNotThrow(() -> builder.setAge(age));
    }
}