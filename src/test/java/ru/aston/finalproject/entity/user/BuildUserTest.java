package ru.aston.finalproject.entity.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuildUserTest {

    private BuildUser buildUser;
    private User user;

    @BeforeEach
    public void setUp() {
        buildUser = new BuildUser();
    }

    @Nested
    class BuildUserCreationTests {

        @Test
        void givenValidData_whenCapitalizeNameAndNormalizedEmail_thenUserCreatedWithCorrectFields() {
            user = buildUser.capitalizeNameAndNormalizedEmail("Ivan", "email@mail.ru", 20);

            assertNotNull(user);
            assertEquals("Ivan", user.getName());
            assertEquals("email@mail.ru", user.getEmail());
            assertEquals(20, user.getAge());
        }
    }

    @Nested
    class NameCapitalizationTests {

        private String invalidName;

        @Test
        void givenNameInLowerCase_whenCapitalizeNameAndNormalizedEmail_thenNameCapitalized() {
            invalidName = "ivan";
            user = buildUser.capitalizeNameAndNormalizedEmail(invalidName, "email@mail.ru", 20);
            assertEquals("Ivan", user.getName());
        }


        @Test
        void givenNameInMixedCase_whenCapitalizeNameAndNormalizedEmail_thenNameNormalizedToCapitalized() {
            invalidName = "iVaN";
            user = buildUser.capitalizeNameAndNormalizedEmail(invalidName, "email@mail.ru", 20);
            assertEquals("Ivan", user.getName());
        }

        @Test
        void givenNameInUpperCase_whenCapitalizeNameAndNormalizedEmail_thenNameConvertedToCapitalized() {
            invalidName = "IVAN";
            user = buildUser.capitalizeNameAndNormalizedEmail(invalidName, "email@mail.ru", 20);
            assertEquals("Ivan", user.getName());
        }
    }

    @Nested
    class EmailNormalizationTests {

        private String invalidEmail;

        @Test
        public void givenEmailInUpperCase_whenCapitalizeNameAndNormalizedEmail_thenEmailConvertedToLowerCase() {
            invalidEmail = "EMAIL@MAIL.RU";
            user = buildUser.capitalizeNameAndNormalizedEmail("Ivan", invalidEmail, 20);
            assertEquals("email@mail.ru", user.getEmail());
        }

        @Test
        public void givenEmailInMixedCase_whenCapitalizeNameAndNormalizedEmail_thenEmailNormalizedToLowerCase() {
            invalidEmail = "EmAiL@mAiL.Ru";
            user = buildUser.capitalizeNameAndNormalizedEmail("Ivan", invalidEmail, 20);
            assertEquals("email@mail.ru", user.getEmail());
        }
    }

}