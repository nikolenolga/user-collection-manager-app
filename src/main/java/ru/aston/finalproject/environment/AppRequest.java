package ru.aston.finalproject.environment;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ru.aston.finalproject.util.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class AppRequest {
    private static final String PARAMETERS_SPLITERATOR = "=";
    private static final String REQUEST_PARTS_SPLITERATOR = "\s+";
    private static final String CHANGE_ENTITY = "change";
    private static final Set<String> exitWords = Set.of("exit", "^Z");
    private final Map<String, String> parameters = new HashMap<>();
    private String commandName;

    private AppRequest() {
    }

    public static AppRequest createRequest(String requestLine) {
        if (StringUtils.isBlank(requestLine)) {
            throw new AppException(Message.EMPTY_REQUEST);
        }

        String[] requestParts = requestLine.trim().split(REQUEST_PARTS_SPLITERATOR);
        AppRequest request = new AppRequest();
        request.commandName = requestParts[0];

        if (requestParts.length > 1) {
            String[] parameters = Arrays.copyOfRange(requestParts, 1, requestParts.length);
            setRequestParameters(parameters, request);
        }
        return request;
    }

    private static void setRequestParameters(String[] requestParts, AppRequest request) {
        for (String part : requestParts) {
            String[] parameterParts = part.trim().split(PARAMETERS_SPLITERATOR);
            if (parameterParts.length > 2) {
                throw new AppException(Message.WRONG_REQUEST_PARAMETER_SYNTAXES_X.formatted(part));
            }

            String parameterKey = parameterParts[0];
            String parameterValue = (parameterParts.length == 2)
                    ? parameterParts[1]
                    : null;
            request.parameters.put(parameterKey, parameterValue);
        }
    }

    public String getParameter(String parameterKey) {
        if (!parameters.containsKey(parameterKey)) {
            throw new AppException(Message.ENTER_PARAMETER_X.formatted(parameterKey));
        }

        return parameters.get(parameterKey);
    }

    public String getStringParameter(String parameterKey) {
        String parameterValue = getParameter(parameterKey);
        if (StringUtils.isBlank(parameterValue)) {
            throw new AppException(Message.ENTER_PARAMETER_VALUE_X.formatted(parameterKey));
        }

        return parameterValue;
    }

    public Integer getIntegerParameter(String parameterKey) {
        String parameterValue = getStringParameter(parameterKey);

        try {
            return Integer.parseInt(parameterValue);
        } catch (NumberFormatException e) {
            throw new AppException(Message.WRONG_PARAMETER_VALUE_X.formatted(parameterKey, parameterValue));
        }
    }

    public Double getDoubleParameter(String parameterKey) {
        String parameterValue = getStringParameter(parameterKey);

        try {
            return Double.parseDouble(parameterValue);
        } catch (NumberFormatException e) {
            throw new AppException(Message.WRONG_PARAMETER_VALUE_X.formatted(parameterKey, parameterValue));
        }
    }

    public boolean containsParameter(String parameterKey) {
        return parameters.containsKey(parameterKey);
    }

    public void checkParametersAmount(Integer expectedParametersAmount) {
        if (parameters.size() > expectedParametersAmount) {
            throw new AppException(Message.WRONG_PARAMETERS_AMOUNT);
        }
    }

    public int getSizeParameters() {
        return parameters.size();
    }

    public boolean isExitRequest() {
        return exitWords.contains(commandName);
    }

    public boolean isChangeEntityRequest() {
        return CHANGE_ENTITY.equals(commandName);
    }
}
