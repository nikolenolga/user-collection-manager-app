package ru.aston.finalproject.actions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class PrintActionTest {
    @Mock
    private AppData appData;
    @Mock
    private AppRequest request;
    PrintAction printAction = new PrintAction();
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenRequestThrowsAppException_whenCallCheckParametersAmount_thenThrowsAppException() {
        doThrow(new AppException("Invalid parameters amount")).when(request).checkParametersAmount(0);
        assertThrows(AppException.class, () -> printAction.action(appData, request));
    }
}