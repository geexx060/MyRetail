package com.myRetail.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myRetail.dao.ProductRepository;
import com.myRetail.exception.IdMatchingException;
import com.myRetail.exception.IdNotFoundException;
import com.myRetail.product.CurrentPrice;
import com.myRetail.product.Product;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    private final String TARGET_URI = "https://redsky.target.com/v2/pdp/tcin/";
    private final String URI_EXCLUDES = "?excludes=taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    public Map<String, Object> getProductById(String id) throws IdNotFoundException {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String uri = TARGET_URI + id + URI_EXCLUDES;

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode head = root.path("product");

            //retrieve product title
            String title = head.get("item").
                    get("product_description").
                    get("title").
                    textValue();

            //retrieve product offer price
            BigDecimal price = head.get("price").
                                get("offerPrice").
                                get("price").decimalValue();

            //create product object
            //Used "USD" as currency code because that is no longer on the product object from redsky
            CurrentPrice currentPrice = new CurrentPrice(price, "USD");
            Product product = new Product(id, title, currentPrice);

            Map<String, Object> result= new HashMap();
            result.put("product",product);

            return result;
        } catch (Exception e){
            throw new IdNotFoundException("Could not find product with ID: " + id);
        }
    }

    public Product getById(String id) throws IdNotFoundException{
        try{
            if(repository.findProductById(id)==null){
                return null;
            }
            return repository.findProductById(id);
        } catch (Exception e){
        throw new IdNotFoundException("Could not find product with ID: " + id + " in MongoDb");
        }
    }

    public Product updateProduct(String id, Product product) throws IdNotFoundException, IdMatchingException {
        if(!id.equals(product.getId())){
            throw new IdMatchingException("Path ID: " + id + " does not equal productId: " + product.getId());
        }
        if(repository.findProductById(id)==null){
            throw new IdNotFoundException("Could not find product with ID: " + id);
        }
        return repository.save(product);
        }
}