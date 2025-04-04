package com.samsungdisplay.esbaseapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ElasticsearchConnectionChecker implements CommandLineRunner {
	
	private final ElasticsearchClient elasticsearchClient;

    @Override
    public void run(String... args) throws Exception {
        var info = elasticsearchClient.info();
        System.out.println("Elasticsearch 클러스터 이름: " + info.clusterName());
        System.out.println("Elasticsearch 버전: " + info.version().number());
    }
}