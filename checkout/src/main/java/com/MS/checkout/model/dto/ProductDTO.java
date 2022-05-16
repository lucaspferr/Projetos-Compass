package com.MS.checkout.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long product_id;
    private String name;
    private String description;
    private Boolean active;
    private List<Long> category_ids;

}
