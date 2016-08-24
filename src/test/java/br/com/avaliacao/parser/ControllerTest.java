package br.com.avaliacao.parser;

import br.com.avaliacao.AvaliacaoJavaApplication;
import br.com.avaliacao.checkout.db.CartDBInMemory;
import br.com.avaliacao.checkout.db.CatalogDBInMemory;
import br.com.avaliacao.checkout.model.Cart;
import br.com.avaliacao.checkout.model.Produto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Controller tests
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AvaliacaoJavaApplication.class)
@WebAppConfiguration
public class ControllerTest {
    @Autowired
    private CartDBInMemory cartDB;

    @Autowired
    private CatalogDBInMemory catalogDB;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private Cart testCart;
    private Produto testProduct;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    /**
     * Prepare a little mass for test
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.testCart = this.cartDB.createNewCart();

        List<Produto> products = catalogDB.findAll();
        this.testProduct = products.get(0);
    }

    /**
     * Create an empty cart
     * @throws Exception
     */
    @Test
    public void cartCreation() throws Exception {
        mockMvc.perform(post("/cart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8) )
                .andExpect(jsonPath("$.status").value("OPENED"))
                .andExpect(jsonPath("$.cartId").isNotEmpty())
        ;
    }


    /**
     * Add some cart item
     * @throws Exception
     */
    @Test
    public void cartAddItem() throws Exception {

        String cartId = this.testCart.getCartId();
        String prodId = this.testProduct.getCodigo();

        mockMvc.perform(post("/cart/{cartId}/", cartId)
                .param("codeProd", prodId)
                .param("quantity", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8") )
        ;

    }

    /**
     * Check the quantity update durability
     * @throws Exception
     */
    @Test
    public void cartUpdateDelete() throws Exception {

        String cartId = this.testCart.getCartId();
        List<Produto> allProds = this.catalogDB.findAll();
        String prodId = allProds.get(2).getCodigo();

        Integer qtdOriginal = 5;
        Integer qtdAlterada=10;

        String cartItemId = this.cartDB.includeNewItem(prodId,cartId,qtdOriginal);

        //check the original quantity for the cartItem
        mockMvc.perform(get("/cart/{cartId}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.cartId").value(cartId))
                .andExpect(jsonPath("$.mapItems." + cartItemId + ".produto.codigo").value(prodId))
                .andExpect(jsonPath("$.mapItems." + cartItemId + ".quantity").value(qtdOriginal))
        ;

        //update the cart item
        mockMvc.perform(post("/cart/{cartId}/{cartItemId}", cartId, cartItemId)
                .param("quantity", ""+qtdAlterada))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(cartItemId))
        ;

        //check the new quantity for the cart item
        mockMvc.perform(get("/cart/{cartId}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.cartId").value(cartId))
                .andExpect(jsonPath("$.mapItems." + cartItemId + ".produto.codigo").value(prodId))
                .andExpect(jsonPath("$.mapItems." + cartItemId + ".quantity").value(qtdAlterada))
        ;

        //cart delete
        mockMvc.perform(delete("/cart/{cartId}", cartId)).andExpect(status().isOk());

        //check the cart deletion
        mockMvc.perform(get("/cart/{cartId}", cartId)).andExpect(status().isNotFound());

    }


    /**
     * A simple 404 corner case test
     * @throws Exception
     */
    @Test
    public void cart404NotFound() throws Exception {
        String cartId = UUID.randomUUID().toString();

        mockMvc.perform(get("/cart/{cartId}", cartId)).andExpect(status().isNotFound());
    }

}