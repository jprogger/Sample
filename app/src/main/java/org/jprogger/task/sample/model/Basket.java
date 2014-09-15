package org.jprogger.task.sample.model;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Singleton
public class Basket {

    private Set<Product> itemSet = new HashSet<Product>();

    public List<Product> getProducts() {
        return new ArrayList<Product>(itemSet);
    }

    public void removeProducts() {
        itemSet.clear();
    }

    public void updateProduct(Product product) {
        if (itemSet.contains(product)) {
            for (Iterator<Product> iterator = itemSet.iterator(); iterator.hasNext(); ) {
                Product nextProduct = iterator.next();
                if (nextProduct.equals(product)) {
                    nextProduct.setAddedQuantity(nextProduct.getAddedQuantity() + 1);
                    return;
                }
            }
        }
    }

    public void addProduct(Product product) {
        if (itemSet.add(product)) {
            product.setAdded(true);
            product.setAddedQuantity(1);
        };
    }
}
