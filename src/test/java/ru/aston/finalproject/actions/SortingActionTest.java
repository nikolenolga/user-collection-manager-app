package ru.aston.finalproject.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.service.sorting.Sorter;
import ru.aston.finalproject.service.sorting.StrangeSorter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class SortingActionTest {

    private UserSortingAction action;
    private AppData appDataMock;
    private AppRequest requestMock;
    private List<User> unsortedUsers;
    private List<User> sortedByBasic;
    private List<User> sortedByStrange;
    private Validate<User.Builder> validate;

    @BeforeEach
    void setUp() {
        action = new UserSortingAction();
        appDataMock = mock(AppData.class);
        requestMock = mock(AppRequest.class);
        validate = new UserBuilderValidator();

        unsortedUsers = Arrays.asList(
                User.builder().setName("Bob").setEmail("bob@example.com").setAge(30).build(validate),
                User.builder().setName("Alice").setEmail("alice@example.com").setAge(25).build(validate),
                User.builder().setName("Charlie").setEmail("charlie@example.com").setAge(20).build(validate)
        );
        sortedByBasic = Arrays.asList(
                User.builder().setName("Alice").setEmail("alice@example.com").setAge(25).build(validate),
                User.builder().setName("Bob").setEmail("bob@example.com").setAge(30).build(validate),
                User.builder().setName("Charlie").setEmail("charlie@example.com").setAge(20).build(validate)
        );
        sortedByStrange = Arrays.asList(
                User.builder().setName("Charlie").setEmail("charlie@example.com").setAge(20).build(validate),
                User.builder().setName("Alice").setEmail("alice@example.com").setAge(25).build(validate),
                User.builder().setName("Bob").setEmail("bob@example.com").setAge(30).build(validate)
        );
    }

    @Nested
    @DisplayName("Basic sorting")
    class BasicSorting {

        @Test
        void shouldSortByDefaultWhenParameterIsBasic() throws AppException {
            when(appDataMock.getEntityList()).thenReturn(unsortedUsers);
            when(requestMock.containsParameter("-basic")).thenReturn(true);
            when(requestMock.containsParameter("-strange")).thenReturn(false);

            var basicSorter = mock(Sorter.class);
            when(basicSorter.sort(any(List.class))).thenReturn(sortedByBasic);
            when(appDataMock.getSorter()).thenReturn(basicSorter);

            action.action(appDataMock, requestMock);

            verify(appDataMock).setEntityList(sortedByBasic);
        }
    }

    @Nested
    @DisplayName("Strange sorting")
    class StrangeSorting {

        @Test
        void shouldSortByAgeWhenParameterIsStrange() throws AppException {
            when(appDataMock.getEntityList()).thenReturn(unsortedUsers);
            when(requestMock.containsParameter("-basic")).thenReturn(false);
            when(requestMock.containsParameter("-strange")).thenReturn(true);

            var strangeSorter = mock(StrangeSorter.class);
            when(strangeSorter.sort(any(List.class), any(Function.class))).thenReturn(sortedByStrange);
            when(appDataMock.getStrangeSorter()).thenReturn(strangeSorter);

            action.action(appDataMock, requestMock);

            verify(appDataMock).setEntityList(sortedByStrange);
        }
    }

    @Nested
    @DisplayName("Error cases")
    class ErrorCases {

        @Test
        void shouldThrowWhenUserListIsEmpty() {
            when(appDataMock.getEntityList()).thenReturn(null);
            when(requestMock.containsParameter(any())).thenReturn(true);

            AppException exception = assertThrows(AppException.class, () -> action.action(appDataMock, requestMock));

            assertTrue(exception.getMessage().toLowerCase().contains("list not loaded"));
        }

        @Test
        void shouldThrowWhenCommandParameterIsWrong() {
            when(requestMock.containsParameter(any())).thenReturn(false);

            AppException exception = assertThrows(AppException.class, () -> action.action(appDataMock, requestMock));

            assertTrue(exception.getMessage().toLowerCase().contains("wrong"));
        }

        @Test
        void shouldThrowWhenNoValidParameterProvided() {
            when(appDataMock.getEntityList()).thenReturn(unsortedUsers);
            when(requestMock.containsParameter("-basic")).thenReturn(false);
            when(requestMock.containsParameter("-strange")).thenReturn(false);

            AppException exception = assertThrows(AppException.class, () -> action.action(appDataMock, requestMock));

            assertTrue(exception.getMessage().toLowerCase().contains("wrong request parameter"));
        }

        @Test
        void shouldCheckParametersAmount() throws AppException {
            doNothing().when(requestMock).checkParametersAmount(1);

            when(appDataMock.getEntityList()).thenReturn(unsortedUsers);
            when(requestMock.containsParameter("-basic")).thenReturn(true);
            when(requestMock.containsParameter("-strange")).thenReturn(false);

            var basicSorter = mock(Sorter.class);
            when(basicSorter.sort(any(List.class))).thenReturn(sortedByBasic);
            when(appDataMock.getSorter()).thenReturn(basicSorter);

            action.action(appDataMock, requestMock);

            verify(requestMock).checkParametersAmount(1);
        }
    }

    @Nested
    @DisplayName("Interaction with sorters")
    class SorterInteractions {

        @Test
        void shouldPassCorrectListToBasicSorter() throws AppException {
            when(appDataMock.getEntityList()).thenReturn(unsortedUsers);
            when(requestMock.containsParameter("-basic")).thenReturn(true);
            when(requestMock.containsParameter("-strange")).thenReturn(false);

            var basicSorter = mock(Sorter.class);
            when(basicSorter.sort(any(List.class))).thenReturn(sortedByBasic);
            when(appDataMock.getSorter()).thenReturn(basicSorter);

            action.action(appDataMock, requestMock);

            ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
            verify(basicSorter).sort(captor.capture());
            assertEquals(unsortedUsers, captor.getValue());
        }

        @Test
        void shouldPassCorrectListAndKeyExtractorToStrangeSorter() throws AppException {
            when(appDataMock.getEntityList()).thenReturn(unsortedUsers);
            when(requestMock.containsParameter("-basic")).thenReturn(false);
            when(requestMock.containsParameter("-strange")).thenReturn(true);

            var strangeSorter = mock(StrangeSorter.class);
            when(strangeSorter.sort(any(List.class), any(Function.class))).thenReturn(sortedByStrange);
            when(appDataMock.getStrangeSorter()).thenReturn(strangeSorter);

            action.action(appDataMock, requestMock);

            ArgumentCaptor<List<User>> listCaptor = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<Function<User, Integer>> funcCaptor =
                    ArgumentCaptor.forClass(Function.class);

            verify(strangeSorter).sort(listCaptor.capture(), funcCaptor.capture());

            assertEquals(unsortedUsers, listCaptor.getValue());

            User testUser = User.builder()
                    .setName("Test")
                    .setEmail("t@e.com")
                    .setAge(99)
                    .build(validate);
            assertEquals(99, funcCaptor.getValue().apply(testUser));
        }
    }
}