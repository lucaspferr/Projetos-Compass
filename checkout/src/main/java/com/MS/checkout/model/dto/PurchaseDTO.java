package com.MS.checkout.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO{

    @NotNull
    private Long user_id;
    @NotNull
    private Long payment_id;
    @NotNull
    private List<CartDTO> cart;

}
