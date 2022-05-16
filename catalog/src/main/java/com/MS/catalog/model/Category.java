package com.MS.catalog.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "category")
public class Category {

    @Transient
    public static final String SEQUENCE_NAME = "categories_sequence";

    @Id
    private Long category_id;

    @NotNull @NotBlank
    private String name;
    @NotNull
    private Boolean active;

    @DocumentReference
    private List<Product> productList = new ArrayList<>();
}
