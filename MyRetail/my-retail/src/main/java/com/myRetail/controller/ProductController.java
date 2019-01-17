package com.myRetail.controller;

import com.myRetail.exception.IdMatchingException;
import com.myRetail.exception.IdNotFoundException;
import com.myRetail.product.Product;
import com.myRetail.service.ProductService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //GET call to MongoDb
    @GetMapping("/{id}")
    public Product findProductById(@PathVariable(value="id") String id) throws IdNotFoundException {
        try{
            Product product = productService.getById(id);
            return product;
        } catch (Exception e){
            throw new IdNotFoundException("Could not find product with ID: " + id + " in MongoDb");
        }
    }

    //Update product in MongoDb
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable(value="id") String id, @RequestBody Product product) throws IdNotFoundException,IdMatchingException {
        try{
            return productService.updateProduct(id, product);
        }
        catch (IdNotFoundException e) {
            throw new IdNotFoundException("Could not update product with ID: " + id);
        } catch (IdMatchingException e){
            throw new IdMatchingException("Path ID: " + id + " does not equal productId: " + product.getId());
        }
    }

    //GET product from redsky
    @GetMapping("/redsky/{id}")
    public Map<String, Object> getProductById(@PathVariable(value="id") String id) throws IdNotFoundException {
        try{
            return productService.getProductById(id);
        } catch (Exception e){
            throw new IdNotFoundException("Could not find product with ID: " + id);
        }
    }
}