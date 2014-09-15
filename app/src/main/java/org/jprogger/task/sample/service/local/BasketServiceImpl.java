package org.jprogger.task.sample.service.local;

import org.jprogger.task.sample.model.Basket;
import org.jprogger.task.sample.model.Product;
import org.jprogger.task.sample.service.BasketService;

public class BasketServiceImpl implements BasketService {

    @Override
    public void cleanBasket(final Basket basket) {
        basket.removeProducts();
    }

    @Override
    public void increaseQuantity(Basket basket, Product product) {
        basket.updateProduct(product);
    }

    @Override
    public void putToBasket(final Basket basket, final Product product) {
        basket.addProduct(product);
    }
}
