package com.samsungdisplay.esbaseapp.document;

import co.elastic.clients.elasticsearch._types.mapping.IndexOptions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "products")  // ES index 이름
public class ProductDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard") // Full-text 검색용
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String brand;

    @Field(type = FieldType.Integer)
    private int price;

    @Field(type = FieldType.Double)
    private double rating;
}