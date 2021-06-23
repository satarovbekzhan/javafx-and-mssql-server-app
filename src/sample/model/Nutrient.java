package sample.model;

public class Nutrient {
    private Integer id;
    private String name;

    public Nutrient(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Nutrient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
