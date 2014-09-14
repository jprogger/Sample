package org.jprogger.task.sample;

import com.google.inject.AbstractModule;

import org.jprogger.task.sample.service.BasketService;
import org.jprogger.task.sample.service.MenuService;
import org.jprogger.task.sample.service.local.BasketServiceImpl;
import org.jprogger.task.sample.service.local.LocalMenuService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MenuService.class).to(LocalMenuService.class);
        bind(BasketService.class).to(BasketServiceImpl.class);
        bind(EventBus.class).toInstance(EventBus.getDefault());
        bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
    }
}
