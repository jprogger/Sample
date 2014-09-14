package org.jprogger.task.sample;

import android.support.v4.app.FragmentTransaction;

import com.google.inject.Inject;

import org.jprogger.task.sample.basket.BasketFragment;
import org.jprogger.task.sample.event.MenuLoadedEvent;
import org.jprogger.task.sample.menu.MenuController;
import org.jprogger.task.sample.menu.ProductListFragment;

import de.greenrobot.event.EventBus;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.main)
public class MainActivity extends RoboFragmentActivity {

    @Inject
    EventBus eventBus;
    @Inject
    MenuController menuController;

    BasketFragment basketFragment;
    ProgressFragment progressFragment;
    ProductListFragment productsFragment;

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleFragments(true);
        menuController.doLoadMenu();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(MenuLoadedEvent event) {
        toggleFragments(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    private void toggleFragments(boolean loading) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (loading) {
            removeContentFragments(ft);
            recreateProgressFragment(ft);
            ft.commit();
            return;
        }
        removeProgressFragment(ft);
        recreateContentFragments(ft);
        ft.commit();
    }

    private void removeContentFragments(FragmentTransaction ft) {
        if (productsFragment != null && productsFragment.isAdded()) {
            ft.remove(productsFragment);
        }
        if (basketFragment != null && basketFragment.isAdded()) {
            ft.remove(basketFragment);
        }
    }

    private void recreateContentFragments(FragmentTransaction ft) {
        productsFragment = new ProductListFragment();
        ft.replace(R.id.menu_container, productsFragment);
        basketFragment = new BasketFragment();
        ft.replace(R.id.basket_container, basketFragment);
    }

    private void removeProgressFragment(FragmentTransaction ft) {
        if (progressFragment != null && progressFragment.isAdded()) {
            ft.remove(progressFragment);
        }
    }

    private void recreateProgressFragment(FragmentTransaction ft) {
        progressFragment = new ProgressFragment();
        ft.replace(R.id.progress_container, progressFragment);
    }
}
