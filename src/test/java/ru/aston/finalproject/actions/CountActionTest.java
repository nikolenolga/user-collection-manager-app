package ru.aston.finalproject.actions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.aston.finalproject.appEnviroment.AppData;
import ru.aston.finalproject.appEnviroment.AppException;
import ru.aston.finalproject.appEnviroment.AppRequest;

public class CountActionTest {

    private AutoCloseable mockitoClosable;
    private static AppException appException;
    private static AppAction action;


    @Mock
    private AppData mockAppData;

    @Mock
    private AppRequest mockAppRequest;

    @BeforeAll
    static void init() {
        appException = new AppException("Any message");
        action = new CountAction();
    }

    @BeforeEach
    void prep() {
        mockitoClosable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tear() throws Exception{
        mockitoClosable.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 3, 4})
    void givenIncorrectThreadParameter_whenCountAction_thenAppException(int parameterCount) {
        Mockito.doThrow(appException).when(mockAppRequest).checkParametersAmount(
                Mockito.eq(parameterCount)
        );

        Assertions.assertThrowsExactly(
                AppException.class,
                () -> action.action(mockAppData, mockAppRequest)
        );
    }

    @Test
    void givenNoThreadParameter_whenCountAction_thenAppException() {
        Mockito.doNothing().when(mockAppRequest).checkParametersAmount(Mockito.eq(1));
        Mockito.doThrow(appException).when(mockAppRequest)
                .getIntegerParameter(Mockito.eq("-threads"));

        Assertions.assertThrowsExactly(
                AppException.class,
                () -> action.action(mockAppData, mockAppRequest)
        );
    }

}
