package ru.aston.finalproject.service.loader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.parser.UserParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileDataLoaderTest {
    @TempDir
    private Path tempDir;
    private Path testFilePath;
    @Mock
    private UserParser userParser;
    @Mock
    private AppRequest appRequest;
    @InjectMocks
    private FileDataLoader<User> fileDataLoader;
    private User user;
    private Stream<String> validConsoleInputStream;

    @BeforeEach
    void setUp() throws IOException {
        user = User.builder()
                .setAge(3)
                .setEmail("test@mail.ru")
                .setName("Name")
                .build(new UserBuilderValidator());
        testFilePath = tempDir.resolve("test.txt");
        validConsoleInputStream = Stream.generate(() -> "Name : test@mail.ru : 3");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    @Test
    void givenFileWithValidDataAndExpectedSize_whenCallLoadEntityList_thenReturnUserStreamWithExpectedUsersAmount() throws IOException {
        int expected = 10;
        createFile(expected);
        when(appRequest.getStringParameter(any())).thenReturn(testFilePath.toString());
        when(userParser.parse(any())).thenReturn(user);
        List<User> list = fileDataLoader.loadEntityList(expected, appRequest).toList();

        assertEquals(expected, list.size());
    }

    @Test
    void givenFileWithValidDataWithUsersAmountLessThenExpected_whenCallLoadEntityList_thenReturnUserStreamWithLessUsersAmount() throws IOException {
        int size = 10;
        int expected = 5;
        createFile(expected);
        when(appRequest.getStringParameter(any())).thenReturn(testFilePath.toString());
        when(userParser.parse(any())).thenReturn(user);
        List<User> list = fileDataLoader.loadEntityList(size, appRequest).toList();

        assertEquals(expected, list.size());
    }

    @Test
    void givenNotValidFilePathParameter_whenCallLoadEntityList_thenTrowAppException() throws IOException {
        int expected = 10;
        createFile(expected);
        when(appRequest.getStringParameter(any())).thenReturn("not valid path");

        assertThrows(AppException.class, () -> fileDataLoader.loadEntityList(expected, appRequest).toList());
    }

    private void createFile(int expected) throws IOException {
        String collected = validConsoleInputStream.limit(expected).collect(Collectors.joining("\n"));
        Files.writeString(testFilePath, collected, CREATE, WRITE);
    }
}