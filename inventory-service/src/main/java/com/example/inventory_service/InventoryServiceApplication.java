package com.example.inventory_service;

import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Optional<Inventory> inventoryOptional = inventoryRepository.findBySkuCode("iphone_16");
			if (inventoryOptional.isEmpty()) {
				Inventory inventory = new Inventory();
				inventory.setSkuCode("iphone_16");
				inventory.setQuantity(100);
				inventoryRepository.save(inventory);
			}

			inventoryOptional = inventoryRepository.findBySkuCode("iphone_16_red");
			if (inventoryOptional.isEmpty()) {
				Inventory inventory = new Inventory();
				inventory.setSkuCode("iphone_16_red");
				inventory.setQuantity(0);
				inventoryRepository.save(inventory);
			}
		};
	}
}
