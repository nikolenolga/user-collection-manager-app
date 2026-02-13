package ru.aston.finalproject.actions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.aston.finalproject.collection.CustomArrayList;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.service.writer.FileWriter;
import ru.aston.finalproject.util.Message;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WriteActionTest {

    private WriteAction writeAction;
    private CustomArrayList<User> userList;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private AutoCloseable mockitoCloseable;
    private Validate<User.Builder> validate;

    @Mock
    private AppData mockAppData;

    @Mock
    private AppRequest mockAppRequest;

    @Mock
    private FileWriter<User> mockFileWriter;

    @BeforeEach
    void setUp() {
        mockitoCloseable = MockitoAnnotations.openMocks(this);
        writeAction = new WriteAction<>();
        userList = new CustomArrayList<>();
        outputStream = new ByteArrayOutputStream();
        validate = new UserBuilderValidator();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void givenValidParametersAndNonEmptyUserList_whenAction_thenWriteUsersToFileAndPrintSuccessMessage() throws AppException {
        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));
        userList.add(User.builder().setName("Anna").setEmail("anna@mail.ru").setAge(30).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output.txt");

        writeAction.action(mockAppData, mockAppRequest);

        verify(mockAppRequest).checkParametersAmount(1);
        verify(mockAppRequest).getStringParameter("-file");
        verify(mockFileWriter).write(userList, "output.txt");

        String output = outputStream.toString().trim();
        assertTrue(output.contains(Message.ENTITIES_SAVED));
        assertFalse(output.contains(Message.LIST_NOT_LOADED));
    }

    @Test
    void givenValidParametersAndEmptyUserList_whenAction_thenPrintWarningMessageAndStillWriteToFile() throws AppException {
        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output.txt");

        writeAction.action(mockAppData, mockAppRequest);

        verify(mockAppRequest).checkParametersAmount(1);
        verify(mockAppRequest).getStringParameter("-file");
        verify(mockFileWriter).write(userList, "output.txt");

        String output = outputStream.toString().trim();
        assertTrue(output.contains(Message.LIST_NOT_LOADED));
        assertTrue(output.contains(Message.ENTITIES_SAVED));
    }

    @Test
    void givenValidParametersAndNullUserList_whenAction_thenPrintWarningMessageAndStillWriteToFile() throws AppException {
        when(mockAppData.getEntityList()).thenReturn(null);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output.txt");

        writeAction.action(mockAppData, mockAppRequest);

        verify(mockAppRequest).checkParametersAmount(1);
        verify(mockAppRequest).getStringParameter("-file");
        verify(mockFileWriter).write(null, "output.txt");

        String output = outputStream.toString().trim();
        assertTrue(output.contains(Message.LIST_NOT_LOADED));
        assertTrue(output.contains(Message.ENTITIES_SAVED));
    }

    @Test
    void givenInvalidParametersAmount_whenAction_thenThrowAppException() throws AppException {
        doThrow(new AppException("Invalid parameters amount"))
                .when(mockAppRequest).checkParametersAmount(1);

        AppException exception = assertThrows(AppException.class,
                () -> writeAction.action(mockAppData, mockAppRequest));

        assertEquals("Invalid parameters amount", exception.getMessage());
        verify(mockAppRequest).checkParametersAmount(1);
        verify(mockAppRequest, never()).getStringParameter(anyString());
        verify(mockAppData, never()).getEntityList();
        verify(mockAppData, never()).getFileWriter();
        verify(mockFileWriter, never()).write(any(), anyString());
    }

    @Test
    void givenValidParametersAndFileWriterThrowsException_whenAction_thenExceptionPropagated() throws AppException {
        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output.txt");

        doThrow(new AppException("File write error"))
                .when(mockFileWriter).write(userList, "output.txt");

        AppException exception = assertThrows(AppException.class,
                () -> writeAction.action(mockAppData, mockAppRequest));

        assertEquals("File write error", exception.getMessage());
        verify(mockAppRequest).checkParametersAmount(1);
        verify(mockAppRequest).getStringParameter("-file");
        verify(mockFileWriter).write(userList, "output.txt");

        String output = outputStream.toString().trim();
        assertFalse(output.contains(Message.ENTITIES_SAVED));
        assertFalse(output.contains(Message.LIST_NOT_LOADED));
    }

    @Test
    void givenValidParametersAndEmptyFilePath_whenAction_thenFileWriterReceivesEmptyPath() throws AppException {
        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("");

        writeAction.action(mockAppData, mockAppRequest);

        verify(mockFileWriter).write(userList, "");

        String output = outputStream.toString().trim();
        assertTrue(output.contains(Message.ENTITIES_SAVED));
    }

    @Test
    void givenValidParametersAndNullFilePath_whenAction_thenFileWriterReceivesNullPath() throws AppException {
        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn(null);

        writeAction.action(mockAppData, mockAppRequest);

        verify(mockFileWriter).write(userList, null);

        String output = outputStream.toString().trim();
        assertTrue(output.contains(Message.ENTITIES_SAVED));
    }

    @Test
    void givenValidParametersAndSingleUserInList_whenAction_thenWriteCalledWithSingleUser() throws AppException {
        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output.txt");

        writeAction.action(mockAppData, mockAppRequest);

        verify(mockFileWriter).write(userList, "output.txt");
        assertEquals(1, userList.size());

        String output = outputStream.toString().trim();
        assertTrue(output.contains(Message.ENTITIES_SAVED));
        assertFalse(output.contains(Message.LIST_NOT_LOADED));
    }

    @Test
    void givenValidParametersAndMultipleCalls_whenActionCalledMultipleTimes_thenFileWriterCalledMultipleTimes() throws AppException {
        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));
        userList.add(User.builder().setName("Anna").setEmail("anna@mail.ru").setAge(30).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output.txt");

        writeAction.action(mockAppData, mockAppRequest);
        writeAction.action(mockAppData, mockAppRequest);
        writeAction.action(mockAppData, mockAppRequest);

        verify(mockAppRequest, times(3)).checkParametersAmount(1);
        verify(mockAppRequest, times(3)).getStringParameter("-file");
        verify(mockFileWriter, times(3)).write(userList, "output.txt");
    }

    @Test
    void givenValidParameters_whenAction_thenCorrectParameterNameUsed() throws AppException {
        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output.txt");

        writeAction.action(mockAppData, mockAppRequest);

        verify(mockAppRequest).getStringParameter("-file");
        verify(mockAppRequest, never()).getStringParameter("file");
        verify(mockAppRequest, never()).getStringParameter("-output");
    }

    @Test
    void givenWriteActionInstance_whenMultipleInstancesCreated_thenEachInstanceWorksIndependently() throws AppException {
        WriteAction writeAction1 = new WriteAction();
        WriteAction writeAction2 = new WriteAction();

        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output1.txt", "output2.txt");

        writeAction1.action(mockAppData, mockAppRequest);
        writeAction2.action(mockAppData, mockAppRequest);

        verify(mockFileWriter).write(userList, "output1.txt");
        verify(mockFileWriter).write(userList, "output2.txt");
    }

    @Test
    void givenValidParametersAndFileWriterReturns_whenAction_thenSuccessMessagePrinted() throws AppException {
        userList.add(User.builder().setName("Ivan").setEmail("ivan@mail.ru").setAge(25).build(validate));

        when(mockAppData.getEntityList()).thenReturn(userList);
        when(mockAppData.getFileWriter()).thenReturn(mockFileWriter);
        when(mockAppRequest.getStringParameter("-file")).thenReturn("output.txt");

        writeAction.action(mockAppData, mockAppRequest);

        String output = outputStream.toString().trim();
        assertTrue(output.endsWith(Message.ENTITIES_SAVED));
    }

    @AfterEach
    void tearDown() throws Exception {
        System.setOut(originalOut);
        if (mockitoCloseable != null) {
            mockitoCloseable.close();
        }
    }
}
