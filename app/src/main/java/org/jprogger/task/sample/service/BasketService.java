package org.jprogger.task.sample.service;

import org.jprogger.task.sample.model.Basket;
import org.jprogger.task.sample.model.BasketItem;
import org.jprogger.task.sample.model.Category;
import org.jprogger.task.sample.model.Product;

public interface BasketService {

    void cleanBasket(final Basket basket);

    void increaseQuantity(final Basket basket, BasketItem item);

    void putToBasket(final Basket basket, final Product product, final Category category);
}
