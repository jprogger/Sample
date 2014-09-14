package org.jprogger.task.sample.model;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Singleton
public class Basket {

    private Set<BasketItem> itemSet = new HashSet<BasketItem>();

    public List<BasketItem> getItems() {
        return new ArrayList<BasketItem>(itemSet);
    }

    public void removeItems() {
        itemSet.clear();
    }

    public void updateItem(BasketItem basketItem) {
        if (itemSet.contains(basketItem)) {
            for (Iterator<BasketItem> iterator = itemSet.iterator(); iterator.hasNext(); ) {
                BasketItem item = iterator.next();
                if (item.equals(basketItem)) {
                    item.setQuantity(item.getQuantity() + 1);
                    return;
                }
            }
        }
    }

    public void addItem(BasketItem basketItem) {
        itemSet.add(basketItem);
    }
}
