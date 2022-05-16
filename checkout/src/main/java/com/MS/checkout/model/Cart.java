package com.MS.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cart")
public class Cart{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cart_id;

    private Long variant_id;
    private Integer quantity;

}
