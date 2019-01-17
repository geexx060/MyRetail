package com.myRetail.myretail;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myRetail.dao.ProductRepository;
import com.myRetail.product.CurrentPrice;
import com.myRetail.product.Product;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Before
    public void setup(){
        CurrentPrice currentPrice = new CurrentPrice(BigDecimal.valueOf(11.64), "USD");
        Product testProduct = new Product("81475", "TestProductRest", currentPrice);
        repository.save(testProduct);
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }

    @Test
    public void findProductByIdTest() throws Exception {
        this.mockMvc.perform(get("/products/81475"))
                .andExpect(status().isOk())
                .andExpect(content().json("{id:'81475',name:'TestProductRest',currentPrice:{value:11.64,currencyCode:USD}}"));
    }

    @Test
    public void findProductByIdProductDoesNotExistTest() throws Exception {
            this.mockMvc.perform(get("/products/2321"))
                    .andExpect(content().string(""));
    }

    //update happy path
    @Test
    public void UpdateProductTest() throws Exception {
        CurrentPrice currentPrice = new CurrentPrice(BigDecimal.valueOf(45.22), "USD");
        Product updatedProduct = new Product("81475", "TestProductRest", currentPrice);

        ObjectMapper mapper = new ObjectMapper();
        String jsonUpdatedProduct = mapper.writeValueAsString(updatedProduct);

        this.mockMvc.perform(put("/products/81475")
                                .contentType(MediaType.APPLICATION_JSON).content(jsonUpdatedProduct))
            .andExpect(status().isOk())
            .andExpect(content().json("{id:'81475',name:'TestProductRest',currentPrice:{value:45.22,currencyCode:USD}}"));
    }


    //update with different path id and productId; throws IdMatchingException
    @Test
    public void UpdateProductIdsDoNotMatchTest() throws Exception {
        try{
        CurrentPrice currentPrice = new CurrentPrice(BigDecimal.valueOf(11.64), "USD");
        Product updatedProduct = new Product("573924", "TestProductRest", currentPrice);

        ObjectMapper mapper = new ObjectMapper();
        String jsonUpdatedProduct = mapper.writeValueAsString(updatedProduct);

        this.mockMvc.perform(put("/products/81475")
                .contentType(MediaType.APPLICATION_JSON).content(jsonUpdatedProduct));
    }catch (NestedServletException e) {
            Assert.assertTrue(e.getCause().getMessage().equals("Path ID: 81475 does not equal productId: 573924"));
        }
    }

    //update product that doesn't exist; throws IdNotFoundException
    @Test
    public void UpdateProductIdNotFoundTest() throws Exception {
        try {
            CurrentPrice currentPrice = new CurrentPrice(BigDecimal.valueOf(11.64), "USD");
            Product updatedProduct = new Product("00000", "TestProductRest", currentPrice);

            ObjectMapper mapper = new ObjectMapper();
            String jsonUpdatedProduct = mapper.writeValueAsString(updatedProduct);

            this.mockMvc.perform(put("/products/00000")
                    .contentType(MediaType.APPLICATION_JSON).content(jsonUpdatedProduct));
        } catch (NestedServletException e) {
            Assert.assertTrue(e.getCause().getMessage().equals("Could not update product with ID: 00000"));
        }
    }
}
