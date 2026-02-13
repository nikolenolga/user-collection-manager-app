package ru.aston.finalproject.actions;

import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.util.Message;

import java.util.function.Predicate;

public class RemovePartsOfStockAction extends AppAction<Stock> {
    private static final Integer EXPECTED_MAX_PARAMETERS_AMOUNT = 2;
    private static final String VALUE_PARAMETER = "-value";
    private static final String FLOOR_VALUE_PARAMETER = "-floor";
    private static final String CEILING_VALUE_PARAMETER = "-ceiling";

    @Override
    public void action(AppData<Stock> appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_MAX_PARAMETERS_AMOUNT);

        Predicate<Stock> filterPredicate = setFilterPredicate(request);
        appData.getFilter().filter(filterPredicate);

        appData.setEntityList(appData.getFilter().getList());
        System.out.println(Message.ENTITIES_FILTERED);
    }

    private Predicate<Stock> setFilterPredicate(AppRequest request) throws AppException {
        int sizeParameters = request.getSizeParameters();
        if (sizeParameters == 1) {
            return setSingleParameterPredicate(request);
        } else if (sizeParameters == 2) {
            return setTwoParametersPredicate(request);
        }
        throw new AppException(Message.WRONG_REQUEST_PARAMETER_SYNTAXES);
    }

    private Predicate<Stock> setSingleParameterPredicate(AppRequest request) {
        if (request.containsParameter(VALUE_PARAMETER)) {
            Double value = request.getDoubleParameter(VALUE_PARAMETER);
            return stock -> stock.isThisValue(value);
        }
        if (request.containsParameter(FLOOR_VALUE_PARAMETER)) {
            Double floorValue = request.getDoubleParameter(FLOOR_VALUE_PARAMETER);
            return stock -> stock.isFloorBorder(floorValue);
        }
        if (request.containsParameter(CEILING_VALUE_PARAMETER)) {
            Double ceilingValue = request.getDoubleParameter(CEILING_VALUE_PARAMETER);
            return stock -> stock.isCeilingBorder(ceilingValue);
        }
        throw new AppException(Message.WRONG_REQUEST_PARAMETER_SYNTAXES);
    }

    private Predicate<Stock> setTwoParametersPredicate(AppRequest request) {

        if (!request.containsParameter(FLOOR_VALUE_PARAMETER)
                || !request.containsParameter(CEILING_VALUE_PARAMETER)) {
            throw new AppException(Message.WRONG_REQUEST_PARAMETER_SYNTAXES);
        }

        Double floorValue = request.getDoubleParameter(FLOOR_VALUE_PARAMETER);
        Double ceilingValue = request.getDoubleParameter(CEILING_VALUE_PARAMETER);

        return stock -> stock.isFloorBorder(floorValue) && stock.isCeilingBorder(ceilingValue);
    }
}
