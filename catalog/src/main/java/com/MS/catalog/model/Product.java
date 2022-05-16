package com.MS.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class Product {

    @Transient
    public static final String SEQUENCE_NAME = "products_sequence";

    @Id
    private Long product_id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean active;

    @NotNull
    private List<Long> category_ids;

    @DocumentReference
    private List<Variation> variationList = new ArrayList<>();

//    @DBRef
//    private List<Variation> variations = new ArrayList<>();


}
