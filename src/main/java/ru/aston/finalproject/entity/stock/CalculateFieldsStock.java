package ru.aston.finalproject.entity.stock;

import java.math.BigDecimal;

public class CalculateFieldsStock {

    private static final BigDecimal hundredPercent = new BigDecimal("100");

    public static BigDecimal nowPercent(Stock stock) {
        BigDecimal result = stock.getMaxValue().subtract(stock.getMinValue());
        BigDecimal nowResult = stock.getNowValue().subtract(stock.getMinValue());
        return nowResult.multiply(hundredPercent).divide(result, 2, java.math.RoundingMode.HALF_UP);
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

        BigDecimal conservativeGrowthRate = getConservativeGrowthRate(stock, one, oneFromFiveYears);
        BigDecimal bracketValue = getBracketValue(grahamConstant, two, conservativeGrowthRate);
        BigDecimal numerator = stock.getEps().multiply(bracketValue);
        BigDecimal denominator = one.add(riskFreeRate);
        return (numerator).divide(denominator, 2, java.math.RoundingMode.HALF_UP);
    }

    private static BigDecimal getConservativeGrowthRate(
            Stock stock, BigDecimal one, BigDecimal oneFromFiveYears) {

        BigDecimal maximumAllowedValue = new BigDecimal("15");

        BigDecimal growthFactor =
                stock.getEps().divide(stock.getEpsFrom5Years(), 10, java.math.RoundingMode.HALF_UP);

        double compoundAnnualGrowthRateDouble = Math.pow(
                growthFactor.doubleValue(), oneFromFiveYears.doubleValue()) - one.doubleValue();

        if (compoundAnnualGrowthRateDouble > 0) {
            BigDecimal compoundAnnualGrowthRate = new BigDecimal(compoundAnnualGrowthRateDouble);
            BigDecimal compoundAnnualGrowthRatePercent = compoundAnnualGrowthRate.multiply(hundredPercent);
            return compoundAnnualGrowthRatePercent.min(maximumAllowedValue);
        } else {
            return new BigDecimal(0);
        }
    }

    private static BigDecimal getBracketValue(
            BigDecimal grahamConstant, BigDecimal two, BigDecimal conservativeGrowthRate) {

        BigDecimal multiply = two.multiply(conservativeGrowthRate);
        return grahamConstant.add(multiply);
    }

}
