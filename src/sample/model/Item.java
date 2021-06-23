package sample.model;

public class Item {
    private Integer id;
    private Order order;
    private Product product;
    private Integer amount;

    public Item(Integer id, Order order, Product product, Integer amount) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", order=" + order +
                ", product=" + product +
                ", amount=" + amount +
                '}';
    }
}
