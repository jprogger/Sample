package org.jprogger.task.sample.basket;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.jprogger.task.sample.event.BasketChangedEvent;
import org.jprogger.task.sample.model.Basket;
import org.jprogger.task.sample.model.Product;
import org.jprogger.task.sample.service.BasketService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.greenrobot.event.EventBus;

@Singleton
public class BasketController {

    private final Lock lock = new ReentrantLock();
    @Inject Basket basket;
    @Inject EventBus eventBus;
    @Inject ExecutorService executor;
    @Inject BasketService basketService;

    public Basket getBasket() {
        return basket;
    }

    public void putToBasket(final Product product) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean locked = lock.tryLock();
                    if (locked) {
                        basketService.putToBasket(basket, product);
                        eventBus.post(new BasketChangedEvent());
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    public void increaseQuantity(final Product product) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean locked = lock.tryLock();
                    if (locked) {
                        basketService.increaseQuantity(basket, product);
                        eventBus.post(new BasketChangedEvent());
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    public void removeAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean locked = lock.tryLock();
                    if (locked) {
                        basketService.cleanBasket(basket);
                        eventBus.post(new BasketChangedEvent());
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
    }
}
