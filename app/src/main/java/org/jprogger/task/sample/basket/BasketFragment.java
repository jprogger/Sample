package org.jprogger.task.sample.basket;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;

import org.jprogger.task.sample.R;
import org.jprogger.task.sample.event.BasketChangedEvent;
import org.jprogger.task.sample.model.BasketItem;

import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.fragment.RoboFragment;

public class BasketFragment extends RoboFragment {

    ListView listView;
    @Inject
    EventBus eventBus;
    @Inject
    BasketController basketController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basket_fragment, null);
        listView = (ListView) rootView.findViewById(R.id.items_list);
        listView.setEmptyView(rootView.findViewById(R.id.basket_empty_view));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                basketController.increaseQuantity(((BasketItem) parent.getItemAtPosition(position)));
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.setAdapter(new BasketItemsAdapter(getActivity(),
                R.layout.basket_item_view, basketController.getBasket().getItems()));
    }

    @Override
    public void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.basket_menu, menu);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(BasketChangedEvent event) {
        listView.setAdapter(new BasketItemsAdapter(getActivity(),
                R.layout.basket_item_view, basketController.getBasket().getItems()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_items: {
                basketController.removeAll();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private static class BasketItemsAdapter extends ArrayAdapter<BasketItem> {

        private Context context;
        private List<BasketItem> items;

        public BasketItemsAdapter(Context context, int resource, List<BasketItem> items) {
            super(context, resource, items);
            this.items = items;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.basket_item_view, null);
            TextView itemView = (TextView) rowView.findViewById(R.id.basket_item_view);
            itemView.setText(items.get(position).getProduct().getName() + "(" + items.get(position).getQuantity() + ")");
            return rowView;
        }
    }
}
