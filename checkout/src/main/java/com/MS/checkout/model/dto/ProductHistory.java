package com.MS.checkout.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistory {

    private String name;
    private String description;
    private String color;
    private String size;
    private Double price;
    private Integer quantity;

}
