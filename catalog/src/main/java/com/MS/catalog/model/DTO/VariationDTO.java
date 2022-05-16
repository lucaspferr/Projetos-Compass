package com.MS.catalog.model.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariationDTO {

    private long variant_id;
    @NotNull @NotBlank
    private String color;
    @NotNull @NotBlank
    private String size;
    @NotNull
    private Double price;
    @NotNull
    private Integer quantity;
    @NotNull
    private Long product_id;

}
