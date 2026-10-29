package com.example.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/products")
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    private final List<Product> products = List.of(
        new Product(1L, "Laptop", 55000.0, 10),
        new Product(2L, "Keyboard", 1500.0, 25),
        new Product(3L, "Mouse", 800.0, 40)
    );

    @GetMapping
    public List<Product> getProducts() {
        return products;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return products.stream()
            .filter(product -> product.id().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public record Product(Long id, String name, double price, int stock) {}
}
