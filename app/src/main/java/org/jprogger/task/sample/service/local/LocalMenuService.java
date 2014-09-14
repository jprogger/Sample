package org.jprogger.task.sample.service.local;

import org.jprogger.task.sample.model.Category;
import org.jprogger.task.sample.model.Product;
import org.jprogger.task.sample.service.MenuService;

import java.util.ArrayList;
import java.util.List;

public class LocalMenuService implements MenuService {


    @Override
    public List<Category> getCategories() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Product product1 = new Product();
        product1.setId("productA");
        product1.setName("Product A");
        product1.setPrice(100);

        Product product2 = new Product();
        product2.setId("productB");
        product2.setName("Product B");
        product2.setPrice(200);

        Category category = new Category();
        category.setId("categoryA");
        category.setName("Category A");
        category.getProducts().add(product2);
        category.getProducts().add(product1);

        Category category1 = new Category();
        category1.setId("categoryB");
        category1.setName("Category B");
        category1.getProducts().add(product2);
        category1.getProducts().add(product1);

        Category category2 = new Category();
        category2.setId("categoryC");
        category2.setName("Category C");
        category2.getProducts().add(product1);
        category2.getProducts().add(product2);

        ArrayList<Category> categories = new ArrayList<Category>();
        categories.add(category);
        categories.add(category1);
        categories.add(category2);

        return categories;
    }
}
