package com.MS.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "variation")
public class Variation {

    @Id
    private Long variant_id;

    @Transient
    public static final String SEQUENCE_NAME = "variants_sequence";


    private String color;
    private String size;
    private Double price;
    private int quantity;
    private Long product_id;

}
