package br.com.avaliacao.checkout.http;

import br.com.avaliacao.checkout.db.CartDBInMemory;
import br.com.avaliacao.checkout.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Cart controller for RESTful services
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    /**
     * Cart DB component ref
     */
    @Autowired
    private CartDBInMemory cartDB;

    /**
     * Create a new cart from scratch. Return the cart id as UUID
     * TODO: CHECK WHETHER THIS AUTHENTICATED USER ALREADY HAS A CART! Allow only one per user
     * TODO: Recover an old cart for the same user
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Cart createCart(){
        return cartDB.createNewCart();
    }

    /**
     * Retrieve a cart state of some code
     * @param cartId
     * @return
     */
    @RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
    public ResponseEntity<Cart> getCart(@PathVariable String cartId){
        Optional<Cart> optCart = cartDB.findOne(cartId);
        if(optCart.isPresent())
            return new ResponseEntity<Cart>(optCart.get(), HttpStatus.OK);
        else
            return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);

    }

    /**
     * Delete a given cart
     * @param cartId
     */
    @RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
    public void deleteCart(@PathVariable String cartId){
        cartDB.deteleCart(cartId);
    }

    /**
     * Creates a new cart item, based on cartId, prodId and initial quantity
     * @param cartId
     * @param codeProd
     * @param quantity
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{cartId}")
    public String addItem(@PathVariable String cartId, String codeProd, Integer quantity){
        return cartDB.includeNewItem(codeProd, cartId, quantity);
    }

    /**
     * Update a given cart item quantity
     * @param cartId
     * @param cartItemId
     * @param quantity
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{cartId}/{cartItemId}")
    public String updateItemQtd(@PathVariable String cartId, @PathVariable String cartItemId, Integer quantity){
        return cartDB.updateCartItem(cartId, cartItemId, quantity).getCartItemId();
    }


}