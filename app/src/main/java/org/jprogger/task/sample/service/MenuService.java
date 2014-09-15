package org.jprogger.task.sample.service;

import org.jprogger.task.sample.model.Basket;
import org.jprogger.task.sample.model.Category;

import java.util.List;

public interface MenuService {

    public List<Category> getCategories();

    public void syncMenuWithBasket(final List<Category> categories, final Basket basket);
}
