package com.MS.checkout.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariationDTO {

    private Long variant_id;
    private String color;
    private String size;
    private Double price;
    private int quantity;
    private long product_id;
}
