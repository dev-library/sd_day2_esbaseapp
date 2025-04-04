package com.samsungdisplay.esbaseapp.service;

import com.samsungdisplay.esbaseapp.document.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchClient client;

    public Page<ProductDocument> search(
            String keyword,
            Integer minPrice,
            Integer maxPrice,
            Double minRating,
            Double maxRating,
            String sortOption,
            int page,
            int size
    ) {
        List<Query> mustQueries = new ArrayList<>();

        // keyword (name or brand)
        if (StringUtils.hasText(keyword)) {
            mustQueries.add(Query.of(q -> q
                    .bool(b -> b.should(
                            MatchQuery.of(m -> m.field("name").query(keyword))._toQuery(),
                            MatchQuery.of(m -> m.field("brand").query(keyword))._toQuery()
                    ))
            ));
        }

        // range queries
        if (minPrice != null || maxPrice != null) {
            NumberRangeQuery.Builder numberRange = new NumberRangeQuery.Builder().field("price");
            if (minPrice != null) numberRange.gte((double) minPrice);
            if (maxPrice != null) numberRange.lte((double) maxPrice);
            RangeQuery rangeQuery = RangeQuery.of(r -> r.number(numberRange.build()));
            mustQueries.add(Query.of(q -> q.range(rangeQuery)));
        }

        if (minRating != null || maxRating != null) {
            NumberRangeQuery.Builder numberRange = new NumberRangeQuery.Builder().field("rating");
            if (minRating != null) numberRange.gte(minRating);
            if (maxRating != null) numberRange.lte(maxRating);
            RangeQuery rangeQuery = RangeQuery.of(r -> r.number(numberRange.build()));
            mustQueries.add(Query.of(q -> q.range(rangeQuery)));
        }

        // 최종 쿼리 생성
        Query finalQuery = Query.of(q -> q.bool(b -> b.must(mustQueries)));

        // 정렬 옵션 설정
        SortOptions sort = null;
        if (sortOption != null) {
            switch (sortOption) {
                case "priceAsc" -> sort = SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Asc)));
                case "priceDesc" -> sort = SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Desc)));
                case "ratingAsc" -> sort = SortOptions.of(s -> s.field(f -> f.field("rating").order(SortOrder.Asc)));
                case "ratingDesc" -> sort = SortOptions.of(s -> s.field(f -> f.field("rating").order(SortOrder.Desc)));
            }
        }
        final SortOptions finalSort = sort;

        try {
            SearchResponse<ProductDocument> response = client.search(s -> {
                s.index("products")
                 .query(finalQuery)
                 .from(page * size)
                 .size(size);
                if (finalSort != null) {
                    s.sort(finalSort);
                }
                return s;
            }, ProductDocument.class);

            List<ProductDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

            long total = response.hits().total() != null ? response.hits().total().value() : 0;

            return new PageImpl<>(results, PageRequest.of(page, size), total);

        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch 검색 실패", e);
        }
    }
}