package com.project.shop.Product;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product productDetails) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        product.setImageUrl(productDetails.getImageUrl());
        product.setIngredient(productDetails.getIngredient());
        product.setRecommender(productDetails.getRecommender());
        product.setHtu(productDetails.getHtu());
        return productRepository.save(product);
    }

    public void deleteProduct (String id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategory (String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProductsByName (String name) {
        return productRepository.findByNameContaining(name);
    }
}
