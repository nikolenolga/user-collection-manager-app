package ru.aston.finalproject.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.entity.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.aston.finalproject.util.ConstantFields.MAX_AGE;
import static ru.aston.finalproject.util.ConstantFields.MIN_AGE;

public class UserParserTest {

    private Parsing<User> userParser;
    private Validate<User.Builder> validate;

    @BeforeEach
    void setUp() {
        validate = new UserBuilderValidator();
        userParser = new UserParser(validate);
    }

    @Nested
    class ParseToStringTests {

        @Test
        void givenValidUser_whenParseToString_thenReturnFormattedString() {
            User user = User.builder()
                    .setName("Anna")
                    .setEmail("anna@example.com")
                    .setAge(30)
                    .build(validate);
            String result = userParser.parseToString(user);
            assertEquals("Anna : anna@example.com : 30", result);
        }

        @Test
        void givenUserWithMinimumAge_whenParseToString_thenReturnStringWithMinimumAge() {
            User user = User.builder()
                    .setName("Test")
                    .setEmail("test@example.com")
                    .setAge(MIN_AGE)
                    .build(validate);
            String result = userParser.parseToString(user);
            assertEquals("Test : test@example.com : 1", result);
        }

        @Test
        void givenUserWithMaximumAge_whenParseToString_thenReturnStringWithMaximumAge() {
            User user = User.builder()
                    .setName("Test")
                    .setEmail("test@example.com")
                    .setAge(MAX_AGE)
                    .build(validate);
            String result = userParser.parseToString(user);
            assertEquals("Test : test@example.com : 120", result);
        }

        @Test
        void givenUserWithCompoundName_whenParseToString_thenReturnStringWithFullName() {
            User user = User.builder()
                    .setName("Anna Maria")
                    .setEmail("anna@example.com")
                    .setAge(30)
                    .build(validate);
            String result = userParser.parseToString(user);
            assertEquals("Anna Maria : anna@example.com : 30", result);
        }
    }

    @Nested
    class ParseWithDefaultDelimiterTests {

        @Test
        void givenValidStringWithDefaultDelimiter_whenParse_thenReturnUserWithCorrectData() {
            String input = "Ivan : ivan@example.com : 25";
            User result = userParser.parse(input);
            assertNotNull(result);
            assertEquals("Ivan", result.getName());
            assertEquals("ivan@example.com", result.getEmail());
            assertEquals(25, result.getAge());
        }

        @Test
        void givenStringWithMixedCaseData_whenParse_thenReturnUserWithNormalizedData() {
            String input = "iVaN : IVAN@EXAMPLE.COM : 25";
            User result = userParser.parse(input);
            assertNotNull(result);
            assertEquals("Ivan", result.getName());
            assertEquals("ivan@example.com", result.getEmail());
            assertEquals(25, result.getAge());
        }

        @Test
        void givenStringWithSpacesAroundDelimiter_whenParse_thenReturnUserIgnoringSpaces() {
            String input = "Ivan   :   ivan@example.com   :   25";
            User result = userParser.parse(input);
            assertNotNull(result);
            assertEquals("Ivan", result.getName());
            assertEquals("ivan@example.com", result.getEmail());
            assertEquals(25, result.getAge());
        }

        @Test
        void givenStringWithCompoundName_whenParse_thenReturnUserWithFullName() {
            String input = "Anna Maria : anna@example.com : 30";
            User result = userParser.parse(input);
            assertNotNull(result);
            assertEquals("Anna Maria", result.getName());
            assertEquals("anna@example.com", result.getEmail());
            assertEquals(30, result.getAge());
        }
    }

    @Nested
    class ParseWithCustomDelimiterTests {

        @Test
        void givenStringWithSlashDelimiter_whenParseWithCustomDelimiter_thenReturnCorrectUser() {
            String input = "Ivan/ivan@example.com/25";
            String customDelimiter = "/";
            User result = userParser.parse(input, customDelimiter);
            assertNotNull(result);
            assertEquals("Ivan", result.getName());
            assertEquals("ivan@example.com", result.getEmail());
            assertEquals(25, result.getAge());
        }

        @Test
        void givenStringWithPipeDelimiter_whenParseWithCustomDelimiter_thenReturnCorrectUser() {
            String input = "Ivan|ivan@example.com|25";
            String customDelimiter = "\\|";
            User result = userParser.parse(input, customDelimiter);
            assertNotNull(result);
            assertEquals("Ivan", result.getName());
            assertEquals("ivan@example.com", result.getEmail());
            assertEquals(25, result.getAge());
        }

