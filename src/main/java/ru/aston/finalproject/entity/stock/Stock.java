package ru.aston.finalproject.entity.stock;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.aston.finalproject.entity.validator.StockBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;

import java.math.BigDecimal;

import static ru.aston.finalproject.entity.stock.CalculateFieldsStock.nowPercent;
import static ru.aston.finalproject.entity.stock.CalculateFieldsStock.setGrahamPrice;

@Getter
@EqualsAndHashCode
public class Stock implements Comparable<Stock> {

    private final String name;
    private final BigDecimal nowValue;
    private final BigDecimal maxValue;
    private final BigDecimal minValue;
    private final BigDecimal pe;
    private final BigDecimal eps;
    private final BigDecimal epsFrom5Years;
    private final BigDecimal nowValueInPercent;
    private final BigDecimal grahamPrice;
    private final boolean dividends;
    private final boolean buyInThisPeriod;

    private Stock(Builder builder) {
        this.name = builder.name;
        this.nowValue = builder.nowValue;
        this.maxValue = builder.maxValue;
        this.minValue = builder.minValue;
        this.pe = builder.pe;
        this.eps = builder.eps;
        this.epsFrom5Years = builder.epsFrom5Years;
        this.nowValueInPercent = nowPercent(this);
        this.grahamPrice = setGrahamPrice(this);
        this.dividends = builder.dividends;
        this.buyInThisPeriod = builder.buyInThisPeriod;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return name + "\n"
                + "   PE   =   " + pe + "\n"
                + " 52 w High  = " + maxValue + "\n"
                + "  --> Nov   = " + nowValue + " (" + nowValueInPercent + "%)" + "\n"
                + "grahamPrice = " + grahamPrice + "\n"
                + "  minValue  = " + minValue + "\n";

    }

    @Override
    public int compareTo(Stock o) {
        return this.nowValueInPercent.compareTo(o.nowValueInPercent);
    }

    public boolean isFloorBorder(double value) {
        return pe.compareTo(BigDecimal.valueOf(value)) > 0;
    }

    public boolean isCeilingBorder(double value) {
        return pe.compareTo(BigDecimal.valueOf(value)) < 0;
    }

    public boolean isThisValue(double value) {
        return pe.compareTo(BigDecimal.valueOf(value)) == 0;
    }

    @Getter
    public static class Builder {

        private String name;
        private BigDecimal nowValue;
        private BigDecimal maxValue;
        private BigDecimal minValue;
        private BigDecimal pe;
        private BigDecimal eps;
        private BigDecimal epsFrom5Years;
        private boolean dividends;
        private boolean buyInThisPeriod;

        private Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setNowValue(String nowValue) {
            this.nowValue = new BigDecimal(nowValue);
            return this;
        }

        public Builder setMaxValue(String maxValue) {
            this.maxValue = new BigDecimal(maxValue);
            return this;
        }

        public Builder setMinValue(String minValue) {
            this.minValue = new BigDecimal(minValue);
            return this;
        }

        public Builder setPe(String pe) {
            this.pe = new BigDecimal(pe);
            return this;
        }

        public Builder setEps(String eps) {
            this.eps = new BigDecimal(eps);
            return this;
        }

        public Builder setEpsFrom5Years(String epsFrom5Years) {
            this.epsFrom5Years = new BigDecimal(epsFrom5Years);
            return this;
        }

        public Builder setDividends(String dividends) {
            this.dividends = Boolean.parseBoolean(dividends);
            return this;
        }

        public Builder setBuyInThisPeriod(String buyInThisPeriod) {
            this.buyInThisPeriod = Boolean.parseBoolean(buyInThisPeriod);
            return this;
        }

        public Stock build(Validate<Builder> validator) {
            validator.validate(this);
            return new Stock(this);
        }
    }
}
