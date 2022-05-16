package com.MS.checkout.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO{

    @NotNull @NotEmpty
    private String type;
    @NotNull
    private Double discount;
    @NotNull
    private Boolean status;

}
