package ru.aston.finalproject.entity.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.AppException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.aston.finalproject.util.ConstantFields.MAX_AGE;
import static ru.aston.finalproject.util.ConstantFields.MIN_AGE;

public class UserTest {

    private Validate<User.Builder> validate;

    @BeforeEach
    void setUp() {
        validate = new UserBuilderValidator();
    }

    @Nested
    class UserCreationTests {

        @Test
        void givenValidData_whenBuildUser_thenUserCreatedSuccessfully() {
            User user = User.builder()
                    .setName("Ivan")
                    .setEmail("email@mail.ru")
                    .setAge(20)
                    .build(validate);

            assertNotNull(user);
            assertEquals("Ivan", user.getName());
            assertEquals("email@mail.ru", user.getEmail());
            assertEquals(20, user.getAge());
        }
    }

    @Nested
    class NameValidationTests {

        @Test
        void givenNullName_whenBuildUser_thenThrowException() {
            AppException exception = assertThrows(AppException.class, () ->
                    User.builder()
                            .setEmail("email@mail.ru")
                            .setAge(20)
                            .build(validate)
            );
            assertTrue(exception.getMessage().contains("cannot be empty"));
        }

        @Test
        void givenEmptyName_whenBuildUser_thenThrowException() {
            AppException exception = assertThrows(AppException.class, () ->
                    User.builder()
                            .setName("")
                            .setEmail("email@mail.ru")
                            .setAge(20)
                            .build(validate)
            );
            assertTrue(exception.getMessage().contains("cannot be empty"));
        }

        @Test
        void givenInvalidNameWithDigitsOrSymbols_whenBuildUser_thenThrowException() {
            String invalidName = "I1v5a4n1";
            AppException exception = assertThrows(AppException.class, () ->
                    User.builder()
                            .setName(invalidName)
                            .setEmail("email@mail.ru")
                            .setAge(20)
                            .build(validate)
            );
            assertTrue(exception.getMessage().contains("not a valid Name"));
        }
    }

    @Nested
    class EmailValidationTests {

        @Test
        void givenNullEmail_whenBuildUser_thenThrowException() {
            AppException exception = assertThrows(AppException.class, () ->
                    User.builder()
                            .setName("Ivan")
                            .setAge(20)
                            .build(validate)
            );
            assertTrue(exception.getMessage().contains("cannot be empty"));
        }

        @Test
        void givenInvalidEmailFormat_whenBuildUser_thenThrowException() {
            String invalidEmail = "invalid";
            AppException exception = assertThrows(AppException.class, () ->
                    User.builder()
                            .setName("Ivan")
                            .setEmail(invalidEmail)
                            .setAge(20)
                            .build(validate)
            );
            assertTrue(exception.getMessage().contains("not a valid"));
        }
    }

    @Nested
    class AgeValidationTests {

        @Test
        void givenAgeBelowMinimum_whenBuildUser_thenThrowException() {
            int invalidAge = MIN_AGE - 1;
            AppException exception = assertThrows(AppException.class, () ->
                    User.builder()
                            .setName("Ivan")
                            .setEmail("email@mail.ru")
                            .setAge(invalidAge)
                            .build(validate)
            );
            assertTrue(exception.getMessage().contains("should be between"));
            assertTrue(exception.getMessage().contains(String.valueOf(invalidAge)));
        }

        @Test
        void givenAgeAboveMaximum_whenBuildUser_thenThrowException() {
            int invalidAge = MAX_AGE + 1;
            AppException exception = assertThrows(AppException.class, () ->
                    User.builder()
                            .setName("Ivan")
                            .setEmail("email@mail.ru")
                            .setAge(invalidAge)
                            .build(validate)
            );
            assertTrue(exception.getMessage().contains("should be between"));
            assertTrue(exception.getMessage().contains(String.valueOf(invalidAge)));
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void givenUserWithData_whenToString_thenReturnCorrectString() {
            User user = User.builder()
                    .setName("Ivan")
                    .setEmail("email@mail.ru")
                    .setAge(20)
                    .build(validate);
            String result = user.toString();
            assertTrue(result.contains("Ivan"));
            assertTrue(result.contains("email@mail.ru"));
            assertTrue(result.contains("20"));
        }
    }

    @Nested
    class ComparableTests {

        @Test
        void givenTwoUsersWithDifferentNames_whenSort_thenSortedByName() {
            User ivan = User.builder().setName("Ivan").setEmail("a@mail.ru").setAge(25).build(validate);
            User anna = User.builder().setName("Anna").setEmail("b@mail.ru").setAge(30).build(validate);
            List<User> users = Arrays.asList(ivan, anna);
            Collections.sort(users);
            assertEquals("Anna", users.get(0).getName());
            assertEquals("Ivan", users.get(1).getName());
        }

        @Test
        void givenTwoUsersWithSameNameDifferentEmails_whenSort_thenSortedByEmail() {
            User user1 = User.builder().setName("Ivan").setEmail("b@mail.ru").setAge(25).build(validate);
            User user2 = User.builder().setName("Ivan").setEmail("a@mail.ru").setAge(30).build(validate);
            List<User> users = Arrays.asList(user1, user2);
            Collections.sort(users);
            assertEquals("a@mail.ru", users.get(0).getEmail());
            assertEquals("b@mail.ru", users.get(1).getEmail());
        }

        @Test
        void givenTwoUsersWithSameNameAndEmailDifferentAges_whenSort_thenSortedByAge() {
            User user1 = User.builder().setName("Ivan").setEmail("a@mail.ru").setAge(30).build(validate);
            User user2 = User.builder().setName("Ivan").setEmail("a@mail.ru").setAge(25).build(validate);
            List<User> users = Arrays.asList(user1, user2);
            Collections.sort(users);
            assertEquals(25, users.get(0).getAge());
            assertEquals(30, users.get(1).getAge());
        }
    }
}
