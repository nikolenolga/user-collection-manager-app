package ru.aston.finalproject.service.loader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.entity.user.BuildUser;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.entity.user.UserDataFaker;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomUserDataLoaderTest {
    @Mock
    private UserDataFaker userDataFaker;
    @Mock
    private AppRequest appRequest;
    @Mock
    private BuildUser buildUser;
    @InjectMocks
    private RandomUserDataLoader randomUserDataLoader;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .setAge(3)
                .setEmail("test-email@gmail.com")
                .setName("Name")
                .build(new UserBuilderValidator());
    }

    @Test
    void givenValidSizeParameter_whenCallLoadEntityListAndCollect_thenVerifyBuildUserCapitalizeNameAndNormalizedEmailMethodCalledEqualSizeParameterTimes() {
        int size = 10;

        randomUserDataLoader.loadEntityList(size, appRequest).toList();

        verify(buildUser, times(size)).capitalizeNameAndNormalizedEmail(any(), any(), anyInt());
    }

    @Test
    void givenUserDataFakerReturningAgeNameAndEmail_whenCallLoadEntityListAndCollect_thenVerifyBuildUserCapitalizeNameAndNormalizedEmailMethodCalledWithSameParameters() {
        int size = 1;
        int age = 3;
        String name = "Test";
        String email = "test@gmail.com";
        when(userDataFaker.getRandomUserName()).thenReturn(name);
        when(userDataFaker.getRandomUserEmail()).thenReturn(email);
        when(userDataFaker.getRandomUserAge()).thenReturn(age);

        randomUserDataLoader.loadEntityList(size, appRequest).toList();

        verify(buildUser).capitalizeNameAndNormalizedEmail(name, email, age);
    }

    @Test
    void givenValidSizeParameter_whenCallLoadEntityListAndCollect_thenVerifyAllUserDataFakerMethodsCalledEqualSizeParameterTimes() {
        int size = 7;

        randomUserDataLoader.loadEntityList(size, appRequest).toList();

        verify(userDataFaker, times(size)).getRandomUserName();
        verify(userDataFaker, times(size)).getRandomUserEmail();
        verify(userDataFaker, times(size)).getRandomUserAge();
    }

    @Test
    void givenSizeParameter_whenCallLoadEntityList_thenReturnUserStreamWithRequiredUsersAmount() {
        Integer expected = 10;
        when(buildUser.capitalizeNameAndNormalizedEmail(any(), any(), anyInt())).thenReturn(user);

        List<User> users = randomUserDataLoader.loadEntityList(expected, appRequest).toList();

        assertEquals(expected, users.size());
    }

    @Test
    void givenMockedBuilderReturningUser_whenCallLoadEntityList_thenReturnUserStreamContainsEqualUsers() {
        Integer size = 10;

        when(buildUser.capitalizeNameAndNormalizedEmail(any(), any(), anyInt())).thenReturn(user);

        randomUserDataLoader.loadEntityList(size, appRequest)
                .forEach(actual -> assertEquals(user, actual));
    }

    @Test
    void givenZeroSizeParameter_whenCallLoadEntityList_thenReturnEmptyUserStream() {
        Integer size = 0;

        List<User> users = randomUserDataLoader.loadEntityList(size, appRequest).toList();

        assertTrue(users.isEmpty());
    }

    @Test
    void givenNullSizeParameter_whenCallLoadEntityList_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> randomUserDataLoader.loadEntityList(null, appRequest));
    }
}