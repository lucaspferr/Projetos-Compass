package com.MS.history.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasesDTO {

    private PaymentDTO paymentMethod;
    private List<ProductsDTO> products;
    private Double total;
    private String date;

}
