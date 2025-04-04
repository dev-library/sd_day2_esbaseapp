package com.samsungdisplay.esbaseapp.repository;

import com.samsungdisplay.esbaseapp.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEsRepository extends ElasticsearchRepository<ProductDocument, Long> {
    // 필요시 커스텀 쿼리 메서드 정의 가능
}