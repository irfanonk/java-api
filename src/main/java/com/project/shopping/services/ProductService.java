package com.project.shopping.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.shopping.entities.Product;
import com.project.shopping.repos.ProductRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product newProduct) {
        if (productRepository.existsById(id)) {
            newProduct.setId(id);
            return productRepository.save(newProduct);
        }
        return null;
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void importProductsFromExternalURL(String externalURL) {
        // Make HTTP request to the external URL to fetch products
        JsonNode response = restTemplate.getForObject(externalURL, JsonNode.class);

        // Check if response is null
        if (response == null) {
            // Handle the null response, such as logging an error or throwing an exception
            return;
        }
        // Extract the 'data' array from the response
        JsonNode dataArray = response.get("data");

        // Deserialize each product from the array and add them to a list
        List<Product> products = new ArrayList<>();
        if (dataArray != null && dataArray.isArray()) {
            for (JsonNode productNode : dataArray) {
                Product product = deserializeProduct(productNode);
                products.add(product);
            }
        }
        // Bulk save all products
        productRepository.saveAll(products);
    }

    private Product deserializeProduct(JsonNode productNode) {
        try {
            // Create a copy of the JSON node without the 'id' field
            ObjectNode copy = objectMapper.createObjectNode();
            copy.setAll((ObjectNode) productNode);
            copy.remove("id");
            // System.out.println("copy= " + copy);

            // Deserialize the modified JSON node into a Product object
            return objectMapper.treeToValue(copy, Product.class);
        } catch (JsonProcessingException e) {
            // Handle the exception (e.g., log the error or throw a custom exception)
            e.printStackTrace(); // Example: print the stack trace
            return null; // Return null or throw a custom exception based on your error handling strategy
        }
    }
}
