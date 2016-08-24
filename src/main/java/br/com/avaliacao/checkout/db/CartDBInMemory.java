package br.com.avaliacao.checkout.db;

import br.com.avaliacao.checkout.model.Cart;
import br.com.avaliacao.checkout.model.CartItem;
import br.com.avaliacao.checkout.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Emulates a DB component for a shopping cart.
 * The separation of business and persistence layers was not considered for simplicity
 *
 */

@Service
@Scope()
public class CartDBInMemory {

    /**
     * instance to catalog component
     */
    @Autowired
    private CatalogDBInMemory catalogDB;

    /**
     * Internal state of this memory repo
     */
    private static Map<String, Cart> carts = new HashMap<>();

    /**
     * Creates a completely new cart instance
     * @return
     */
    public Cart createNewCart() {
        // Decentralize the entity id generation using UUID. Even using a
        // DB sequence, that is non-blocking, this will demand a centralized resource.
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setCartId(cartId);
        carts.put(cartId, cart);
        return cart;
    }

    /**
     * Creates a new cart item, based on some product id and cart id
     *
     * @param proId
     * @param cartId
     * @param quantity
     * @return
     */
    public String includeNewItem(String proId, String cartId, Integer quantity) {

        Optional<Cart> optCart = findOne(cartId);
        Optional<Produto> optProd = catalogDB.findById(proId);

        Cart cart = optCart.orElse(createNewCart());

        if(optProd.isPresent()){
            Produto produto = optProd.get();
            CartItem item;

            if((item = jaPossuiEsteProduto(cart, produto))==null){

                //decentralized id generation
                String cartItemId = UUID.randomUUID().toString();

                item = new CartItem();

                item.setCartItemId(cartItemId);
                item.setProduto(produto);
                item.setQuantity(quantity);
                cart.getMapItems().put(item.getCartItemId(),item);

            }else{
                item.setQuantity( item.getQuantity() + quantity );
            }

            return item.getCartItemId();
        }else{
            throw new IllegalArgumentException("Product {} not found!");
        }
    }

    /**
     * Retorna o item que ja possui este produto, caso possua
     * @param cart
     * @param p
     * @return
     */
    private CartItem jaPossuiEsteProduto(Cart cart, Produto p) {
        Collection<CartItem> cartItems = cart.getItems();
        for(CartItem item : cartItems){
            if(item.getProduto().getCodigo().equals(p.getCodigo())){
                return item;
            }
        }
        return null;
    }

    /**
     * Find one cart instance
     * @param id
     * @return
     */
    public Optional<Cart> findOne(String id) {
        Cart cart = carts.get(id);
        return Optional.ofNullable(cart);
    }


    /**
     * deletes a cart entity
     * @param id
     */
    public void deteleCart(String id) {
        carts.remove(id);
    }

    /**
     * completely clears the cart db memory
     */
    public void clear() {
        carts.clear();
    }

    /**
     * Increment, decrement or exclude cart items
     * @param cartId
     * @param cartItemId
     * @param quantity
     * @return
     */
    public CartItem updateCartItem(String cartId, String cartItemId, Integer quantity) {

        Optional<Cart> optCart = findOne(cartId);

        if(optCart.isPresent()){

            Cart cart = optCart.get();

            CartItem cartItem = cart.getMapItems().get(cartItemId);

            if(cartItem!=null)

                if(quantity<=0)
                    cart.getMapItems().remove(cartItemId);
                else
                    cartItem.setQuantity(quantity);

            else
                throw new IllegalArgumentException("Cart item {} not found!");

            return cartItem;

        }else
            throw new IllegalArgumentException("Cart id {} not found!");


    }
}
