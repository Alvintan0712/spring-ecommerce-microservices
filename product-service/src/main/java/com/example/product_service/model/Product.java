package com.example.product_service.model;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Document(value = "product")
@AllArgsConstructor
@Builder
@Data
public class Product {
    @Id
    private String id;

    public Product() {
        this.id = UuidCreator.getTimeOrderedEpoch().toString();
    }

    public Product(String name, String description, BigDecimal price) {
        this.id = UuidCreator.getTimeOrderedEpoch().toString();
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private String name;
    private String description;
    private BigDecimal price;
}
