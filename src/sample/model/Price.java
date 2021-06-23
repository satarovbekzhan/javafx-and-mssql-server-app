package sample.model;

import java.math.BigDecimal;

public class Price {
    private Integer id;
    private Integer product;
    private BigDecimal value;
    private String starting;

    public Price(Integer id, Integer product, BigDecimal value, String starting) {
        this.id = id;
        this.product = product;
        this.value = value;
        this.starting = starting;
    }

    public Integer getId() {
        return id;
    }

    public Integer getProduct() {
        return product;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getStarting() {
        return starting;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", product=" + product +
                ", value=" + value +
                ", starting='" + starting + '\'' +
                '}';
    }
}
