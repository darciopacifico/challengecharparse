package br.com.avaliacao.parser;

import br.com.avaliacao.checkout.db.CartDBInMemory;
import br.com.avaliacao.checkout.db.CatalogDBInMemory;
import br.com.avaliacao.checkout.model.Cart;
import br.com.avaliacao.checkout.model.CartItem;
import br.com.avaliacao.checkout.model.Produto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by darcio on 8/23/16.
 */
@SpringApplicationConfiguration(AvaliacaoJavaApplicationTest.class)
@SpringBootApplication(scanBasePackages = "br.com.avaliacao")
@RunWith(SpringJUnit4ClassRunner.class)
public class CartDBTest {


    @Autowired
    private CartDBInMemory cartDB;

    @Autowired
    private CatalogDBInMemory catalogDB;


    @Test
    public void testRegularCycle(){

        //cria novo carrinho
        Cart cart = cartDB.createNewCart();

        Assert.notNull(cart,"the empty initial cart should be created");
        Assert.notNull(cart.getCartId(),"this cart should have an unique id");
        Assert.isTrue(cart.getItems().isEmpty(),"this cart should be empty");

        //inclui novo prod
        List<Produto> allProds = catalogDB.findAll();

        Produto prod = allProds.get(0);
        Assert.notNull(prod,"A set of products should exists!");
        Assert.notNull(prod.getCodigo(),"this product should have an id!");

        String cartItemId = cartDB.includeNewItem(prod.getCodigo(),cart.getCartId(),1);

        //testa se deu certo
        Optional<Cart> cartRet = cartDB.findOne(cart.getCartId());
        Assert.isTrue(cartRet.isPresent(),"the previous created cart should be accessible by cartDB");

        CartItem cartItemRet = cart.getMapItems().get(cartItemId);
        Assert.notNull(cartItemRet,"this cart should have one item!");

        //compara com o prod esperado
        Assert.isTrue(cartItemRet.getProduto().getCodigo().equals(prod.getCodigo()),"this item should be the same previously inserted");
        Assert.isTrue(cartItemRet.getQuantity().equals(1),"the item initial quantity should be 1");


        //updating the cart state
        cartDB.updateCartItem(cart.getCartId(),cartItemId,2);


        //finding the cart
        Optional<Cart> cartRet2 = cartDB.findOne(cart.getCartId());
        Assert.isTrue(cartRet.isPresent(), "the previous created cart should be accessible by cartDB");

        CartItem cartItemRet2 = cart.getMapItems().get(cartItemId);

        Assert.isTrue(cartItemRet2.getQuantity().equals(2),"the item initial quantity should be 2 now");

    }


    @Test
    public void testCartNotFound(){

        String fakeCartId = UUID.randomUUID().toString();

        Optional<Cart> cart = cartDB.findOne(fakeCartId);

        Assert.isTrue(!cart.isPresent(),"This random id cart should not be found!");

    }


}
