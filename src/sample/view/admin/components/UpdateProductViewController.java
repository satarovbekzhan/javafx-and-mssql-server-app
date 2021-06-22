package sample.view.admin.components;

import sample.model.Product;

public class UpdateProductViewController extends AdminComponentController {

    private Product product;

    public void setProduct(Product product) {
        this.product = product;
    }
}
