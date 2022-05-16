package com.MS.checkout.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistory{

    private Long user_id;
    private PaymentDTO paymentMethod;
    private List<ProductHistory> products;
    private Double total;
    private String date;

}
