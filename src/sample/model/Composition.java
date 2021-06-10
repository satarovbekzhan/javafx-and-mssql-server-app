package sample.model;

import java.math.BigDecimal;

public class Composition {
    private Integer id;
    private String nutrient;
    private String unit;
    private BigDecimal pro_100;
    private BigDecimal pro_por;

    public Composition(Integer id, String nutrient, String unit, BigDecimal pro_100, BigDecimal pro_por) {
        this.id = id;
        this.nutrient = nutrient;
        this.unit = unit;
        this.pro_100 = pro_100;
        this.pro_por = pro_por;
    }

    public Integer getId() {
        return id;
    }

    public String getNutrient() {
        return nutrient;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getPro_100() {
        return pro_100;
    }

    public BigDecimal getPro_por() {
        return pro_por;
    }
}
