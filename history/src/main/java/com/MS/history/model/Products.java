package com.MS.history.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Products {

    @Transient
    public static final String SEQUENCE_NAME = "products_sequence";

    @Id
    private Long product_id;

    private String name;
    private String description;
    private String color;
    private String size;
    private Double price;
    private Integer quantity;

    private Long purchase_id;
}
