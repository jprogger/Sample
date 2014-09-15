package org.jprogger.task.sample.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.jprogger.task.sample.event.MenuChangedEvent;
import org.jprogger.task.sample.event.MenuLoadedEvent;
import org.jprogger.task.sample.model.Basket;
import org.jprogger.task.sample.model.Category;
import org.jprogger.task.sample.service.MenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.greenrobot.event.EventBus;

@Singleton
public class MenuController {

    private final Lock lock = new ReentrantLock();
    @Inject EventBus eventBus;
    @Inject MenuService service;
    @Inject ExecutorService executor;
    private List<Category> menuCategories = new ArrayList<Category>();

    public List<Category> getMenuCategories() {
        return menuCategories;
    }

    public void doLoadMenu() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean locked = lock.tryLock();
                    if (locked) {
                        List<Category> result = service.getCategories();
                        menuCategories = result != null ? result : new ArrayList<Category>();
                        eventBus.post(new MenuLoadedEvent());
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    public void syncWithBasket(final Basket basket) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean locked = lock.tryLock();
                    if (locked) {
                        service.syncMenuWithBasket(menuCategories, basket);
                        eventBus.post(new MenuChangedEvent());
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
    }
}
