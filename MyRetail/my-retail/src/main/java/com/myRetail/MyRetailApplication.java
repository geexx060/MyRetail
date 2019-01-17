package com.myRetail;

import com.myRetail.dao.ProductRepository;
import com.myRetail.product.CurrentPrice;
import com.myRetail.product.Product;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyRetailApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyRetailApplication.class, args);
	}

	@Autowired
    ProductRepository repository;

	//initialize MongoDB with values
	@PostConstruct
    private void setup(){
        CurrentPrice currentPrice = new CurrentPrice(BigDecimal.valueOf(11.64), "USD");
        Product productOne = new Product("111111", "Product_1", currentPrice);
        currentPrice.setValue(BigDecimal.valueOf(24.76));
        Product productTwo = new Product("222222", "Product_2", currentPrice);
        currentPrice.setValue(BigDecimal.valueOf(37.45));
        Product productThree = new Product("333333", "Product_3", currentPrice);

        repository.save(productOne);
        repository.save(productTwo);
        repository.save(productThree);
    }

    @PreDestroy
    private void cleanUp(){
	    repository.deleteAll();
    }

}

