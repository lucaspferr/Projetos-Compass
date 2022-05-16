package com.MS.history.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutHistory {

    private Long user_id;
    private PaymentDTO paymentMethod;
    private List<ProductsDTO> products;
    private Double total;
    private String date;

}
