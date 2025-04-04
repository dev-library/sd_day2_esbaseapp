package com.samsungdisplay.esbaseapp.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.samsungdisplay.esbaseapp.document.ProductDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final ElasticsearchClient client;

    private static final int DATA_SIZE = 300_000;
    private static final int BATCH_SIZE = 1_000;
    
    @Override
    public void run(String... args) throws Exception {
    	long existingCount = client.count(c -> c.index("products")).count();

    	if (existingCount >= DATA_SIZE) {
    	    System.out.println("이미 30만 건 이상 존재 (" + existingCount + ")");
    	    return;
    	}

    	System.out.println("Bulk로 30만 건 입력 시작...");

    	String[] baseNames = {"Laptop", "Phone", "Tablet", "Shoes", "Jacket", "Cookware", "Headphones", "Monitor", "Chair", "Desk"};
    	String[] brands = {"BrandA", "BrandB", "BrandC", "BrandD", "BrandE"};
    	String[] categories = {"Electronics", "Fashion", "Kitchen", "Office", "Furniture"};
    	Random rand = new Random();

    	for (int i = 1; i <= DATA_SIZE; i += BATCH_SIZE) {
    	    List<BulkOperation> operations = new ArrayList<>();

    	    for (int j = 0; j < BATCH_SIZE && (i + j) <= DATA_SIZE; j++) {
    	        int id = i + j;

    	        String baseName = baseNames[rand.nextInt(baseNames.length)];
    	        String brand = brands[rand.nextInt(brands.length)];
    	        String category = categories[rand.nextInt(categories.length)];

    	        String name = baseName + " Model " + (100 + rand.nextInt(900));
    	        Integer price = 10_000 + rand.nextInt(990_000); // 1만원 ~ 100만원
    	        Double rating = Math.round((1.0 + rand.nextDouble() * 4.0) * 10.0) / 10.0; // 1.0 ~ 5.0

    	        ProductDocument doc = ProductDocument.builder()
    	                .id((long)id)
    	                .name(name)
    	                .brand(brand)
    	                .price(price)
    	                .rating(rating)
    	                .build();

    	        BulkOperation op = BulkOperation.of(b -> b
    	                .index(idx -> idx
    	                        .index("products")
    	                        .id(String.valueOf(doc.getId()))
    	                        .document(doc)
    	                ));

    	        operations.add(op);
    	    }

    	    client.bulk(BulkRequest.of(b -> b.index("products").operations(operations)));

    	    if ((i / BATCH_SIZE) % 10 == 0) {
    	        System.out.println((i + BATCH_SIZE - 1) + "건 완료");
    	    }
    	}

    	System.out.println("Bulk 입력 완료");
    }
}