        @Test
        void givenStringWithCommaDelimiter_whenParseWithCustomDelimiter_thenReturnCorrectUser() {
            String input = "Ivan,ivan@example.com,25";
            String customDelimiter = ",";
            User result = userParser.parse(input, customDelimiter);
            assertNotNull(result);
            assertEquals("Ivan", result.getName());
            assertEquals("ivan@example.com", result.getEmail());
            assertEquals(25, result.getAge());
        }

        @Test
        void givenStringWithSemicolonDelimiterAndSpaces_whenParseWithCustomDelimiter_thenIgnoreSpaces() {
            String input = "Ivan ; ivan@example.com ; 25";
            String customDelimiter = ";";
            User result = userParser.parse(input, customDelimiter);
            assertNotNull(result);
            assertEquals("Ivan", result.getName());
            assertEquals("ivan@example.com", result.getEmail());
            assertEquals(25, result.getAge());
        }
    }

    @Nested
    class InvalidInputHandlingTests {

        @Test
        void givenNullString_whenParse_thenThrowException() {
            String input = null;
            Exception exception = assertThrows(AppException.class,
                    () -> userParser.parse(input)
            );
            assertTrue(exception.getMessage().contains("data in parser"));
        }

        @Test
        void givenEmptyString_whenParse_thenThrowException() {
            String input = "";
            Exception exception = assertThrows(AppException.class,
                    () -> userParser.parse(input)
            );
            assertTrue(exception.getMessage().contains("data in parser"));
        }

        @Test
        void givenStringWithOnlySpaces_whenParse_thenThrowException() {
            String input = "   ";
            Exception exception = assertThrows(AppException.class,
                    () -> userParser.parse(input)
            );
            assertTrue(exception.getMessage().contains("data in parser"));
        }

        @Test
        void givenStringWithWrongNumberOfComponents_whenParse_thenThrowException() {
            String[] invalidInputs =
                    {"Ivan : ivan@example.com", "Ivan : 25", "Ivan", "Ivan : ivan@example.com : 25 : extra"};
            for (String input : invalidInputs) {
                assertThrows(AppException.class,
                        () -> userParser.parse(input)
                );
            }
        }

        @Test
        void givenStringWithNonNumericAge_whenParse_thenThrowException() {
            String input = "Ivan : ivan@example.com : twenty";
            assertThrows(AppException.class,
                    () -> userParser.parse(input)
            );
        }

        @Test
        void givenStringWithNegativeAge_whenParse_thenThrowExceptionDuringUserCreation() {
            String input = "Ivan : ivan@example.com : -5";
            assertThrows(Exception.class,
                    () -> userParser.parse(input)
            );
        }

        @Test
        void givenStringWithInvalidEmail_whenParse_thenThrowExceptionDuringUserCreation() {
            String input = "Ivan : invalid-email : 25";
            assertThrows(Exception.class,
                    () -> userParser.parse(input)
            );
        }
    }

    @Nested
    class IdempotencyTests {

        @Test
        void givenUser_whenParseToStringAndParseBack_thenReturnEquivalentUser() {
            User originalUser = User.builder()
                    .setName("Ivan")
                    .setEmail("ivan@example.com")
                    .setAge(25)
                    .build(validate);
            String serialized = userParser.parseToString(originalUser);
            User deserialized = userParser.parse(serialized);
            assertEquals(originalUser.getName(), deserialized.getName());
            assertEquals(originalUser.getEmail(), deserialized.getEmail());
            assertEquals(originalUser.getAge(), deserialized.getAge());
        }

        @Test
        void givenString_whenParseAndParseToString_thenReturnNormalizedString() {
            String originalString = "iVaN : IVAN@EXAMPLE.COM : 25";
            User user = userParser.parse(originalString);
            String result = userParser.parseToString(user);
            assertEquals("Ivan : ivan@example.com : 25", result);
        }
    }

    @Nested
    class ConstantsUsageTests {

        @Test
        void givenStandardString_whenParseWithoutDelimiter_thenUsesConstantDelimiter() {
            String input = "Ivan : ivan@example.com : 25";
            User result = userParser.parse(input);
            assertNotNull(result);
            assertEquals("Ivan", result.getName());
        }

        @Test
        void givenString_whenParsing_thenArrayIndexesMatchConstants() {
            String input = "Name : email@example.com : 30";
            User result = userParser.parse(input);
            assertNotNull(result);
        }
    }

    @Nested
    class EdgeCasesTests {

        @Test
        void givenStringWithMinimumAge_whenParse_thenUserWithMinimumAgeCreated() {
            String input = "Test : test@example.com : 1";
            User result = userParser.parse(input);
            assertNotNull(result);
            assertEquals(1, result.getAge());
        }

        @Test
        void givenStringWithMaximumAge_whenParse_thenUserWithMaximumAgeCreated() {
            String input = "Test : test@example.com : 120";
            User result = userParser.parse(input);
            assertNotNull(result);
            assertEquals(120, result.getAge());
        }
    }
}