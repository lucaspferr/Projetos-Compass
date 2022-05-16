package com.MS.history.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "purchases")
public class Purchases{

    @Transient
    public static final String SEQUENCE_NAME = "purchases_sequence";

    @Id
    private Long purchase_id;

    @DocumentReference
    private PaymentMethod payment_Method;

    @DocumentReference
    private List<Products> products;

    private Double total;
    private LocalDate date;

    private Long history_id;

}
