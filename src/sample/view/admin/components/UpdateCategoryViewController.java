package sample.view.admin.components;

import sample.model.Category;

public class UpdateCategoryViewController extends AdminComponentController {

    private Category category;

    public void setCategory(Category category) {
        this.category = category;
    }
}
