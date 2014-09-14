package org.jprogger.task.sample.service.local;

import org.jprogger.task.sample.model.Basket;
import org.jprogger.task.sample.model.BasketItem;
import org.jprogger.task.sample.model.Category;
import org.jprogger.task.sample.model.Product;
import org.jprogger.task.sample.service.BasketService;

public class BasketServiceImpl implements BasketService {

    @Override
    public void cleanBasket(final Basket basket) {
        basket.removeItems();
    }

    @Override
    public void increaseQuantity(Basket basket, BasketItem item) {
        basket.updateItem(item);
    }

    @Override
    public void putToBasket(final Basket basket, final Product product, final Category category) {
        basket.addItem(new BasketItem.Builder().withQuantity(1).withProduct(product).withCategory(category).build());
    }
}
