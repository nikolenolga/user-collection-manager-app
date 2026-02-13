package ru.aston.finalproject.service.loader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.parser.UserParser;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsoleDataLoaderTest {
    @Mock
    private UserParser userParser;
    @Mock
    private AppRequest appRequest;
    @InjectMocks
    private ConsoleDataLoader<User> consoleDataLoader;
    private User user;
    private Stream<String> validConsoleInputStream;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .setAge(3)
                .setEmail("test@mail.ru")
                .setName("Name")
                .build(new UserBuilderValidator());
        validConsoleInputStream = Stream.generate(() -> "Name test@mail.ru 3");
    }

    @Test
    void givenValidConsoleInputAndExpectedSize_whenCallLoadEntityList_thenReturnStreamWithRequiredUserAmount() {
        int expected = 10;
        String consoleInput = validConsoleInputStream.limit(expected).collect(Collectors.joining("\n"));
        System.setIn(new ByteArrayInputStream(consoleInput.getBytes()));
        when(userParser.parse(any())).thenReturn(user);

        List<User> list = consoleDataLoader.loadEntityList(expected, appRequest).toList();

        assertEquals(expected, list.size());
    }

    @Test
    void givenConsoleInputContainsInvalidDataAndExpectedSize_whenCallLoadEntityList_thenReturnStreamWithRequiredUserAmount() {
        int expected = 10;
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("wrong input").add("wrong input");
        validConsoleInputStream.limit(expected).forEach(stringJoiner::add);
        System.setIn(new ByteArrayInputStream(stringJoiner.toString().getBytes()));
        when(userParser.parse(any())).thenReturn(user);

        List<User> list = consoleDataLoader.loadEntityList(expected, appRequest).toList();

        assertEquals(expected, list.size());
    }

    @Test
    void givenConsoleInputContainsConsoleExitCommandAfterExpectedValidUsersInput_whenCallLoadEntityList_thenReturnStreamWithExpectedUserAmountLessThenSize() {
        int size = 10;
        int expected = 5;
        StringJoiner stringJoiner = new StringJoiner("\n");
        List<String> validInputList = validConsoleInputStream.limit(expected).toList();
        validInputList.forEach(stringJoiner::add);
        stringJoiner.add(ConsoleDataLoader.STOP_CONSOLE_LOADER_COMMAND);
        validInputList.forEach(stringJoiner::add);
        System.setIn(new ByteArrayInputStream(stringJoiner.toString().getBytes()));
        when(userParser.parse(any())).thenReturn(user);

        List<User> list = consoleDataLoader.loadEntityList(size, appRequest).toList();

        assertEquals(expected, list.size());
    }
}