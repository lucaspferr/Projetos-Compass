package com.MS.history.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDTO {

    private String name;
    private String description;
    private String color;
    private String size;
    private Double price;
    private Integer quantity;
}
