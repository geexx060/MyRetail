package com.myRetail.dao;

import com.myRetail.product.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, Id> {

    Product findProductById(String id);
    void deleteProductByid(String id);

}