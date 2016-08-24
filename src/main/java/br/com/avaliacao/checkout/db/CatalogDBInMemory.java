package br.com.avaliacao.checkout.db;

import br.com.avaliacao.checkout.model.Cart;
import br.com.avaliacao.checkout.model.CartItem;
import br.com.avaliacao.checkout.model.Produto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope()
public class CatalogDBInMemory {

    private static Map<String, Produto> productOffers = new HashMap<>();

    private static Map<String, Cart> carts = new HashMap<>();

    public CatalogDBInMemory(){

        Produto prod1 = createProduct(UUID.randomUUID().toString(), "Force");
        productOffers.put(prod1.getCodigo(), prod1);

        Produto prod2 = createProduct(UUID.randomUUID().toString(), "Shox");
        productOffers.put(prod2.getCodigo(), prod2);

        Produto prod3 = createProduct(UUID.randomUUID().toString(), "Endless Summer");
        productOffers.put(prod3.getCodigo(), prod3);

        Produto prod4 = createProduct(UUID.randomUUID().toString(), "Runner");
        productOffers.put(prod4.getCodigo(), prod4);

    }


    /**
     * Create some fake product
     * @param id
     * @param nome
     * @return
     */
    private Produto createProduct(String id, String nome) {
        Produto p = new Produto();

        p.setCodigo(id);
        p.setNome(nome);
        p.setMarca("Nike");
        p.setPreco(200.0);

        return p;
    }

    public List<Produto> findAll(){
        return new ArrayList(productOffers.values());
    }

    public Optional<Produto> findById(String prodId){

        return Optional.ofNullable(productOffers.get(prodId));

    }

}
