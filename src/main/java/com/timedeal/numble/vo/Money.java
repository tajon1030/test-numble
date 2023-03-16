package com.timedeal.numble.vo;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@NoArgsConstructor
public class Money {

    private BigDecimal value;

    public Money(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getPercentOff(Money salePrice) {
        return this.value.subtract(salePrice.value)
                .multiply(BigDecimal.valueOf(100))
                .divide(this.value, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
