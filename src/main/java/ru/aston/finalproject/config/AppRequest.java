package ru.aston.finalproject.config;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ru.aston.finalproject.util.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class AppRequest {
    private static final Integer COMMAND_NAME_INDEX = 0;
    private static final Integer REQUEST_NON_PARAMETER_PARTS_AMOUNT = 1;
    private static final Integer PARAMETER_PARTS_AMOUNT = 2;
    private static final Integer PARAMETER_KEY_INDEX = 0;
    private static final Integer PARAMETER_VALUE_INDEX = 1;
    private static final String PARAMETERS_SPLITERATOR = "=";
    private static final String REQUEST_PARTS_SPLITERATOR = "\s+";
    private static final Set<String> exitWords = Set.of(
            "exit",
            "^Z"
    );
    private final Map<String, String> parameters = new HashMap<>();
    private String commandName;

    private AppRequest() {
    }

    public static AppRequest createRequest(String requestLine) {
        if (StringUtils.isBlank(requestLine)) {
            throw new AppException(Message.EXCEPTION_EMPTY_REQUEST);
        }

        String[] requestParts = requestLine.trim().split(REQUEST_PARTS_SPLITERATOR);
        AppRequest request = new AppRequest();
        request.commandName = requestParts[COMMAND_NAME_INDEX];

        if (requestParts.length > REQUEST_NON_PARAMETER_PARTS_AMOUNT) {
            String[] parameters = Arrays.copyOfRange(requestParts, REQUEST_NON_PARAMETER_PARTS_AMOUNT,
                    requestParts.length);
            setRequestParameters(parameters, request);
        }
        return request;
    }

    private static void setRequestParameters(String[] requestParts, AppRequest request) {
        for (String part : requestParts) {
            String[] parameterParts = part.trim().split(PARAMETERS_SPLITERATOR);
            if (parameterParts.length > PARAMETER_PARTS_AMOUNT) {
                throw new AppException(Message.EXCEPTION_WRONG_REQUEST_PARAMETER_SYNTAXES_X.formatted(part));
            }

            String parameterKey = parameterParts[PARAMETER_KEY_INDEX];
            String parameterValue = (parameterParts.length == PARAMETER_PARTS_AMOUNT)
                    ? parameterParts[PARAMETER_VALUE_INDEX]
                    : null;
            request.parameters.put(parameterKey, parameterValue);
        }
    }

    public String getParameter(String parameterKey) {
        if (!parameters.containsKey(parameterKey)) {
            throw new AppException(Message.EXCEPTION_ENTER_PARAMETER_X.formatted(parameterKey));
        }

        return parameters.get(parameterKey);
    }

    public String getStringParameter(String parameterKey) {
        String parameterValue = getParameter(parameterKey);
        if (StringUtils.isBlank(parameterValue)) {
            throw new AppException(Message.EXCEPTION_ENTER_PARAMETER_VALUE_X.formatted(parameterKey));
        }

        return parameterValue;
    }

    public Integer getIntegerParameter(String parameterKey) {
        String parameterValue = getStringParameter(parameterKey);
        try {
            return Integer.parseInt(parameterValue);
        } catch (NumberFormatException e) {
            throw new AppException(Message.EXCEPTION_WRONG_PARAMETER_VALUE_X.formatted(parameterKey, parameterValue));
        }
    }

    public boolean containsParameter(String parameterKey) {
        return parameters.containsKey(parameterKey);
    }

    public void checkParametersAmount(Integer expectedParametersAmount) {
        if (parameters.size() > expectedParametersAmount){
            throw new AppException(Message.EXCEPTION_WRONG_PARAMETERS_AMOUNT);
        }
    }

    public boolean isExitRequest() {
        return exitWords.contains(commandName);
    }
}
