package com.myRetail.myretail;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.myRetail.controller.ProductController;
import com.myRetail.dao.ProductRepository;
import com.myRetail.exception.IdMatchingException;
import com.myRetail.exception.IdNotFoundException;
import com.myRetail.product.CurrentPrice;
import com.myRetail.product.Product;
import com.myRetail.service.ProductService;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductController productController;

    @Before
    public void setup(){
        CurrentPrice currentPrice = new CurrentPrice(BigDecimal.valueOf(14.87), "USD");
        Product testProduct = new Product("75501", "TestProduct", currentPrice);
        repository.save(testProduct);
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }

    //****MongoDB tests****

    //retrieve correct product
    @Test
    public void FindProductByIdTest() throws IdNotFoundException {
        Product result = productController.findProductById("75501");
        assertEquals(result.getId(), "75501");
    }

    //can't find product Id
    @Test
    public void FindProductByIdWhereIdDoesNotExistTest() throws IdNotFoundException {
        assertNull(productController.findProductById("270"));
        assertNull(productController.findProductById("99999999999"));
        assertNull(productController.findProductById("-20"));
        assertNull(productController.findProductById("0"));
    }

    //update happy path
    @Test
    public void UpdateProductTest() throws IdNotFoundException, IdMatchingException {
        Product oldProduct = productController.findProductById("75501");
        assertEquals(oldProduct.getId(), "75501");
        oldProduct.getCurrentPrice().setValue(BigDecimal.valueOf(45.76));
        productController.updateProduct("75501", oldProduct);
        Product result = productController.findProductById("75501");
        assertEquals(result.getCurrentPrice().getValue(),BigDecimal.valueOf(45.76));
    }

    //update with different path id and productId; throws IdMatchingException
    @Test(expected = IdMatchingException.class)
    public void UpdateProductIdsDoNotMatchTest() throws IdNotFoundException,IdMatchingException {
        Product otherProduct = productController.findProductById("75501");
        assertEquals(otherProduct.getId(), "75501");
        otherProduct.setId("865");
        productController.updateProduct("75501", otherProduct);
    }

    //update product that doesn't exist; throws IdNotFoundException
    @Test(expected = IdNotFoundException.class)
    public void UpdateProductIdNotFoundTest() throws IdNotFoundException,IdMatchingException {
        Product product = productController.findProductById("75501");
        assertEquals(product.getId(), "75501");
        product.setId("434");
        productController.updateProduct("434", product);
    }

    //****redsky tests****

    //get product with title and current price
    @Test
    public void GetProductByIdTest() throws IdNotFoundException {
        Product result = (Product) productController.getProductById("13860428").get("product");
        assertEquals(result.getName(), "The Big Lebowski (Blu-ray)");
    }

    //Try to get product that doesn't exist
    @Test(expected = IdNotFoundException.class)
    public void GetProductByIdWhereIdDoesNotExistTest() throws IdNotFoundException {
        Product result = (Product) productController.getProductById("342");
    }




}

