package org.jprogger.task.sample.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.inject.Inject;

import org.jprogger.task.sample.R;
import org.jprogger.task.sample.basket.BasketController;
import org.jprogger.task.sample.event.BasketChangedEvent;
import org.jprogger.task.sample.model.Category;
import org.jprogger.task.sample.model.Product;

import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.fragment.RoboFragment;

public class ProductListFragment extends RoboFragment {

    ExpandableListView listView;
    @Inject
    EventBus eventBus;
    @Inject
    MenuController menuController;
    @Inject
    BasketController basketController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.products_fragment, null);
        listView = (ExpandableListView) rootView.findViewById(R.id.list_view);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                ExpandableListAdapter adapter = parent.getExpandableListAdapter();
                Category category = (Category) adapter.getGroup(groupPosition);
                Product product = (Product) adapter.getChild(groupPosition, childPosition);
                basketController.putToBasket(product, category);
                return true;
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
        listView.setAdapter(new MenuAdapter(getActivity(), menuController.getMenuCategories()));
    }

    @Override
    public void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(BasketChangedEvent event) {
        ((MenuAdapter) listView.getExpandableListAdapter()).notifyDataSetChanged();
    }

    private static class MenuAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<Category> categories;

        public MenuAdapter(Context context, List<Category> categories) {
            this.context = context;
            this.categories = categories;
        }

        @Override
        public int getGroupCount() {
            return categories.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return categories.get(groupPosition).getProducts().size();
        }

        @Override
        public Category getGroup(int groupPosition) {
            return categories.get(groupPosition);
        }

        @Override
        public Product getChild(int groupPosition, int childPosition) {
            return categories.get(groupPosition).getProducts().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return categories.get(groupPosition).getId().hashCode();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return categories.get(groupPosition).getProducts().get(childPosition).getId().hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.group_list_item_view, null);
            TextView categoryView = (TextView) rowView.findViewById(R.id.category_name);
            categoryView.setText(categories.get(groupPosition).getName());
            return rowView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.child_list_item_view, null);
            TextView productView = (TextView) rowView.findViewById(R.id.product_name);
            productView.setText(categories.get(groupPosition).getProducts().get(childPosition).getName());
            return rowView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
