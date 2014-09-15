package org.jprogger.task.sample.service.local;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;

import org.jprogger.task.sample.R;
import org.jprogger.task.sample.model.Basket;
import org.jprogger.task.sample.model.Category;
import org.jprogger.task.sample.model.Product;
import org.jprogger.task.sample.service.MenuService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalMenuService implements MenuService {

    @Inject
    Context context;

    public static String loadResourceJson(Context context, int resource) throws IOException {
        InputStream is = context.getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        return writer.toString();
    }

    @Override
    public List<Category> getCategories() {
        if (context == null) {
            throw new IllegalStateException("Context can not be NULL");
        }
        try {
            GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Product.class, new ProductDeserializer());
            gsonBuilder.registerTypeAdapter(Category.class, new CategoryDeserializer());
            Gson gson = gsonBuilder.create();
            Menu menu = gson.fromJson(loadResourceJson(context, R.raw.menu), Menu.class);
            return Arrays.asList(menu.categories);
        } catch (IOException e) {
            Log.e("Menu Service", e.getMessage());
            return null;
        }
    }

    @Override
    public void syncMenuWithBasket(List<Category> categories, Basket basket) {
        for (Product product : basket.getProducts()) {
            for (Category category : categories) {
                for (Product pr : category.getProducts()) {
                    if (product.equals(pr)) {
                        pr.setAdded(true);
                        pr.setAddedQuantity(product.getAddedQuantity());
                    }
                }
            }
        }
    }

    public static class Menu {

        @SerializedName("categories")
        public Category[] categories;

        @SerializedName("products")
        public Product[] products;
    }

    private static class ProductDeserializer implements JsonDeserializer<Product> {

        private final ThreadLocal<Map<String, Product>> cache = new ThreadLocal<Map<String, Product>>() {
            @Override
            protected Map<String, Product> initialValue() {
                return new HashMap<String, Product>();
            }
        };

        @Override
        public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive()) {
                final JsonPrimitive primitive = json.getAsJsonPrimitive();
                return getOrCreate(primitive.getAsString());
            }
            if (json.isJsonObject()) {
                final JsonObject jsonObject = json.getAsJsonObject();

                final Product product = getOrCreate(jsonObject.get("id").getAsString());
                product.setName(jsonObject.get("name").getAsString());
                return product;
            }

            throw new JsonParseException("Unexpected JSON type: " + json.getClass().getSimpleName());
        }

        private Product getOrCreate(final String id) {
            Product product = cache.get().get(id);
            if (product == null) {
                product = new Product();
                product.setId(id);
                cache.get().put(id, product);
            }
            return product;
        }
    }

    private static class CategoryDeserializer implements JsonDeserializer<Category> {

        @Override
        public Category deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            final String id = jsonObject.get("id").getAsString();
            final String name = jsonObject.get("name").getAsString();

            Product[] products = context.deserialize(jsonObject.get("products"), Product[].class);

            final Category book = new Category();
            book.setId(id);
            book.setName(name);
            book.setProducts(Arrays.asList(products));
            return book;
        }
    }
}
