package com.MS.catalog.model.DTO;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO{

    private Long product_id;
    @NotNull @NotEmpty
    private String name;
    @NotNull @NotEmpty
    private String description;
    @NotNull
    private Boolean active;
    @NotNull
    private List<Long> category_ids;
}
