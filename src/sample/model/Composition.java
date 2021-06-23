package sample.model;

import java.math.BigDecimal;

public class Composition {
    private Integer id;
    private Nutrient nutrient;
    private Unit unit;
    private BigDecimal pro_100;
    private BigDecimal pro_por;

    public Composition(Integer id, Nutrient nutrient, Unit unit, BigDecimal pro_100, BigDecimal pro_por) {
        this.id = id;
        this.nutrient = nutrient;
        this.unit = unit;
        this.pro_100 = pro_100;
        this.pro_por = pro_por;
    }

    public Integer getId() {
        return id;
    }

    public Nutrient getNutrient() {
        return nutrient;
    }

    public Unit getUnit() {
        return unit;
    }

    public BigDecimal getPro_100() {
        return pro_100;
    }

    public BigDecimal getPro_por() {
        return pro_por;
    }

    @Override
    public String toString() {
        return "Composition{" +
                "id=" + id +
                ", nutrient=" + nutrient +
                ", unit=" + unit +
                ", pro_100=" + pro_100 +
                ", pro_por=" + pro_por +
                '}';
    }
}
