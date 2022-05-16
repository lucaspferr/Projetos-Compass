package com.MS.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "purchase")
public class Purchase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchase_id;

    private Long user_id;
    private Long payment_id;

    @OneToMany @JoinColumn(name = "purchase_id")
    private List<Cart> cart;
}
