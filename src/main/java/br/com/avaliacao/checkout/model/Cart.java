package br.com.avaliacao.checkout.model;

import com.google.common.collect.Collections2;

import java.util.*;
import java.util.function.Consumer;

public class Cart {

    public enum CartStatus {OPENED, ACCOMPLISHED, ABANDONED}

    private String cartId;

    public Map<String, CartItem> getMapItems() {
        return mapItems;
    }

    public void setMapItems(Map<String, CartItem> mapItems) {
        this.mapItems = mapItems;
    }

    private Map<String, CartItem> mapItems = new HashMap<>();


    private CartStatus status = CartStatus.OPENED;

    public String getCartId() {
        return this.cartId;
    }

    public CartStatus getStatus() {
        return this.status;
    }

    public void setCartId(final String cartId) {
        this.cartId = cartId;
    }

    public void setItems(final ArrayList<CartItem> items) {

        //am i correct??
        this.mapItems.clear();

        //update map
        for(CartItem item : items)
            this.mapItems.put(item.getCartItemId(),item);

    }

    public void setStatus(final CartStatus status) {
        this.status = status;
    }

    public Collection<CartItem> getItems() {

        return this.mapItems.values();
    }

    public boolean isOpened() {
        return CartStatus.OPENED.equals(this.status);
    }

    public boolean isAccomplished() {
        return CartStatus.ACCOMPLISHED.equals(this.status);
    }

    public void accomplished() {
        this.status = CartStatus.ACCOMPLISHED;
    }


    public Double getPrice() {

        Collection<CartItem> items = getItems();
        Double total = 0.0;

        for(CartItem ci : items)
            total += ci.getPrice();


        return total;
    }

}
