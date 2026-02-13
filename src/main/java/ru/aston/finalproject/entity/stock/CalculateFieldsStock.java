package ru.aston.finalproject.entity.stock;

import java.math.BigDecimal;

public class CalculateFieldsStock {

    private static final BigDecimal hundred = new BigDecimal("100");

    public static BigDecimal nowPercent(Stock stock) {
        BigDecimal result = stock.getMaxValue().subtract(stock.getMinValue());
        BigDecimal nowResult = stock.getNowValue().subtract(stock.getMinValue());
        return nowResult.multiply(hundred).divide(result, 2, java.math.RoundingMode.HALF_UP);
    }

    public static BigDecimal setGrahamPrice(Stock stock) {
        if (stock.getPe() == null || stock.getEps() == null || stock.getEpsFrom5Years() == null) {
            return null;
        }
        return calculateGrahamValueCounting(stock);
    }

    private static BigDecimal calculateGrahamValueCounting(Stock stock) {
        BigDecimal grahamConstant = new BigDecimal("8.5");
        BigDecimal riskFreeRate = new BigDecimal("0.12");
        BigDecimal oneFromFiveYears = new BigDecimal("0.2");
        BigDecimal one = BigDecimal.ONE;
        BigDecimal two = new BigDecimal("2");

        BigDecimal conservativeG = getConservativeG(stock, one, oneFromFiveYears);
        BigDecimal bracketValue = getBracketValue(grahamConstant, two, conservativeG);
        BigDecimal numerator = stock.getEps().multiply(bracketValue);
        BigDecimal denominator = one.add(riskFreeRate);
        return (numerator).divide(denominator, 2, java.math.RoundingMode.HALF_UP);
    }

    private static BigDecimal getConservativeG(Stock stock, BigDecimal one, BigDecimal oneFromFiveYears) {
        BigDecimal fifteen = new BigDecimal("15");
        BigDecimal growthFactor =
                (stock.getEpsFrom5Years().divide(hundred, 10, java.math.RoundingMode.HALF_UP))
                        .add(one);
        double compoundAnnualGrowthRateDouble = Math.pow(
                growthFactor.doubleValue(), oneFromFiveYears.doubleValue()) - 1;
        BigDecimal compoundAnnualGrowthRate = new BigDecimal(compoundAnnualGrowthRateDouble);
        BigDecimal compoundAnnualGrowthRatePercent = compoundAnnualGrowthRate.multiply(hundred);
        return compoundAnnualGrowthRatePercent.min(fifteen);
    }

    private static BigDecimal getBracketValue(BigDecimal grahamConstant, BigDecimal two, BigDecimal conservativeG) {
        BigDecimal multiply = two.multiply(conservativeG);
        return grahamConstant.add(multiply);
    }

}
