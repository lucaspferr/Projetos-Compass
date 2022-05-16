package com.MS.history.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "paymentMethod")
public class PaymentMethod {

    @Transient
    public static final String SEQUENCE_NAME = "payments_sequence";

    @Id
    private Long payment_id;

    private String type;
    private Double discount;
    private Boolean status;

    private Long purchase_id;

}
