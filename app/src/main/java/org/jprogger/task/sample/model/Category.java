package org.jprogger.task.sample.model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Category {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("products")
    private List<Product> products = new ArrayList<Product>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Category) {
            final Category other = (Category) o;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(name, other.name)
                    .append(products, other.products)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(products)
                .toHashCode();
    }
}
