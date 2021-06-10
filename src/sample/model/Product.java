package sample.model;

public class Product {
    private Integer id;
    private String title;
    private String description;
    private String picture;
    private String ingredients;

    public Product(Integer id, String title, String description, String picture, String ingredients) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getIngredients() {
        return ingredients;
    }
}
