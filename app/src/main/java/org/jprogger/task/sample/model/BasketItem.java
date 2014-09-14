package org.jprogger.task.sample.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BasketItem {

    private int quantity;
    private Product product;
    private Category category;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BasketItem) {
            final BasketItem other = (BasketItem) o;
            return new EqualsBuilder()
                    .append(product, other.product)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(product)
                .toHashCode();
    }

    public static class Builder {

        private BasketItem item = new BasketItem();

        public Builder() {
        }

        public Builder withQuantity(int quantity) {
            item.setQuantity(quantity);
            return this;
        }

        public Builder withProduct(Product product) {
            item.setProduct(product);
            return this;
        }

        public Builder withCategory(Category category) {
            item.setCategory(category);
            return this;
        }

        public BasketItem build() {
            return item;
        }
    }
}
