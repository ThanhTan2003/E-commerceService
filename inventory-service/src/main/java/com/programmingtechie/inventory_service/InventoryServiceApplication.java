package com.programmingtechie.inventory_service;

import com.programmingtechie.inventory_service.model.Inventory;
import com.programmingtechie.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner LoadData (InventoryRepository inventoryRepository)
//	{
//		return  args ->
//		{
//			Inventory inventory1 = new Inventory();
//			inventory1.setSkuCode("iphone_13");
//			inventory1.setQuantity(100);
//
//			Inventory inventory2 = new Inventory();
//			inventory2.setSkuCode("iphone_13_red");
//			inventory2.setQuantity(0);
//
//			inventoryRepository.save(inventory1);
//			inventoryRepository.save(inventory2);
//		};
//	}

}
